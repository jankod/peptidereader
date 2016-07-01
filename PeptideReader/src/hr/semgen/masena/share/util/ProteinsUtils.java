package hr.semgen.masena.share.util;

import hr.semgen.masena.share.model.NadjenaAA;
import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.Peptid;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProteinsUtils {
	private static final Logger log = LoggerFactory.getLogger(ProteinsUtils.class);

	private Map<String, Float> proteinMonoisotopicMass = new HashMap<String, Float>();

	private ProteinsUtils() throws IOException {
		loadMasses();
	}

	private static ProteinsUtils instance;

	public static final ProteinsUtils get() {
		if (instance == null) {
			try {
				instance = new ProteinsUtils();
			} catch (IOException e) {
				log.error("", e);
			}
		}
		return instance;
	}

	public Map<String, Float> getProteinMonoisotopicMass() {
		return proteinMonoisotopicMass;
	}

	private void loadMasses() throws IOException {
		Properties p = new Properties();
		InputStream res = getClass().getResourceAsStream("/mass.properties");
		p.load(res);

		Set<Object> keySet = p.keySet();
		for (Object object : keySet) {
			String aa = (String) object;
			String aaMass = p.getProperty(aa);
			proteinMonoisotopicMass.put(aa, Float.valueOf(aaMass));
			// log.debug("aa " + aa + " " + aaMass + " " + l);

		}
		// log.debug("Sadrzi: " + proteinMonoisotopicMass.size());
	}

	public final double getMassFromAAfast(char aa) {
		switch (aa) {
		case 'G':
			return 57.021463724D;
			// return 57;
		case 'A':
			return 71.03711379D;
		case 'S':
			return 87.03202841D;
		case 'P':
			return 97.05276385D;
		case 'V':
			return 99.06841392D;
		case 'T':
			return 101.04767847D;
		case 'C':
			return 103.00918448D;
		case 'I':
			return 113.08406398D;
		case 'L':
			return 113.08406398D;
		case 'N':
			return 114.04292745D;
		case 'D':
			return 115.02694303D;
		case 'Q':
			return 128.05857751D;
		case 'K':
			return 128.09496302D;
		case 'E':
			return 129.0425931D;
		case 'M':
			return 131.04048461D;
		case 'H':
			return 137.05891186D;
		case 'F':
			return 147.0684139162D;
		case 'R':
			return 156.10111103D;
		case 'Y':
			return 163.06332854D;
		case 'W':
			return 186.07931295D;
		case 'U':
			return 150.95363559D;
		case 'O':
			return 114.0793129535D;
		case 'J':
			return 113.08406398D;
		default:
			log.error("Koji je ovo aa: '{}'", aa);
			throw new RuntimeException("wrong aa "+ aa);
//			return '?';
		}
	}

	public final double getMassFromAA(char aa) {
		return getMassFromAAfast(aa);
		// return getMassFromAA(String.valueOf(aa));
	}

	public final float getMassFromAA(String aa) {
		final Float mass = proteinMonoisotopicMass.get(aa);
		if (mass == null) {
			log.warn("Not find mass for {}", aa);
			return 0;
		}
		return mass;
	}

	/**
	 * Detektira jeli ovaj pik u (molekula od cindrica). Sama masa molekule u je
	 * 247.9445. Kasnije tek, kad idemo snimati peptide u masenom spektrometru,
	 * dolazi do ionizacije, molekula prima proton kad se ionizira pa se masa
	 * uvecava za 1.0072. (247.9445 + 1.0072 = 248.9515 Da).
	 * 
	 * @param peak
	 * @return
	 */
	public boolean isU_Molecule(Peak peak) {
		if (peak.getCentroidMass() - 0.5 < 248.9515 && 248.9515 < peak.getCentroidMass() + 0.5) {
			return true;
		}
		return false;
	}

	public static enum SORT {
		MANJI_PREMA_VECEMU(1), VECI_PREMA_MANJEMU(-1);

		private int compareResult;

		private SORT(int i) {
			compareResult = i;
		}

		public int getCompareResult() {
			return compareResult;
		}
	}

	/**
	 * sort centroid mass, od manje mase prema vecoj
	 * 
	 * @param peaks
	 */
	public void sortByMass(List<Peak> peaks) {
		sortByMass(peaks, SORT.MANJI_PREMA_VECEMU);
	}

	private static final Comparator<Peak> comparator1 = new Comparator<Peak>() {

		public int compare(Peak o1, Peak o2) {
			if (o1.getCentroidMass() < o2.getCentroidMass()) {
				return -1;
			} else if (o1.getCentroidMass() > o2.getCentroidMass()) {
				return 1;
			} else {
				return 0;
			}
		}
	};

	private static final Comparator<Peak> comparator2 = new Comparator<Peak>() {

		public int compare(Peak o1, Peak o2) {
			if (o1.getCentroidMass() < o2.getCentroidMass()) {
				return 1;
			} else if (o1.getCentroidMass() > o2.getCentroidMass()) {
				return -1;
			} else {
				return 0;
			}
		}
	};

	/**
	 * sort centroid mass
	 */
	public void sortByMass(List<Peak> peaks, final SORT sort) {
		// sort centroid mass
		if (SORT.MANJI_PREMA_VECEMU == sort) {
			Collections.sort(peaks, comparator1);
		} else {
			Collections.sort(peaks, comparator2);
		}
	}

	/**
	 * sort po height od manje prema vecemu
	 * 
	 * @param peaks
	 */
	public void sortByHeight(List<Peak> peaks) {
		sortByHeight(peaks, SORT.MANJI_PREMA_VECEMU);
	}

	public static class ComparatorNadjenihHeightPeak implements Comparator<NadjenaAA> {

		private final SORT sort;

		public ComparatorNadjenihHeightPeak(SORT sort) {
			this.sort = sort;
		}

		public int compare(NadjenaAA o1, NadjenaAA o2) {
			int o1max = o1.peak1.getHeight() + o1.peak2.getHeight();
			int o2max = o2.peak1.getHeight() + o2.peak2.getHeight();
			if (sort == SORT.VECI_PREMA_MANJEMU) {
				return -Integer.valueOf(o1max).compareTo(Integer.valueOf(o2max));
			} else {
				return Integer.valueOf(o1max).compareTo(Integer.valueOf(o2max));
			}
		}

	}

	/**
	 * Zbraja height.
	 * 
	 * @param nadjene
	 * @param sort
	 */
	public void sortNadjeneByHeight(List<NadjenaAA> nadjene, final SORT sort) {
		ComparatorNadjenihHeightPeak comparatorNadjenih = new ComparatorNadjenihHeightPeak(sort);
		Collections.sort(nadjene, comparatorNadjenih);
	}

	/**
	 * sort by height
	 * 
	 * @param peaks
	 */
	public void sortByHeight(List<Peak> peaks, final SORT sort) {
		// sort by height
		Collections.sort(peaks, new Comparator<Peak>() {

			public int compare(Peak o1, Peak o2) {
				if (o1.getHeight() < o2.getHeight()) {
					return -sort.getCompareResult();
				} else if (o1.getHeight() > o2.getHeight()) {
					return sort.getCompareResult();
				} else {
					return 0;
				}
			}
		});
	}

	/**
	 * Gleda dali je valueCentar +- udaljeno od valueLeftRight
	 * 
	 * @param valueLeftRight
	 * @param valueCenter
	 * @param valuePlusMinus
	 * @return
	 */
	public final boolean beatween(double valueLeftRight, double valueCenter, double valuePlusMinus) {
		if (((valueLeftRight - valuePlusMinus) < valueCenter) && (valueCenter < (valueLeftRight + valuePlusMinus))) {
			return true;
		}
		return false;
	}

	/**
	 * DA i AD je prije W, EG i GE je prije W, VS i SV zanemaruje.
	 * 
	 * Mario: razmislih, AD i GE i DA i EG su 99 % kombinacija,tako da vs mozemo
	 * zanemariti,specijalno sto s sam ima 2 kombinacije.ako nadje i ge i ad,a i
	 * w onda ni sam ne znam sto bi na ruke ali algoritam ce dati sve
	 * kombinacije izlistane pa ce pobijediti intenzivnija (ge,eg,ad ili da),a w
	 * postoji samo ako niti jedna komb ne odgovara osim cistog w (dakle nema ni
	 * ad ni da ni ge i eg)
	 * 
	 * @return ako nadje W listu novi kombinacija pekova koji umjesto W mogu
	 *         imati AD, EG..., ako ne nadje W onda originalni peptid vraca u
	 *         rezultatu.
	 */
	public List<Peptid> checkAndReplaceTwoAA(Peptid pepOrig, List<Peak> peaks, double deltaPeakAa) {
		List<Peptid> resultPeptides = new ArrayList<Peptid>();

		List<NadjenaAA> asList = pepOrig.getAsList();
		int count = 0;
		for (NadjenaAA aaW : asList) {
			if (aaW.aa.equalsIgnoreCase("W")) {
				List<Peak> gamePeaks = getPeaksBeatween(aaW.peak1, aaW.peak2, peaks);

				// ========== D A ===============
				String firstAA = "D";
				String secondAA = "A";
				if (changeTwoAA(pepOrig, resultPeptides, asList, count, aaW, gamePeaks, firstAA, secondAA, deltaPeakAa)) {
				}

				firstAA = "A";
				secondAA = "D";
				if (changeTwoAA(pepOrig, resultPeptides, asList, count, aaW, gamePeaks, firstAA, secondAA, deltaPeakAa)) {
				}

				firstAA = "E";
				secondAA = "G";
				if (changeTwoAA(pepOrig, resultPeptides, asList, count, aaW, gamePeaks, firstAA, secondAA, deltaPeakAa)) {
				}

				firstAA = "G";
				secondAA = "E";
				if (changeTwoAA(pepOrig, resultPeptides, asList, count, aaW, gamePeaks, firstAA, secondAA, deltaPeakAa)) {
				}

			}

			count++;
		}
		resultPeptides.add(pepOrig);

		return resultPeptides;

	}

	/**
	 * Zamjenjuje two AA
	 * 
	 * @param pepOrig
	 * @param resultPeptides
	 * @param asList
	 * @param count
	 * @param aaW
	 * @param gamePeaks
	 * @param firstAA
	 * @param secondAA
	 * @return vrca true ako je zamjenio
	 */
	private boolean changeTwoAA(Peptid pepOrig, List<Peptid> resultPeptides, List<NadjenaAA> asList, int count,
			NadjenaAA aaW, List<Peak> gamePeaks, String firstAA, String secondAA, double deltaPeakAa) {
		Peak centerPeak = existPeak(ProteinCalculation.getMass(firstAA) + aaW.peak1.getCentroidMass(), gamePeaks,
				deltaPeakAa);
		if (centerPeak != null) {
			// znaci postoji, to je desni D pik
			NadjenaAA aminoAcidD = new NadjenaAA(aaW.peak1, centerPeak, firstAA);
			NadjenaAA aminoAcidA = new NadjenaAA(centerPeak, aaW.peak2, secondAA);
			Peptid copyPeptide = new Peptid(pepOrig);
			copyPeptide.clearAllaa();
			copyPeptide.addAll(asList.subList(0, count));
			copyPeptide.addNew(aminoAcidD);
			copyPeptide.addNew(aminoAcidA);

			if (count + 1 != asList.size()) { // ako nije zadnja u nizu W
				copyPeptide.addAll(asList.subList(count + 1, asList.size()));
			}
			resultPeptides.add(copyPeptide);

			// log.debug("Nasao W i zamjenio sa {} i {}", firstAA, secondAA);
			return true;
		}
		return false;
	}

	/**
	 * Vraca peak ako postoji medju gamePeaks sa devijacojom.
	 * 
	 * @param string
	 * @param string2
	 * @param gamePeaks
	 * @return null ako ne nadje nis.
	 */
	private Peak existPeak(double mass, List<Peak> gamePeaks, double deltaPeakAa) {
		if (gamePeaks.size() < 3) {
			return null;
		}
		sortByMass(gamePeaks, SORT.MANJI_PREMA_VECEMU);

		double aa1RightPeakMass = mass;

		for (Peak peak : gamePeaks) {
			double m2 = peak.getCentroidMass();
			if (beatween(m2, aa1RightPeakMass, deltaPeakAa)) {
				return peak;
			}

		}
		return null;
	}

	/**
	 * Vraca pikove izmedju peak1 i peak2.
	 * 
	 * @param peak1
	 * @param peak2
	 * @param peaks
	 * @return
	 */
	private List<Peak> getPeaksBeatween(Peak peak1, Peak peak2, List<Peak> peaks) {
		Peak p1;
		Peak p2;
		if (Math.min(peak1.getCentroidMass(), peak2.getCentroidMass()) == peak1.getCentroidMass()) {
			p1 = peak1;
			p2 = peak2;
		} else {
			p1 = peak2;
			p2 = peak1;
		}

		ArrayList<Peak> gp = new ArrayList<Peak>(peaks);
		sortByMass(gp, SORT.MANJI_PREMA_VECEMU);
		ArrayList<Peak> result = new ArrayList<Peak>();
		for (Peak peak : gp) {
			if (peak.getCentroidMass() >= p1.getCentroidMass() && peak.getCentroidMass() <= p2.getCentroidMass()) {
				result.add(peak);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param peptide samo masa amino kiselina plus voda
	 * @return
	 */
	public double calcPeptideMassZeroo(String peptide) {
		double h = 0;
		for (int i = 0; i < peptide.length(); i++) {
			char c = peptide.charAt(i);
			double massFromAA = getMassFromAA(c);
		//	log.debug("char {} mass {}" , c, massFromAA);
			h += massFromAA;
		}
		// log.debug("To je to");
		// voda
		return h + ProteinConstants.H2O;
	}

}
