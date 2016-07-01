package hr.semgen.masena.share.util;

import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.PeakFile;
import hr.semgen.masena.share.model.PeakLite;
import hr.semgen.masena.share.model.Peptid;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sharani peptide utils. Koristi compomics.
 * 
 * @author ja
 * 
 */
public class PeptideUtils {


	public static void main22(String[] args) {
		String s = "AIVGMTGXGVNDAPXLKK";
		System.out.println("Sadrzi:  " + StringUtils.countMatches(s, "X"));
		final Map<String, Float> m = PeptideUtils.massesPeptideWithTwoX(s);
		final Set<String> keySet = m.keySet();
		for (String p : keySet) {
			System.out.println(p + " " + m.get(p));
		}

	}

	

	/**
	 * Optimizirano! Treba jos dodati na vracenu vrjednost masu protona 1.0072.
	 * Racuna potencijalne mase peptida, obzirom na Z i B i X. Vazno. X se
	 * nesmje unjeti
	 * 
	 * @param peptid
	 *            velikim slovima mora biti
	 * @return mapu <Peptide,Mass>
	 */
	public final static Map<String, Float> massesPeptideWithTwoX(String peptid) {
		final int numX = StringUtils.countMatches(peptid, "X");
		if (numX > 2) {
			throw new RuntimeException("peptide contain " + numX + " X: " + peptid);
		}

		// B => D i N
		// peptide => MASS
		Map<String, Float> result = new HashMap<String, Float>();

		HashSet<String> peptides = new HashSet<String>();
		getCombinationStringWithX_B_Z(peptid, peptides);

		for (String pep : peptides) {
			// AASequenceImpl seq = new AASequenceImpl(pep);

			// JPLPeptide peptide = new JPLPeptide.Builder(pep).build();
			// result.add(monoMassCalc.getMass(peptide));
			// result.put(pep, seq.getMass());

			result.put(pep, Peptid.calculateMassWidthH2O(pep));
		}

		if (peptides.isEmpty()) { // normalan peptid
			// AASequenceImpl seq = new AASequenceImpl(peptid);
			// result.put(peptid, seq.getMass());
			result.put(peptid, Peptid.calculateMassWidthH2O(peptid));
			// JPLPeptide peptide = new JPLPeptide.Builder(peptid).build();
			// result.add(monoMassCalc.getMass(peptide));
		}

		return result;

	}

	/**
	 * Treba jos dodati na vracenu vrjednost masu protona 1.0072. Racuna
	 * potencijalne mase peptida, obzirom na Z i B i X. Vazno. X se nesmje
	 * unjeti
	 * 
	 * @param peptid
	 *            velikim slovima mora biti
	 * @return mapu <Peptide,Mass>
	 */
	// public static Map<String, Float> massesPeptide(String peptid) {
	// if (peptid.contains("X")) {
	//
	// throw new RuntimeException("peptide contain X: " + peptid);
	// }
	//
	// // B => D i N
	// // peptide => MASS
	// Map<String, Float> result = new HashMap<String, Float>();
	//
	// HashSet<String> peptides = new HashSet<String>();
	// getCombinationString(peptid, peptides);
	//
	// for (String pep : peptides) {
	// AASequenceImpl seq = new AASequenceImpl(pep);
	//
	// // JPLPeptide peptide = new JPLPeptide.Builder(pep).build();
	// // result.add(monoMassCalc.getMass(peptide));
	// result.put(pep, (float) seq.getMass());
	// }
	//
	// if (peptides.isEmpty()) { // normalan peptid
	// AASequenceImpl seq = new AASequenceImpl(peptid);
	// result.put(peptid, (float) seq.getMass());
	// // JPLPeptide peptide = new JPLPeptide.Builder(peptid).build();
	// // result.add(monoMassCalc.getMass(peptide));
	// }
	//
	// return result;
	//
	// }

