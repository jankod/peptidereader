package hr.semgen.masena.share.mph;

import hr.semgen.masena.share.TwoValue;
import hr.semgen.masena.share.model.PeakFile;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MphUtil {
	private static final Logger log = LoggerFactory.getLogger(MphUtil.class);

	/**
	 * Radi FASTA tako da bude peptide u headeru. Treba vezati query za MSMS id
	 * >PEPTIDE-ID >SKEKEK_2345342234
	 * 
	 * @param peptides
	 * @return
	 */
	public static final String createFasta(Collection<MphQueryPeptidesByMsms> peptides) {
		StringBuilder fasta = new StringBuilder(2000);
		for (MphQueryPeptidesByMsms bro : peptides) {
			final PeakFile neg = bro.getNegativMsms();
			for (String p : bro.getQueryPeptidesNegative()) {
				fasta.append(">").append(p + "_" + neg.getId()).append("\n").append(p).append("\n");

			}
			final PeakFile pos = bro.getPositivieMsms();
			for (String p : bro.getQueryPeptidesPositive()) {
				fasta.append(">").append(p + "_" + pos.getId()).append("\n").append(p).append("\n");

			}
		}
		// log.debug(fasta.toString());
		return fasta.toString();
	}

	/**
	 * Parsira mph blast result, kao broj hitova. '7 qseqid sseqid pident length
	 * mismatch gapopen qstart qend sstart send evalue bitscore qseq sseq gaps
	 * ppos'
	 * 
	 * @param blastResultPath
	 * @return
	 * @throws IOException
	 */
	public final static List<MphBlastHit> parseBlastResult(String blastResultPath) throws IOException {
		List<MphBlastHit> hits = new ArrayList<MphBlastHit>(100);
		FileReader reader = new FileReader(blastResultPath);
		try {
			List<String> lines = IOUtils.readLines(reader);
			for (String line : lines) {
				if (line.startsWith("#")) {
					continue;
				}
//	@formatter:off		
//				          0         1           2           3                 4           5          6         7       8         9       10      11         12         13           14    15           16        17        
//				# Fields: query id, subject id, % identity, alignment length, mismatches, gap opens, q. start, q. end, s. start, s. end, evalue, bit score, query seq, subject seq, gaps, % positives, identical positive  
//		                7 qseqid    sseqid      pident      length            mismatch    gapopen    qstart    qend    sstart    send    evalue  bitscore   qseq       sseq         gaps  ppos         nident    positive  
//  @formatter:on

				// Fields: query id, subject id, % identity, alignment length,
				// mismatches, gap opens, q. start, q. end, s. start, s. end,
				// evalue, bit score, query seq, subject seq, gaps, % positives
				String[] split = StringUtils.split(line, '\t'); // line.split("\t");
				
				MphBlastHit hit = new MphBlastHit();
				hit.queryID = split[0];
				hit.subjectID = split[1];
				hit.percentIdentity = Float.parseFloat(split[2].trim());
				hit.alignmentLength = Integer.parseInt(split[3].trim());
				hit.mismatches = Integer.parseInt(split[4].trim());
				hit.gapOpens = Integer.parseInt(split[5].trim());
				hit.queryStart = Integer.parseInt(split[6].trim());
				hit.queryEnd = Integer.parseInt(split[7].trim());
				hit.subjectStart = Integer.parseInt(split[8].trim());
				hit.subjectEnd = Integer.parseInt(split[9].trim());
				hit.eValue = Float.parseFloat(split[10].trim());
				hit.bitScore = Float.parseFloat(split[11].trim());
				hit.querySeqAligned = split[12].trim();
				hit.subjectSeqAligned = split[13].trim();
				// res.subjectGI = Integer.parseInt(split[14].trim());

				hit.totalGaps = Integer.parseInt(split[14].trim());
				hit.percentageOfpositiveMatches = Float.parseFloat(split[15].trim());
				hit.identical = Integer.parseInt(split[16].trim());
				hit.positive = Integer.parseInt(split[17].trim());
			//	hit.percentageOfIdenticalMatches = Integer.parseInt(split[18].trim());

				final TwoValue<String, Long> ext = MphBlastHit.extractQuerySeqAndMsmsIdFromQueryID(hit.queryID);
				hit.querySequenceFromHeader = ext.getT1();
				hit.msmsIDFromHeader = ext.getT2();

				hits.add(hit);
			}
		} finally {
			IOUtils.closeQuietly(reader);
		}
		return hits;

	}

	private static final Comparator<MphBlastHit> hitComparator = new Comparator<MphBlastHit>() {

		public int compare(MphBlastHit o1, MphBlastHit o2) {
			if (o1.identical < o2.identical) {
				return -1;
			}

			if (o1.identical > o2.identical) {
				return 1;
			}

			if (o1.percentIdentity < o2.percentIdentity) {
				return -1;
			}
			if (o1.percentIdentity > o2.percentIdentity) {
				return 1;
			}

			return 0;
		}

	};

	/**
	 * Vraca listu sa prvih X u listi.
	 * 
	 * @param <T>
	 * @param lists
	 * @param firstBest
	 * @return
	 */
	public static <T> List<T> getFirstBestElements(List<T> lists, int firstBest) {
		ArrayList<T> result = new ArrayList<T>(firstBest);
		int count = 1;
		for (T t : lists) {

			if (count == firstBest) {
				break;
			}
			result.add(t);

			count++;
		}
		return result;
	}

	/**
	 * Poziva za svaki future get(), rezultat ignorira.
	 * 
	 * @param futures
	 */
	public static void waitAllFutures(ArrayList<Future<String>> futures) {
		for (Future<?> f : futures) {
			try {
				f.get();
			} catch (Throwable e) {
				log.error("", e);
			}
		}
	}

	public static void close(Connection c) {
		if (c != null) {
			try {
				c.close();
			} catch (SQLException e) {
				log.error("", e);
			}
		}

	}

}
