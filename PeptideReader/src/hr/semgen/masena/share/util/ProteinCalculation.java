package hr.semgen.masena.share.util;

import hr.semgen.masena.share.model.NadjenaAA;
import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.PeakFile;
import hr.semgen.masena.share.mph.MphConstants;
import hr.semgen.masena.share.util.ProteinsUtils.SORT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProteinCalculation {
	private static final Logger log = LoggerFactory.getLogger(ProteinCalculation.class);

	final static ProteinsUtils util = ProteinsUtils.get();

	/**
	 * plus minus aa calculation pref store za peak od AA
	 */
	// public static Float MASS_DEVIATION_AA = null; // 10e-2

	private static Map<String, Float> mass = util.getProteinMonoisotopicMass();

	/**
	 * Lista masa
	 * @return
	 */
	public static Map<String, Float> getMasses() {
		return mass;
	}
	
	/**
	 * Trazi S i T bez vode.
	 */
	// public static boolean SEARCH_S_T_BEZ_VODE = true;

	// public static boolean CALCULATE_AA_R_OR_K_WITH_H2O = true;

	public static boolean CALCULATE_AA_R_OR_K_WITHOUT_H2O = true;

	private LinkedList<NadjenaAA> proteiniNadjeni;

	private static Comparator<Peak> comparaPeakMass;

	public LinkedList<NadjenaAA> getProteiniNadjeni() {
		return proteiniNadjeni;
	}

	public static void addZerroPeakToPositive(Set<PeakFile> allMSPeakFiles) {
		for (PeakFile ms : allMSPeakFiles) {
			for (PeakFile msms : ms.getMsmsChilds()) {
				if (msms.isPositive()) {
					addDummyZerroPeakIfNotExist(msms.getAllPeaks());
				}
			}
		}
	}

	/**
	 * Racuna dali odgovara nekoj masi.
	 * 
	 * @param trenutni
	 * @param sljedeci
	 * @param massDeviation
	 *            bivsi {@link ProteinCalculation}.MASS_DEVIATION
	 * @param searchSorTwithoutH2O
	 *            bivsi {@link ProteinCalculation}.SEARCH_S_T_BEZ_VODE
	 * @return null ako ne nadje, inace aa
	 */
	public final static String odgovaraMasiNekoj(Peak trenutni, Peak sljedeci, double massDeviation,
			boolean searchSorTwithoutH2O) {

		
		
		Set<String> keySet = mass.keySet();

		double razlika = Math.abs(sljedeci.getCentroidMass() - trenutni.getCentroidMass());

		double razlikaPlus = razlika + massDeviation;
		double razlikaMinus = razlika - massDeviation;

		// log.debug("Mass deviaction: "+ MASS_DEVIATION);

		for (String protName : keySet) {
			float masa = mass.get(protName);
			if (razlikaMinus <= masa && masa <= razlikaPlus) {
				// Ovaj odgovara
				// log.debug("NASAO {}", aa);

				if (protName.equals("K")) {
					// nije K nego Q, K i Q imaju istu masu, ali je vrlo
					// vjerojatno Q jer iza K cjepa tripsin pa je on
					// samo na kraju osim ako nije miss cleavage

					return "Q";
				}
				if (protName.equals("L")) {
					// uvjek I od sada
					return "I";
				}
				return protName;
			}
		}

		if (searchSorTwithoutH2O) {
			// Masa S zna biti drugacja, jer OH skupina zna otpasti pa je bez
			// vode 69,02092 .
			// Isto tako T moze biti 83,03657 .
			double masa = 69.02092;
			if (razlikaMinus <= masa && masa <= razlikaPlus) {
				return "S";
			}
			masa = 83.03657;
			if (razlikaMinus <= masa && masa <= razlikaPlus) {
				return "T";
			}

		}

		return null;
	}

	/**
	 * Vraca masu na temelju a.a.
	 * 
	 * @param aaName
	 */
	public static double getMass(String aaName) {

		return mass.get(StringUtils.trim(aaName));
	}

	/**
	 * Remove peaks with mass bigger than precusor mass
	 * 
	 * @param peaks
	 * @param massPrecursor
	 */
	public static void removeBiggerThenMass(List<Peak> peaks, double massPrecursor) {
		List<Peak> forRemove = new ArrayList<Peak>();
		for (Peak peak : peaks) {
			if (peak.getCentroidMass() > (massPrecursor + 0.5)) {
				forRemove.add(peak);
			}
		}

		peaks.removeAll(forRemove);
	}

	/**
	 * Remove peaks with mass less then 248.9515 Da +- 0.5
	 * 
	 * @param peaks
	 * @return
	 */
	public static void removeLessThanMass248(List<Peak> peaks) {
		List<Peak> forRemove = new ArrayList<Peak>();
		for (Peak peak : peaks) {
			// TREBALO BI   if (peak.getCentroidMass() < (247.9445 - 0.5)) {   ???? 
			if (peak.getCentroidMass() < (248.9515 - 0.5)) {
				forRemove.add(peak);
			}
		}

		peaks.removeAll(forRemove);
	}

	/**
	 * Mice sve kojima je masa manja od <code>mass</code>.
	 * 
	 * @param peaks
	 * @param mass
	 */
	public static void removeLessThenMass(List<Peak> peaks, int mass) {
		List<Peak> forRemove = new ArrayList<Peak>();
		for (Peak peak : peaks) {
			if (peak.getCentroidMass() < mass) {
				forRemove.add(peak);
			}
		}

		peaks.removeAll(forRemove);
	}

	/**
	 * Trazi zadnju R ili K aa ako postoji i ako nadje vraca nadjeni prot.
	 * 
	 * Dakle zadnja amino kiselina mora biti ili K ili R, ako postoje obje
	 * mogucnosti (da mu odgovara i 146,10516 i 174,1131) on uvijek mora
	 * izabrati vecu vrijednost po intenzitetu pika.
	 * 
	 * @param peakFile
	 * @return pronadjeni K ili R, ili null ako nis ne nadje
	 */
	public static NadjenaAA findLastRorKaa(List<Peak> peaks, double deltaPlusMinusR_or_K,
			boolean /* ProteinCalculation.CALCULATE_AA_R_OR_K_WITH_H2O */CALCULATE_AA_R_OR_K_WITH_H2O,
			boolean CALCULATE_AA_R_OR_K_WITHOUT_H2O) {
		// double dev = Activator.getPreferencePlusMinusR_or_K();
		double dev = deltaPlusMinusR_or_K;
		if (peaks.isEmpty()) {
			return null;
		}
		if (comparaPeakMass == null)
			comparaPeakMass = new Comparator<Peak>() {
				public int compare(Peak o1, Peak o2) {
					return -Double.valueOf(o1.getCentroidMass()).compareTo(Double.valueOf(o2.getCentroidMass()));
				}
			};
		Collections.sort(peaks, comparaPeakMass);

		// lizin K 146,10516
		// arginina R 174,1131

		NadjenaAA resultWithH20 = null;
		if (CALCULATE_AA_R_OR_K_WITH_H2O) { // SA VODOM
			final double K = 146.10516;
			final double R = 174.1131;

			Peak zadnji = peaks.get(0);

			NadjenaAA nadProtK = null;
			NadjenaAA nadProtR = null;

			for (Peak peak : peaks) {
				double raz = zadnji.getCentroidMass() - peak.getCentroidMass();
				if (K - dev < raz && raz < K + dev) {

					nadProtK = new NadjenaAA(peak, zadnji, "K");

				}
				if (R - dev < raz && raz < R + dev) {
					nadProtR = new NadjenaAA(peak, zadnji, "R");
				}
			}

			if (nadProtK != null && nadProtR != null) {
				// znaci nasao je obje, vrati onu s vecim pikom

				if (nadProtK.peak1.getHeight() > nadProtR.peak1.getHeight()) {
					resultWithH20 = nadProtK;
				} else {
					resultWithH20 = nadProtR;
				}
			}

			if (nadProtK != null) {
				resultWithH20 = nadProtK;
			}

			if (nadProtR != null) {
				resultWithH20 = nadProtR;
			}

			// Ako nije nista nasao nega onda trazi BEZ vode
		}

		// Ako nije nista nasao nega onda trazi BEZ vode
		NadjenaAA resultWithOutH20 = null;
		if (CALCULATE_AA_R_OR_K_WITHOUT_H2O) { // BEZ VODE
			final double K = MphConstants.K;
			final double R = MphConstants.R;

			Peak zadnji = peaks.get(0);

			NadjenaAA nadProtK = null;
			NadjenaAA nadProtR = null;

			for (Peak peak : peaks) {
				double raz = zadnji.getCentroidMass() - peak.getCentroidMass();
				if (K - dev < raz && raz < K + dev) {

					nadProtK = new NadjenaAA();
					nadProtK.peak2 = zadnji;
					nadProtK.peak1 = peak;
					nadProtK.aa = "K";

				}
				if (R - dev < raz && raz < R + dev) {
					nadProtR = new NadjenaAA();
					nadProtR.peak2 = zadnji;
					nadProtR.peak1 = peak;
					nadProtR.aa = "R";
				}
			}

			if (nadProtK != null && nadProtR != null) {
				// znaci nasao je obje, vrati onu s vecim pikom

				if (nadProtK.peak1.getHeight() > nadProtR.peak1.getHeight()) {
					resultWithOutH20 = nadProtK;
				} else {
					resultWithOutH20 = nadProtR;
				}
			}

			if (nadProtK != null) {
				resultWithOutH20 = nadProtK;
			}

			if (nadProtR != null) {
				resultWithOutH20 = nadProtR;
			}

		}

		if (resultWithH20 == null && resultWithOutH20 != null) {
			// log.debug("Nasao bez H20 {}", resultWithOutH20);
			return resultWithOutH20;
		}

		if (resultWithH20 != null && resultWithOutH20 == null) {
			// log.debug("Nasao sa H20 {}", resultWithH20);
			return resultWithH20;
		}
		if (resultWithH20 == null && resultWithOutH20 == null) {
			return null;
		}
		// Ako ni jedno nije null, vrati vislji pik
		int h1 = resultWithH20.peak1.getHeight() + resultWithH20.peak2.getHeight();
		int h2 = resultWithOutH20.peak1.getHeight() + resultWithOutH20.peak2.getHeight();

		// log.debug("Nasao oboje sa {} bez {}",
		// resultWithH20.peak1.getHeight(), resultWithOutH20.peak1.getHeight());
		if (h1 > h2) {
			return resultWithH20;
		} else {
			return resultWithOutH20;
		}

	}

	public static int findMaxHeight(List<Peak> allPeaks) {
		int max = 0;
		for (Peak peak : allPeaks) {
			max = Math.max(max, peak.getHeight());
		}
		return max;
	}

	public static void addDummyPrecursorPeak(float mass, List<Peak> peaks) {
		Peak peakU = new Peak(mass, ProteinCalculation.findMaxHeight(peaks));
		peakU.setDummy(true);
		peaks.add(peakU);
	}

	public static void addDummyZerroPeakIfNotExist(List<Peak> peaks) {
		if(peaks == null || peaks.isEmpty()) {
			return;
		}
		ProteinsUtils.get().sortByMass(peaks);
		
		if (peaks.get(0).getCentroidMass() != 0) {
			Peak peakDummy = new Peak(0, ProteinCalculation.findMaxHeight(peaks));
			peakDummy.setDummy(true);
			peaks.add(peakDummy);
//			log.debug("Dodajem {}", peaks.get(0));
		}
	}

	/**
	 * Gleda dali MS peak sadrzi u sebi masu mass sa devijacijom dev.
	 * 
	 * @param mass
	 * @param dev
	 * @param peakMS
	 * @return
	 */
	public static boolean containMass(Double mass, Double dev, List<Peak> peaks) {
		for (Peak peak : peaks) {
			double pm = peak.getCentroidMass();

			boolean beatween = util.beatween(pm, mass, dev);
			if (beatween) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gleda dali MS peak sadrzi u sebi masu mass sa devijacijom dev.
	 * 
	 * @param mass
	 * @param dev
	 * @param peakMS
	 * @return
	 */
	public static boolean containMass(Double mass, Double dev, PeakFile peakMS) {
		List<Peak> peaks = peakMS.getAllPeaks();
		return containMass(mass, dev, peaks);
	}

	/**
	 * Mice ljevo i desno od mase prekusroa sve pikove za howMuchToRemove
	 * 
	 * @param howMuchToRemove
	 * @param allPeaks
	 * @param massPrecursor
	 */
	public static void removeLeftRightFromPrecursorMass(float howMuchToRemove, List<Peak> allPeaks, Float massPrecursor) {
		List<Peak> forRemove = new ArrayList<Peak>();
		for (Peak peak : allPeaks) {
			double m = peak.getCentroidMass();

			double left = massPrecursor - howMuchToRemove;
			double right = massPrecursor + howMuchToRemove;
			if (left < m && m < right && m != massPrecursor) {
				forRemove.add(peak);
				// log.debug("Micem: " + peak.getCentroidMass());
			}
		}
		allPeaks.removeAll(forRemove);
	}

	public static void ostaviNajintenzivnjePikove(int prvihKoliko, List<Peak> peaks) {
		ProteinsUtils.get().sortByHeight(peaks, SORT.VECI_PREMA_MANJEMU);
		int count = 1;
		final Iterator<Peak> it = peaks.iterator();
		while (it.hasNext()) {
			it.next();
			if (count++ > prvihKoliko) {
				it.remove();
			}
		}
	}

}