	/**
	 * BUG:
	 * 
	 * @param prot
	 * @param peptides
	 */
	public static void getCombinationStringWithX_B_Z(String prot, HashSet<String> peptides) {
		if (prot.contains("X")) {

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "G"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "A"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "S"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "P"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "V"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "T"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "C"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "I"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "L"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "N"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "D"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "Q"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "K"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "E"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "M"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "H"), peptides);

			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "F"), peptides);
			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "R"), peptides);
			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "Y"), peptides);
			getCombinationStringWithX_B_Z(prot.replaceFirst("X", "W"), peptides);
			return;
		}

		if (prot.contains("B")) {
			String res1 = prot.replaceFirst("B", "D");
			String res2 = prot.replaceFirst("B", "N");
			getCombinationStringWithX_B_Z(res1, peptides);
			getCombinationStringWithX_B_Z(res2, peptides);

			return;
		}
		if (prot.contains("Z")) {
			String res1 = prot.replaceFirst("Z", "Q");
			String res2 = prot.replaceFirst("Z", "E");

			getCombinationStringWithX_B_Z(res1, peptides);
			getCombinationStringWithX_B_Z(res2, peptides);
			return;
		}
		peptides.add(prot);
	}

	private static final Logger log = LoggerFactory.getLogger(PeptideUtils.class);

	/**
	 * Racuna relativ H od jedne cepanice na jednome MSMS spektru. U negativu
	 * ako je zadnji desni dummy (precusor) onda uzima prethodni H. Sortirani
	 * ioni i neprekinuti niz se gleda samo!
	 * 
	 * 
	 * @param msms
	 * @param ions
	 * @param peptide
	 * @return relativ H / total H (zaokruzuje na 4 dec)
	 */
	public static final float realtiveHeightIntensity(PeakFile msms, Collection<Ion> ions) {
		final float totalHeight = msms.calculateTotalHeight();

		float maxHunizu = 0;
		float prethodniHeight = 0;

		float maxUnizuHcurrent = 0;
		float maxUnizuHlast = 0;

		Peak dummyNeg = null; // masa prekursora
		// Peak dummyPos = null; // 0

		final List<Peak> allPeaks = msms.getAllPeaks();

		// odozada jer je vjerojatno sortirano i dumm je zadnji
		for (int i = allPeaks.size() - 1; i >= 0; i--) {
			final Peak peak = allPeaks.get(i);
			if (peak.isDummy() && msms.isNegative()) {
				dummyNeg = peak;
				break;
			}
			// if(peak.isDummy() && msms.isPositive()) {
			// dummyPos = peak;
			// break;
			// }
		}

		for (Ion ion : ions) {

			// samo u nizu treba biti
			if (ion.isMatch()) {

				if (msms.isNegative()) { // jeli NEGATIVE
					PeakLite pl = ion.getClosestPeakLite();
					if(dummyNeg == null) {
						log.error("Null "+ msms.getName() + " "+ ions);
					}
					if(pl == null) {
						log.error("Null "+ msms + " "+ ion +"  Ions: "+ ions);
					}
					final float dummyCentroidMass = dummyNeg.getCentroidMass();
					if (pl.getCentroidMass() == dummyCentroidMass) {
						maxUnizuHcurrent += prethodniHeight;
						prethodniHeight = ion.getClosestPeakLite().getHeight();
						continue;
					}
				} else { // pozitiv ima 0 dummy pik, ali gleda samo desni pik
//					PeakLite pl = ion.getClosestPeakLite();
//					if (pl.getCentroidMass() == 0) {
//						maxUnizuHcurrent += prethodniHeight;
//						prethodniHeight = ion.getClosestPeakLite().getHeight();
//						continue;
//					}
				}
//				log.debug("Pik {}", ion.getClosestPeakLite().getHeight());
				maxUnizuHcurrent += ion.getClosestPeak().getHeight();
				prethodniHeight = ion.getClosestPeakLite().getHeight();

			} else { // prekinuo niz

				// Prekinuo niz
				maxUnizuHlast = Math.max(maxUnizuHlast, maxUnizuHcurrent);
				maxUnizuHcurrent = 0;
			}
		}
		maxHunizu = Math.max(maxUnizuHlast, maxUnizuHcurrent);

		return CommonUtils.roundToDecimals(maxHunizu / totalHeight, 4);

	}
}
