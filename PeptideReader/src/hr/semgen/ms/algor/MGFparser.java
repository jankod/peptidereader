package hr.semgen.ms.algor;

import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.PeakFile;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MGFparser {
	private static Pattern labelPattern = Pattern.compile(".*Label:(.*), Spot Id:.*");
	private static final Logger log = LoggerFactory.getLogger(MGFparser.class);

	/**
	 * Vraca MS name iz prve linije stevica file
	 * 
	 * @param line
	 * @return
	 */
	private static String getStevicaFirstLineMSname(String line) {
		final Matcher matcher = labelPattern.matcher(line);
		if (matcher.find()) {
			int groupCount = matcher.groupCount();
			if (groupCount == 1) {
				return matcher.group(1);
			}
		}

		throw new RuntimeException("Not find MS name in line: " + line);

	}

	public static ArrayList<PeakFile> parseStevicaFiles(File dirStevica) {
		final File[] listFiles = dirStevica.listFiles();
		ArrayList<PeakFile> msList = new ArrayList<PeakFile>(listFiles.length);
		for (File file : listFiles) {
			if (file.isDirectory()) {
				continue;
			}
			log.debug("Parse file: "+ file);
			FileReader fileReader = null;
			try {
				fileReader = new FileReader(file);
				final List<String> lines = IOUtils.readLines(fileReader);

				parseMGFLines(msList, lines);

			} catch (Throwable e) {
				log.error("", e);
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(fileReader);
			}
		}
		return msList;
	}

	private static void parseMGFLines(ArrayList<PeakFile> msList, final List<String> lines) {
		boolean firstLine = true;
		boolean startMSpeaks = false;
		boolean startMSMSHeader = false;
		boolean startMSMSPeaks = false;

		PeakFile ms = new PeakFile();
		ms.setPositive(false);
		ms.setMS(true);
		msList.add(ms);
		ms.setAllPeaks(new ArrayList<Peak>(175));
		PeakFile currentMSMS = null;

		for (String line : lines) {
			if(StringUtils.isEmpty(line)) {
				continue;
			}
			if (firstLine) { // 1
				final String msName = getStevicaFirstLineMSname(line);
				ms.setName(msName);
				startMSpeaks = true;
				firstLine = false;
				continue;
			}

			if (startMSpeaks) { // 2
				if (line.contains("BEGIN IONS")) {
					// zavrsio MS peaks
					startMSpeaks = false;
					startMSMSHeader = true;
					continue;
				}

				Peak p = getPeaks(line);
				ms.addNewPeak(p);
			}

			if (startMSMSHeader) {
				if (line.startsWith("TITLE")) {
					startMSMSHeader = false;
					startMSMSPeaks = true;
					continue;
				}

				if (line.startsWith("PEPMASS=")) {
					String massPrecursor = line.substring(8, line.length());
					currentMSMS = new PeakFile();

					currentMSMS.setAllPeaks(new ArrayList<Peak>(300));
					currentMSMS.setMassPrecursor(Double.valueOf(massPrecursor.trim()).floatValue());
					currentMSMS.setDateCreated(new Date());
					currentMSMS.setMS(false);
					currentMSMS.setParentMS(ms);
					currentMSMS.setPositive(false);

					ms.addMsmsChild(currentMSMS);
					currentMSMS.setName(massPrecursor.trim());
					continue;
				}

			}

			if (startMSMSPeaks) {
				if (line.startsWith("END IONS")) {
					// gotovi ioni
					startMSMSPeaks = false;
					startMSMSHeader = true;
					continue;
				}
				final Peak p = getPeaks(line);
				currentMSMS.addNewPeak(p);
			}

		}
	}

	private static Peak getPeaks(String line) {
		// ovak izgleda nekako: 2153.8599 1237.4131 masa h
		final String[] split = StringUtils.split(line, '\t');
		double mass = Double.parseDouble(split[0]);
		double h = Double.parseDouble(split[1]);
		return new Peak((float) mass, (int) h);
	}

}
