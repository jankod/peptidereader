package hr.semgen.masena.share.util;

import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.PeakFile;
import hr.semgen.masena.share.model.Peptid;
import hr.semgen.masena.share.util.Ion.MatchType;
import hr.semgen.masena.share.util.Ion.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BionUtils {
	private static final Logger log = LoggerFactory.getLogger(BionUtils.class);

	public static void main1(String[] args) {
		String p = "MADADDSLALRAAWLHFVAGMTQSAVAKRLGLPSVKAHRLIAKAVADGAVKVTIDGDITECIDLENRLADMADADDSLALRAAWLHFVAGMTQSAVAKRLGLPSVKAHRLIAKAVADGAVKVTIDGDITECIDLENRLADMADADDSLALRAAWLHFVAGMTQSAVAKRLGLPSVKAHRLIAKAVADGAVKVTIDGDITECIDLENRLADMADADDSLALRAAWLHFVAGMTQSAVAKRLGLPSVKAHRLIAKAVADGAVKVTIDGDITECIDLENRLADMADADDSLALRAAWLHFVAGMTQSAVAKRLGLPSVKAHRLIAKAVADGAVKVTIDGDITECIDLENRLADMADADDSLALRAAWLHFVAGMTQSAVAKRLGLPSVKAHRLIAKAVADGAVKVTIDGDITECIDLENRLADMADADDSLALRAAWLHFVAGMTQSAVAKRLGLPSVKAHRLIAKAVADGAVKVTIDGDITECIDLENRLADMADADDSLALRAAWLHFVAGMTQSAVAKRLGLPSVKAHRLIAKAVADGAVKVTIDGDITECIDLENRLAD";
		// p = "SAMPLER";
		SpringStopWatch s = new SpringStopWatch();
		s.start("moje");
		final SortedSet<Ion> bions = getYIonsFAST(p);
		s.stop();
		s.start("staro");
		getBions(p, Type.Y);
		s.stop();
		for (Ion ion : bions) {
			System.out.println(ion);
		}
		System.out.println(s.prettyPrint());
	}

	public static void main(String[] args) {
		String p = "LDTSDSSSDAE";
		final TreeSet<Ion> ions = getYIonsFAST(p);

		for (Ion ion : ions) {
			System.out.println(ion);
		}
		System.out.println();
		final ArrayList<Ion> ion2 = getBionsFAST(p, Type.Y);
		for (Ion ion : ion2) {
			System.out.println(ion);
		}
	}

	public static TreeSet<Ion> getYIonsFAST(String peptide) {
		if (peptide.length() == 0) {
			return new TreeSet<Ion>();
		}
		TreeSet<Ion> yIons = new TreeSet<Ion>();

		final double massTotal = Peptid.calculateMass(peptide) + 1.007825 + ProteinConstants.H2O;

		char lastAA = peptide.charAt(0);
		Ion prvi = new Ion(lastAA, massTotal, 1);
		yIons.add(prvi);
		for (int i = 1; i < peptide.length(); i++) {
			char currentAA = peptide.charAt(i);
			Ion ion = new Ion(currentAA, prvi.getMass() - proteinsUtils.getMassFromAA(lastAA), i + 1);
			yIons.add(ion);
			prvi = ion;
			lastAA = currentAA;
		}
		// yIons.add(prvi);
		return yIons;
	}

	/**
	 * Razlika je sto ovo vraca arraylist.
	 * 
	 * @param peptide
	 * @param type
	 * @return
	 */
	public static final ArrayList<Ion> getBionsFAST(String peptide, Type type) {
		if (type == Type.B) {
			ArrayList<Ion> bIons = new ArrayList<Ion>(peptide.length());
			for (int i = 0; i < peptide.length(); i++) {
				bIons.add(null);
			}
		
			// dodaj H+, to je Bn
			final double massTotal = Peptid.calculateMass(peptide) + ProteinConstants.Hplus;

			char aaLast = (peptide.charAt(peptide.length() - 1));
			Ion bZadnji = new Ion(aaLast, massTotal, peptide.length());
			// log.debug("Zadnji je: " + bZadnji);

			double massLast = massTotal;
			for (int i = peptide.length() - 2; i >= 0; i--) {

				final char predzadnji = peptide.charAt(i);
				final double mass = proteinsUtils.getMassFromAA(aaLast);
				Ion ion = new Ion(predzadnji, massLast = massLast - mass, i + 1);
				bIons.set(i, ion);
				// log.debug("" + ion);
				aaLast = predzadnji;
			}
			bIons.set(peptide.length() - 1, bZadnji);
			return bIons;

		} else { // Y

			if (peptide.length() == 0) {
				return new ArrayList<Ion>();
			}
			ArrayList<Ion> yIons = new ArrayList<Ion>(peptide.length());

			final double massTotal = Peptid.calculateMass(peptide) + ProteinConstants.Hplus + ProteinConstants.H2O;

			char lastAA = peptide.charAt(0);
			Ion prvi = new Ion(lastAA, massTotal, 1);
			yIons.add(prvi);
			for (int i = 1; i < peptide.length(); i++) {
				char currentAA = peptide.charAt(i);
				Ion ion = new Ion(currentAA, prvi.getMass() - proteinsUtils.getMassFromAA(lastAA), i + 1);
				yIons.add(ion);
				prvi = ion;
				lastAA = currentAA;
			}
			// yIons.add(prvi);

			return yIons;

		}
	}

	public static TreeSet<Ion> getBIonsFAST(String peptide) {
		TreeSet<Ion> bIons = new TreeSet<Ion>();

		// dodaj H+, to je Bn
		final double massTotal = Peptid.calculateMass(peptide) + 1.007825;

		char aaLast = (peptide.charAt(peptide.length() - 1));
		Ion bZadnji = new Ion(aaLast, massTotal, peptide.length());
		bIons.add(bZadnji);
		// log.debug("Zadnji je: " + bZadnji);

		double massLast = massTotal;
		for (int i = peptide.length() - 2; i >= 0; i--) {

			final char predzadnji = peptide.charAt(i);
			final double mass = ProteinsUtils.get().getMassFromAA(aaLast);
			Ion ion = new Ion(predzadnji, massLast = massLast - mass, i + 1);
			bIons.add(ion);
			// log.debug("" + ion);
			aaLast = predzadnji;
		}

		return bIons;

	}

	/**
	 * Vraca postotak matchanja MSMS-a i peptida.
	 * 
	 * @param peptide
	 * @param msms
	 * @return
	 */
	public static double matchMSMS(String peptide, PeakFile msms, double plusMinus, Type type) {
		// log.debug("peptide {}", peptide);
		if (msms.isMS()) {
			throw new RuntimeException("This is not MSMS");
		}
		if (peptide.length() == 0) {
			return 0;
		}

		double countExistBion = 0;
		List<Peak> allPeaks = msms.getAllPeaks();
		SortedSet<Ion> bions = getBions(peptide, type);
		for (Ion bion : bions) {
			if (existBion(bion, allPeaks, plusMinus, type)) {
				countExistBion++;
			}
		}
		if (countExistBion == 0) {
			return 0;
		}
		// log.debug(peptide + " " + peptide.length() + " " + countExistBion);
		return (double) Math.round((countExistBion / peptide.length() * 100) * 100) / 100;

	}

	private static final ProteinsUtils proteinsUtils = ProteinsUtils.get();

	/**
	 * Nova metoda za exist ion-e
	 * 
	 * @param ion
	 * @param allPeaks
	 * @param plusMinus
	 * @param type
	 * @param ionsSize
	 * @param massPrecursor
	 * @return
	 */
	public static final boolean existIonFixed(Ion ion, Peak[] allPeaks, double plusMinus, Type type, int ionsSize,
			Float massPrecursor) {
		int maxH = -111;

		double ionMass = ion.getMass(); // Y
		if (type == Type.B) {
			ionMass = ion.getMassPlusCAF(); // B
		}

		// =====================================================================================
		// ============================K_i_R_se_matchaju_+-_H20==================================
		// Ali samo ako je R ili K terminalni, i sa 0.8 deltom
		// =====================================================================================

		{
			maxH = -111;
			MatchType rkMatchType = null;
			// mora se fitati sama ovog iona sa masom prekursora
			if ((ionsSize == ion.getPosition()) && (ion.getAA() == 'R' || ion.getAA() == 'K')) {

				// prvo normal match,
				final double l = ionMass - ProteinConstants.DELTA_K_OR_R;
				final double r = ionMass + ProteinConstants.DELTA_K_OR_R;
				if (l < massPrecursor && r > massPrecursor) {
					rkMatchType = MatchType.K_OR_R_PLUS_H2O;
					ion.setMatch(true);

				} else {

					final double lh = l + ProteinConstants.H2O;
					final double rh = r + ProteinConstants.H2O;
					if (lh < massPrecursor && rh > massPrecursor) {
						rkMatchType = MatchType.K_OR_R_PLUS_H2O;
						ion.setMatch(true);
					} else {
						double l_h = l - ProteinConstants.H2O;
						double r_h = r - ProteinConstants.H2O;
						if (l_h < massPrecursor && r_h > massPrecursor) {
							rkMatchType = MatchType.K_OR_R_MINUS_H2O;
							ion.setMatch(true);

						}

					}
				}
				ion.setMatchType(rkMatchType);
			}
		}

		if (ion.isMatch()) {
			// moram postaviti masu prekursora za najblizi pik
			setClosestPeak(ion, allPeaks, massPrecursor - 0.1, massPrecursor + 0.1);

			return true;
		}
		maxH = -111;
		// =====================================================================================
		// ============================NORMAL_MATCH==============================================
		// =====================================================================================
		Peak[] range;
		final double leftMass = ionMass - plusMinus;
		final double rightMass = ionMass + plusMinus;
		range = PeakAlgorithms.getRange(allPeaks, leftMass, rightMass);
		for (Peak peak : range) {
			if (proteinsUtils.beatween(ionMass, peak.getCentroidMass(), plusMinus)) {
				ion.setMatch(true);
				ion.setMatchType(MatchType.NORMAL_MATCH);

				if (peak.getHeight() > maxH) {
					maxH = peak.getHeight();
					ion.setClosestPeak(peak);
				}
			}
		}
		if (ion.isMatch()) {
			return true;
		}

		// =====================================================================================
		// ============================SEARCH_S_ILI_T_BEZ_VODE==================================
		// =====================================================================================
		maxH = -111;
		if (ion.getAA() == 'S' || ion.getAA() == 'T') {
			range = PeakAlgorithms
					.getRange(allPeaks, leftMass - ProteinConstants.H2O, rightMass - ProteinConstants.H2O);

			for (Peak peak : range) {

				ion.setMatch(true);
				ion.setMatchType(MatchType.S_OR_T_WITHOUT_H2O);

				if (peak.getHeight() > maxH) {
					maxH = peak.getHeight();
					ion.setClosestPeak(peak);
				}

			}
		}
		if (ion.isMatch()) {
			return true;
		}

		return false;

	}

	private static void setClosestPeak(Ion ion, Peak[] allPeaks, final double left, final double right) {
		int maxH = -1;
		Peak[] range = PeakAlgorithms.getRange(allPeaks, left, right);
		for (Peak peak : range) {
			if (peak.getHeight() > maxH) {
				maxH = peak.getHeight();
				ion.setClosestPeak(peak);
			}
		}
	}

	/**
	 * Vraca ljevi pik koji odgovara po masi sa plus minus.
	 * 
	 * @param allPeaks
	 *            moraju biti sortirani!
	 * @param plusMinus
	 * @param fromPeak
	 *            od kojeg pika strazimo
	 * @return index u nizu
	 */
	public static int getLeftPeakPoss(Peak[] allPeaks, double plusMinus, double fromPeak) {
		return binSearchLow(fromPeak, allPeaks, plusMinus);
	}

	public static int getRightPeakPoss(Peak[] allPeaks, double plusMinus, double fromPeak, int startFrom) {
		return binSearchHeight(fromPeak, allPeaks, plusMinus, startFrom);
	}

	/**
	 * Algoritam od ranka, C++ copy.
	 * 
	 * @param searchMassLow
	 *            masa koja se sercha
	 * @param peaks
	 *            pikovi
	 * @param plusMinus
	 *            delta
	 * @return
	 */
	public static int binSearchLow(double searchMassLow, Peak[] peaks, double plusMinus) {
		int begin = 0;
		int end = peaks.length;
		int mid = 0;

		searchMassLow -= plusMinus;
		while (end > begin) {
			// mid = (begin + end) / 2;
			mid = (begin + end) >>> 1;
			// Arrays.binarySearch(a, key)
			if (peaks[mid].getCentroidMass() < searchMassLow) {
				begin = mid + 1;
			} else {
				end = mid;
			}
		}

		return begin;
	}

	/**
	 * Serarcha za zajvecom vrjednoscu.
	 * 
	 * @param searchMassHeight
	 * @param peaks
	 * @param plusMinus
	 * @return
	 */
	public static int binSearchHeight(double searchMassHeight, Peak[] peaks, double plusMinus, int startFrom) {

		int end = peaks.length;
		int mid = 0;

		searchMassHeight += plusMinus;
		while (end > startFrom) {
			mid = (startFrom + end) / 2;
			if (peaks[mid].getCentroidMass() > searchMassHeight) {
				end = mid;
			} else {
				startFrom = mid + 1;
			}
		}

		return startFrom;
	}

	/**
	 * Ion.getPosition() mora pocinjati od 1.
	 * 
	 * @param ion
	 * @param allPeaks
	 * @param plusMinus
	 * @param type
	 * @param ionsSize
	 *            koliko iona ima
	 * @deprecated uzmi existBionFixed
	 */
	@Deprecated
	public static boolean existBion(Ion ion, Peak[] allPeaks, double plusMinus, Type type, int ionsSize) {
		Peak closestPeak = null;
		int maxH = 0;
		boolean existBion = false;
		final int length = allPeaks.length;

		double ionMass = ion.getMass(); // Y
		if (type == Type.B) {
			ionMass = ion.getMassPlusCAF(); // B
		}

		for (int i = 0; i < length; i++) {
			Peak peak = allPeaks[i];

			if (proteinsUtils.beatween(ionMass, peak.getCentroidMass(), plusMinus)) {
				existBion = true;

				if (peak.getHeight() > maxH) {
					maxH = peak.getHeight();
					closestPeak = peak;
				}
				ion.setMatch(true);
				ion.setMatchType(MatchType.NORMAL_MATCH);
			}

			// =====================================================================================
			// ============================K_i_R_se_matchaju_+_H20==================================
			// Ali samo ako je R ili K terminalni
			// =====================================================================================

			if (existBion == false && (ionsSize == ion.getPosition()) && (ion.getAA() == 'R' || ion.getAA() == 'K')) {
				if (proteinsUtils.beatween(ionMass + ProteinConstants.H2O, peak.getCentroidMass(), plusMinus)) {
					existBion = true;

					if (peak.getHeight() > maxH) {
						maxH = peak.getHeight();
						closestPeak = peak;
					}

					ion.setMatch(true);
					ion.setMatchType(MatchType.K_OR_R_PLUS_H2O);

				}
			}

			// =====================================================================================
			// ============================SEARCH_S_ILI_T_BEZ_VODE==================================
			// =====================================================================================

			if (existBion == false) {
				if (ion.getAA() == 'S') {
					if (proteinsUtils.beatween(ionMass - ProteinConstants.H2O, peak.getCentroidMass(), plusMinus)) {
						existBion = true;
						if (peak.getHeight() > maxH) {
							maxH = peak.getHeight();
							closestPeak = peak;

						}
						ion.setMatch(true);
						ion.setMatchType(MatchType.S_OR_T_WITHOUT_H2O);
					}
				}
				if (ion.getAA() == 'T') {
					if (proteinsUtils.beatween(ionMass - ProteinConstants.H2O, peak.getCentroidMass(), plusMinus)) {
						existBion = true;
						if (peak.getHeight() > maxH) {
							maxH = peak.getHeight();
							closestPeak = peak;
						}

						ion.setMatch(true);
						ion.setMatchType(MatchType.S_OR_T_WITHOUT_H2O);
					}
				}

			}
		}

		if (closestPeak == null) { // onda uzmi najblizi po masi pik

			double currentDelta = Double.MAX_VALUE;
			for (int i = 0; i < length; i++) {
				Peak peak = allPeaks[i];
				double delta;

				delta = Math.abs((peak.getCentroidMass() - ionMass));
				if (delta < currentDelta) {
					closestPeak = peak;
					currentDelta = delta;
				}
			}

		}

		ion.setClosestPeak(closestPeak);

		return existBion;
	}

	/**
	 * Vraca true ako posltoji b-ion u listi pikova Sortirani pikovi po masi
	 * ulaze. Postavlja i closest peak.
	 * 
	 * @param bion
	 * @param allPeaks
	 * @param plusMinus
	 * @param type
	 * @return
	 */
	public static boolean existBion(Ion ion, List<Peak> allPeaks, double plusMinus, Type type) {
		Peak closestPeak = null;
		int maxH = 0;
		boolean existBion = false;

		for (Peak peak : allPeaks) {

			if (type == Type.B) {
				if (proteinsUtils.beatween(ion.getMassPlusCAF(), peak.getCentroidMass(), plusMinus)) {
					existBion = true;

					if (peak.getHeight() > maxH) {
						maxH = peak.getHeight();
						closestPeak = peak;
					}
				}
			} else { // Y
				if (proteinsUtils.beatween(ion.getMass(), peak.getCentroidMass(), plusMinus)) {
					existBion = true;
					if (peak.getHeight() > maxH) {
						maxH = peak.getHeight();
						closestPeak = peak;
					}
				}
			}

			// =====================================================================================
			// ============================K_i_R_se_matchaju_+_H20==================================
			// =====================================================================================
			if (existBion == false && (ion.getAA() == 'R' || ion.getAA() == 'K')) {
				if (type == Type.B) {
					if (proteinsUtils.beatween(ion.getMassPlusCAF() + ProteinConstants.H2O, peak.getCentroidMass(),
							plusMinus)) {
						existBion = true;

						if (peak.getHeight() > maxH) {
							maxH = peak.getHeight();
							closestPeak = peak;
						}

					}
				} else { // Y
					if (proteinsUtils.beatween(ion.getMass() + ProteinConstants.H2O, peak.getCentroidMass(), plusMinus)) {
						existBion = true;

						if (peak.getHeight() > maxH) {
							maxH = peak.getHeight();
							closestPeak = peak;
						}
					}
				}
			}

			// =====================================================================================
			// ============================MATCH__1+-0.1==============================================
			// =====================================================================================
			if (existBion == false) {
				if (type == Type.B) {
					if (proteinsUtils.beatween(ion.getMassPlusCAF() + 1, peak.getCentroidMass(), 0.1)) {
						existBion = true;

						if (peak.getHeight() > maxH) {
							maxH = peak.getHeight();
							closestPeak = peak;
						}
					}
				} else { // Y
					if (proteinsUtils.beatween(ion.getMass() + 1, peak.getCentroidMass(), 0.1)) {
						existBion = true;

						if (peak.getHeight() > maxH) {
							maxH = peak.getHeight();
							closestPeak = peak;
						}
					}
				}
			}

		}

		if (closestPeak == null) { // onda uzmi najblizi po masi pik

			double currentDelta = Double.MAX_VALUE;
			for (Peak peak : allPeaks) {
				double delta;

				if (type == Type.B) {
					delta = Math.abs((peak.getCentroidMass() - ion.getMassPlusCAF()));
				} else { // Y
					delta = Math.abs((peak.getCentroidMass() - ion.getMass()));
				}
				if (delta < currentDelta) {
					closestPeak = peak;
					currentDelta = delta;
				}
			}

		}

		ion.setClosestPeak(closestPeak);

		return existBion;
	}

	/**
	 * Vraca B-ione koji sadrze match boolean varijablu koja govori dali metcha
	 * ili ne.
	 * 
	 * @param peptide
	 * @param peaks
	 * @param plusMinus
	 * @param type
	 * @return
	 */
	public static TreeSet<Ion> getBionsMatch(String peptide, List<Peak> peaks, double plusMinus, Type type) {
		ProteinsUtils.get().sortByMass(peaks);
		TreeSet<Ion> bions = getBions(peptide, type);
		for (Ion bion : bions) {
			bion.setMatch(existBion(bion, peaks, plusMinus, type));
		}

		return bions;
	}

	/**
	 * Optimizirano!
	 * 
	 * @param peptide
	 * @param type
	 * @return
	 */
	public static TreeSet<Ion> getBions(String peptide, Type type) {
		if (type == Type.B) {
			return getBIonsFAST(peptide);
		} else {
			return getYIonsFAST(peptide);
		}
	}

	// public static SortedSet<Ion> getBionsStaro(String peptide, Type type) {
	// try {
	// if (peptide.contains("X")) {
	// log.warn("Peptide {} sadrzi X", peptide);
	// log.debug("", new Throwable());
	// return new TreeSet<Ion>();
	// }
	// SortedSet<Ion> bioins = new TreeSet<Ion>();
	// JPLPeptide precursorSeq = new JPLPeptide.Builder(peptide).build();
	//
	// JPLPeptideFragmenter fragmenter = new
	// JPLPeptideFragmenter.Builder(EnumSet.of(JPLFragmentationType.BY,
	//
	// JPLFragmentationType.PRECURSOR)).build();
	//
	// fragmenter.setFragmentablePrecursor(precursorSeq, 1);
	// fragmenter.generateFragments();
	//
	// JPLMSPeakList bypiPl = fragmenter.getPeakList();
	// int size = bypiPl.size();
	// int count = 0;
	// JPLFragmentAnnotation a;
	// int i = 0;
	// if (type == Type.B) {
	// for (i = 0; i < size; i++) {
	// List<JPLFragmentAnnotation> anno = bypiPl.getAnnotationsAt(i);
	// a = anno.get(0);
	//
	// if (a.getIonType().equals(JPLPeptideType.B)) {
	// Ion b = new Ion(peptide.charAt(count) + "", bypiPl.getMzAt(i), count +
	// 1);
	// bioins.add(b);
	// count++;
	// }
	//
	// }
	//
	// // zadnji b oin dobijem tako da zadnji p oduzmem 18.0106
	// bioins.add(new Ion(peptide.charAt(count) + "", bypiPl.getMzAt(i - 1) -
	// 18.0106, count + 1));
	// }
	//
	// count = peptide.length() - 1;
	// if (type == Type.Y) {
	// for (i = 0; i < size; i++) {
	// List<JPLFragmentAnnotation> anno = bypiPl.getAnnotationsAt(i);
	// a = anno.get(0);
	//
	// // System.out.println("mz=" + bypiPl.getMzAt(i) + " rn=" +
	// // a.getResidueNumber() + " a="
	// // + a.getAminoAcid() + " ion=" + a.getIonType() + " " +
	// // a.toString());
	//
	// if (a.getIonType().equals(JPLPeptideType.Y)) {
	// Ion b = new Ion(peptide.charAt(count) + "", bypiPl.getMzAt(i), count);
	//
	// bioins.add(b);
	// count--;
	// }
	// }
	// // zadnji b oin dobijem tako da zadnji p oduzmem 18.0106
	// bioins.add(new Ion(peptide.charAt(count) + "", bypiPl.getMzAt(i - 1),
	// count));
	//
	// }
	//
	// return bioins;
	// } catch (Throwable e) {
	// log.error("Error with peptide: " + peptide, e);
	// // SWTUtil.showExceptionMessage("Error with peptide: " + peptide,
	// // e);
	// throw new RuntimeException("Error with peptide: " + peptide);
	// // throw new RuntimeException(e);
	// // return new TreeSet<BionUtils.Bion>();
	// }
	// }

	public static void main2(String[] args) {

		String s = "ERTDSDLSJS";
		SortedSet<Ion> bions = getBions(s, Ion.Type.Y);

		System.out.println(s);
		for (Ion bion : bions) {
			System.out.println(bion);
		}
	}

}
