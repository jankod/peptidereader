package hr.semgen.ms.algor;

import hr.semgen.masena.share.model.NadjenaAA;
import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.Peptid;
import hr.semgen.masena.share.util.ProteinCalculation;
import hr.semgen.masena.share.util.ProteinConstants;
import hr.semgen.masena.share.util.ProteinsUtils;
import hr.semgen.masena.share.util.ProteinsUtils.SORT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SidroAlgorithm3 implements IAlgorithm {

	private static final Logger	log					= LoggerFactory.getLogger(SidroAlgorithm3.class);

	/**
	 * MAX masa koju neka aa moze sadrzavati
	 */
	private static final double	MAX_AA_MASS			= 186.08;
	private final boolean		cALCULATE_AA_R_OR_K_WITH_H2O;

	private final boolean		cALCULATE_AA_R_OR_K_WITHOUT_H2O;

	private int					duzinaSidra;

	private boolean				isPositive			= false;

	private Peak				KAF;

	private NadjenaAA			lastRorKaa;

	private Peptid				maxPeptideSidro		= null;

	private int					numberOfBranch2;

	private final double		peakAaDelta;

	private int					percentLeftRighrForRemove;

	private boolean				prosiriSidro;

	private final boolean		searchSorTwithoutH2O;

	private List<Peptid>		sidra;

	private int					to;

	private boolean				useSequenceRange	= false;

	private int					uzmiPrvihNajvihih;

	/**
	 * 
	 * @param duzinaSidra
	 *            je broj aa koje ce biti u sidru
	 * @param percentLeftRighrForRemove
	 * @param uzmiPrvihNajvihih
	 */
	public SidroAlgorithm3(int duzinaSidra, int percentLeftRighrForRemove, int uzmiPrvihNajvihih, boolean isPositive,
			double peakAaDelta, boolean searchSorTwithoutH2O, boolean CALCULATE_AA_R_OR_K_WITH_H2O,
			boolean CALCULATE_AA_R_OR_K_WITHOUT_H2O) {
		this.duzinaSidra = duzinaSidra;
		this.percentLeftRighrForRemove = percentLeftRighrForRemove;
		this.uzmiPrvihNajvihih = uzmiPrvihNajvihih;
		this.isPositive = isPositive;
		this.peakAaDelta = peakAaDelta;
		this.searchSorTwithoutH2O = searchSorTwithoutH2O;
		cALCULATE_AA_R_OR_K_WITH_H2O = CALCULATE_AA_R_OR_K_WITH_H2O;
		cALCULATE_AA_R_OR_K_WITHOUT_H2O = CALCULATE_AA_R_OR_K_WITHOUT_H2O;
	}

	public List<Peptid> run(List<Peak> originalPeaks, float massPrecursor, double plusMinusRorK) {
		if (originalPeaks.isEmpty()) {
			return new ArrayList<Peptid>();
		}

		maxPeptideSidro = null;

		ArrayList<Peak> peaks = new ArrayList<Peak>(originalPeaks);
		List<Peptid> peptidi = new ArrayList<Peptid>();

		if (!isPositive) { // negativ je
			lastRorKaa = ProteinCalculation.findLastRorKaa(originalPeaks, plusMinusRorK, cALCULATE_AA_R_OR_K_WITH_H2O,
					cALCULATE_AA_R_OR_K_WITHOUT_H2O);
			ProteinCalculation.removeBiggerThenMass(peaks, massPrecursor);
			ProteinCalculation.removeLessThanMass248(peaks);
			// OVO bi trebao biti KAFF
			ProteinsUtils.get().sortByMass(peaks);
			KAF = peaks.get(0);
		} else { // pozitiv je
			ProteinCalculation.addDummyPrecursorPeak((float) (massPrecursor - ProteinConstants.CAFF), peaks);
			ProteinCalculation.removeLessThenMass(peaks, 120);
			ProteinCalculation.removeBiggerThenMass(peaks, massPrecursor - ProteinConstants.CAFF);

		}
		// ProteinCalculation.addDummyPrecursorPeak(massPrecursor, peaks);

		removePercentLeftAndRight(peaks, percentLeftRighrForRemove);

		uzmiPrvihNajvisih(peaks, uzmiPrvihNajvihih);

		Peptid pepAnchorL = findSidro(peaks, duzinaSidra, SORT.MANJI_PREMA_VECEMU, peakAaDelta);
		Peptid pepAnchorR = findSidro(peaks, duzinaSidra, SORT.VECI_PREMA_MANJEMU, peakAaDelta);

		Peptid pepAnchor = null;

		if ((pepAnchorL != null) && (pepAnchorR != null)) {
			if (pepAnchorL.sumPeaksHeight() > pepAnchorR.sumPeaksHeight()) {
				pepAnchor = pepAnchorL;
			} else {
				pepAnchor = pepAnchorR;
			}
		} else {
			return Collections.emptyList();
		}

		sidra = new ArrayList<Peptid>();

		// stop.start("checkAndReplaceTwoAA");
		List<Peptid> replacedWpeptides = ProteinsUtils.get().checkAndReplaceTwoAA(pepAnchor, peaks, peakAaDelta);
		// stop.stop();
		sidra.addAll(replacedWpeptides);

		if (!prosiriSidro) {
			if (!isPositive) checkFitToEnd(sidra, peaks, KAF.getCentroidMass(), massPrecursor);
			return sidra;
		} else {
			// prosiri sidro
			for (Peptid sidro : sidra) {
				prosiriSidroUljevoIUdesno(sidro, new ArrayList<Peak>(originalPeaks), peptidi, massPrecursor);
			}
		}

		// remove i duplikate
		// stop.start("duplikati i check and replace two aa");
		HashSet<Peptid> peptidesResult = new HashSet<Peptid>(peptidi.size());
		for (Peptid peptid : peptidi) {
			List<Peptid> relacW = ProteinsUtils.get().checkAndReplaceTwoAA(peptid, peaks, peakAaDelta);
			peptidesResult.addAll(relacW);
		}

		ArrayList<Peptid> totalResult = new ArrayList<Peptid>(peptidesResult);
		if (!isPositive) { // NEGATIV
			checkFitToEnd(totalResult, peaks, KAF.getCentroidMass(), massPrecursor);
		}

		if (totalResult.size() > 10001) {
			// stop.start("preko 10000 nadjenih, sort i sublist");
			Collections.sort(totalResult, new Comparator<Peptid>() {

				public int compare(Peptid o1, Peptid o2) {
					return -Integer.valueOf(o1.sumPeaksHeight()).compareTo(o2.sumPeaksHeight());
				}
			});
			// stop.stop();
			// log.debug(stop.prettyPrint());
			final ArrayList<Peptid> newList = new ArrayList<Peptid>(totalResult.subList(0, 10000));
			removeOneKojisuSadrzaniUVecemuOPTIMIZIRANO(newList);
			return newList;

		}
		// stop.start("remove one koji su u vecemu");
		removeOneKojisuSadrzaniUVecemuOPTIMIZIRANO(totalResult);
		// stop.stop();

		// log.debug(stop.prettyPrint());
		return totalResult;
	}

	/**
	 * Ovo zapravo trazi sidro.
	 * 
	 * @param peaksPrvihNajvisih
	 * @param pepLength
	 * @return
	 */
	private Peptid findSidro(List<Peak> peaksPrvihNajvisih, int pepLength, SORT sort, double peakAaDelta) {
		maxPeptideSidro = null;
		ProteinsUtils.get().sortByMass(peaksPrvihNajvisih, sort);
		// log.debug("duzina sidra je: {}", pepLength);
		for (Peak peak : peaksPrvihNajvisih) {
			List<NadjenaAA> ljevoPotencijalne = StandardRightToleftAlgorithm.getLjevoDesnoPotencijalne(peak,
					peaksPrvihNajvisih, peakAaDelta, searchSorTwithoutH2O);
			for (NadjenaAA nadjenaAA : ljevoPotencijalne) {
				// ZABRANA R i K u sidru, nesmjeju biti ! Ovo je na 2 mjesta
				// postavljeno.
				if (nadjenaAA.aa.equals("R") || nadjenaAA.aa.equals("K")) {
					continue;
				}
				Peptid p = new Peptid();
				p.addNew(nadjenaAA);
				Peak leftPeak;
				if (sort == SORT.VECI_PREMA_MANJEMU) {
					leftPeak = getLeftPeak(p);
				} else {
					leftPeak = getRightPeak(p);
				}
				traziRekurzivnoSamoZaSidro(p, leftPeak, pepLength, peaksPrvihNajvisih, sort);
			}
		}
		return maxPeptideSidro;
	}

	private Peak getLeftPeak(Peptid sidro) {

		final List<NadjenaAA> asList = sidro.getAsList();
		if (asList.isEmpty()) {
			log.warn("sidro je prazno");
			return null;
		}
		Peak leftPeak = asList.get(0).peak1;
		for (NadjenaAA nadjenaAA : asList) {
			if (nadjenaAA.peak1.getCentroidMass() < leftPeak.getCentroidMass()) {
				leftPeak = nadjenaAA.peak1;
			}
			if (nadjenaAA.peak2.getCentroidMass() < leftPeak.getCentroidMass()) {
				leftPeak = nadjenaAA.peak2;
			}

		}
		return leftPeak;
	}

	private List<Peak> getPeaksMaseManjeIliJednakoOd(double centroidMass, List<Peak> peaks) {
		List<Peak> result = new ArrayList<Peak>();
		for (Peak peak : peaks) {
			if (peak.getCentroidMass() <= centroidMass) {
				result.add(peak);
			}
		}
		return result;
	}

	private List<Peak> getPeaksMaseVeceIliJednakoOd(double centroidMass, List<Peak> peaks) {
		List<Peak> result = new ArrayList<Peak>();
		for (Peak peak : peaks) {
			if (peak.getCentroidMass() >= centroidMass) {
				result.add(peak);
			}
		}
		return result;
	}

	private Peak getRightPeak(Peptid sidro) {
		final TreeSet<NadjenaAA> asList = sidro.getAsListOriginal();
		if (asList.isEmpty()) {
			log.warn("sidro je prazno");
			return null;
		}
		// Peak rightPeak = asList.get(0).peak1;
		Peak rightPeak = asList.first().peak1;
		;
		for (NadjenaAA nadjenaAA : asList) {

			if (nadjenaAA.peak1.getCentroidMass() > rightPeak.getCentroidMass()) {
				rightPeak = nadjenaAA.peak1;
			}
			if (nadjenaAA.peak2.getCentroidMass() > rightPeak.getCentroidMass()) {
				rightPeak = nadjenaAA.peak2;
			}

		}
		return rightPeak;
	}

	public List<Peptid> getSidra() {
		return sidra;
	}

	/**
	 * Ako se pozove ova metoda onda se osim broja racvanje za prosirenje i
	 * enable prosirenje.
	 * 
	 * @param numberOfBranch2
	 */
	public void prosiriSidro(int numberOfBranch2) {
		this.prosiriSidro = true;
		this.numberOfBranch2 = numberOfBranch2;

	}

	/**
	 * Ovdje treba paziti na R i K zadnji. Treba gledati dali zadnja a.a. je R
	 * ili K. Ako nije onda maknuti zadnju a.a. jer mora biti R ili K zadnja.
	 * 
	 * @param sidro
	 * @param spremiPeptidaNadjenog
	 * @param peaks
	 * @param massPrecursor
	 */
	private void prosiriSidroUdesno(Peptid sidro, List<Peptid> spremiPeptidaNadjenog, List<Peak> peaks,
			double massPrecursor) {
		// if (sidro.getProtein().equalsIgnoreCase("nqnpd")) {
		// log.debug("s {}", sidro);
		// }
		if (Thread.currentThread().isInterrupted()) {
			return;
		}
		Peak rigthPeak = getRightPeak(sidro);
		List<Peak> rigthPeaks = getPeaksMaseVeceIliJednakoOd(rigthPeak.getCentroidMass(), peaks);
		ProteinsUtils.get().sortByMass(rigthPeaks, SORT.MANJI_PREMA_VECEMU);
		List<NadjenaAA> desnoPotencijalne = StandardRightToleftAlgorithm.getLjevoDesnoPotencijalne(rigthPeak,
				rigthPeaks, peakAaDelta, searchSorTwithoutH2O);

		if (!desnoPotencijalne.isEmpty()) {

			List<NadjenaAA> nadjene = getNnajboljihPikova(desnoPotencijalne, numberOfBranch2);

			for (int i = 0; i < nadjene.size(); i++) {
				final NadjenaAA nadjena = nadjene.get(i);
				Peptid copySidro = new Peptid(sidro);

				if (lastRorKaa != null) {
					if (nadjena.peak2.getCentroidMass() == lastRorKaa.peak1.getCentroidMass()) {
						// ovdje je sidro
						copySidro.addNew(nadjena);
						copySidro.addNew(lastRorKaa);
						spremiPeptidaNadjenog.add(copySidro);
						// log.debug("Nasao zadnju r ili ka!!!");
						continue;
					} else if (nadjena.peak2.getCentroidMass() > lastRorKaa.peak1.getCentroidMass()) {
						continue;
					}
				}

				copySidro.addNew(nadjena);

				if (useSequenceRange) {
					final int length = copySidro.lengthAA();
					if (length >= to) {
						spremiPeptidaNadjenog.add(copySidro);
						continue;
					}
				}

				// OPET
				prosiriSidroUdesno(copySidro, spremiPeptidaNadjenog, peaks, massPrecursor);
			}
		} else {
			spremiPeptidaNadjenog.add(sidro);
		}
	}

	private void prosiriSidroUljevo(Peptid sidro, List<Peptid> spremiPeptidaNadjenog, List<Peak> peaks) {

		Peak leftPeak = getLeftPeak(sidro);
		// ProteinsUtils.get().sortByHeight(peaks, SORT.MANJI_PREMA_VECEMU);
		// List<Peak> leftPeaks = peaks.subList(0, peaks.indexOf(leftPeak));
		List<Peak> leftPeaks = getPeaksMaseManjeIliJednakoOd(leftPeak.getCentroidMass(), peaks);

		ProteinsUtils.get().sortByMass(leftPeaks, SORT.VECI_PREMA_MANJEMU);

		List<NadjenaAA> ljevoPotencijalne = StandardRightToleftAlgorithm.getLjevoDesnoPotencijalne(leftPeak, leftPeaks,
				peakAaDelta, searchSorTwithoutH2O);

		if (!ljevoPotencijalne.isEmpty()) {
			List<NadjenaAA> nadjene = getNnajboljihPikova(ljevoPotencijalne, numberOfBranch2);

			for (int i = 0; i < nadjene.size(); i++) {
				final NadjenaAA nadjena = nadjene.get(i);
				Peptid copySidro = new Peptid(sidro);
				copySidro.addNew(nadjena);
				if (useSequenceRange) {
					final int length = copySidro.lengthAA();
					if (length >= to) {
						spremiPeptidaNadjenog.add(copySidro);

						continue;
					}
				}

				// OPET
				prosiriSidroUljevo(copySidro, spremiPeptidaNadjenog, peaks);
			}
		} else {
			spremiPeptidaNadjenog.add(sidro);
		}

	}

	private void prosiriSidroUljevoIUdesno(Peptid sidro, List<Peak> peaks, List<Peptid> peptidi, double massPrecursor) {
		prosiriSidroUljevo(sidro, peptidi, peaks);
		ArrayList<Peptid> peptidi2 = new ArrayList<Peptid>(peptidi);
		for (Peptid peptid : peptidi2) {
			prosiriSidroUdesno(peptid, peptidi, peaks, massPrecursor);
		}
	}

	/**
	 * mice prvi i zadnjih 10 %
	 * 
	 * @param allPeaks
	 * @param i
	 * @return
	 */
	private void removePercentLeftAndRight(List<Peak> allPeaks, double percent) {
		List<Peak> result = new ArrayList<Peak>();
		ProteinsUtils.get().sortByMass(allPeaks, SORT.MANJI_PREMA_VECEMU);
		double ukupno = allPeaks.size();

		// double percent90 = (int) (ukupno * 0.9);
		// double percent10 = (int) (ukupno * 0.1);
		// System.out.println("da "+ (1 - percent/100));
		double percentBIG = ukupno * (1d - percent / 100);
		double percentLOW = (ukupno * percent / 100);

		// log.debug("Ukupno={} BIG%={} LOW%={}", new Object[] { ukupno,
		// percentLOW, percentBIG });
		int index = 0;
		TreeSet<Float> bigMassRemoved = new TreeSet<Float>();
		TreeSet<Float> lowMassRemoved = new TreeSet<Float>();
		for (Peak peak : allPeaks) {
			index++;
			if (index < percentLOW) {
				lowMassRemoved.add(peak.getCentroidMass());
				continue;
			}
			if (index > percentBIG) {
				bigMassRemoved.add(peak.getCentroidMass());
				continue;
			}

			result.add(peak);
		}

		allPeaks.clear();
		allPeaks.addAll(result);

		if (!bigMassRemoved.isEmpty()) {
			// ConsoleUtil.writeMes("Maknuo BIG od-do [{}-{}]",
			// bigMassRemoved.first(), bigMassRemoved.last());
			// ConsoleUtil.writeMes("Maknuo LOW od-do [{}-{}]",
			// lowMassRemoved.first(), lowMassRemoved.last());
			// log.debug("Maknuo BIG od-do [{}-{}]", bigMassRemoved.first(),
			// bigMassRemoved.last());
			// log.debug("Maknuo LOW od-do [{}-{}]", lowMassRemoved.first(),
			// lowMassRemoved.last());
		}

	}

	/**
	 * Duzinu pronadjene sequence koje vraca
	 * 
	 * @param from
	 * @param to
	 */
	public void setSeqRange(int from, int to) {
		this.useSequenceRange = true;
		this.to = to;

	}

	private void traziRekurzivnoSamoZaSidro(Peptid p, Peak leftPeak, int pepLength, List<Peak> peaks, SORT sort) {
		List<NadjenaAA> ljevoPotencijalne = StandardRightToleftAlgorithm.getLjevoDesnoPotencijalne(leftPeak, peaks,
				peakAaDelta, searchSorTwithoutH2O);
		for (NadjenaAA nadjenaAA : ljevoPotencijalne) {
			// ZABRANA R i K u sidru, nesmjeju biti ! Ovo je na 2 mjesta
			// postavljeno.
			if (nadjenaAA.aa.equals("R") || nadjenaAA.aa.equals("K")) {
				continue;
			}

			Peptid copypeptide = new Peptid(p);
			copypeptide.addNew(nadjenaAA);
			if (copypeptide.lengthAA() == pepLength) {
				// stop ovoga
				if (maxPeptideSidro != null) {
					if (maxPeptideSidro.sumPeaksHeight() < copypeptide.sumPeaksHeight()) {
						maxPeptideSidro = copypeptide;
						// log.debug("Nasao: {}", maxPeptide);
					}
				} else {
					maxPeptideSidro = copypeptide;
					// log.debug("Nasao pep: {}", maxPeptide);
				}

			} else {
				// idemo jos
				if (copypeptide.lengthAA() <= pepLength) {
					Peak lp;
					if (sort == SORT.VECI_PREMA_MANJEMU) {
						lp = getLeftPeak(copypeptide);
					} else {
						lp = getRightPeak(copypeptide);
					}
					traziRekurzivnoSamoZaSidro(copypeptide, lp, pepLength, peaks, sort);
				}
			}
		}
	}

	/**
	 * Uzima prvih x najvisih
	 * 
	 * @param peaks
	 * @param uzmiPrvihNajvihih
	 * @return
	 */
	private void uzmiPrvihNajvisih(List<Peak> peaks, int uzmiPrvihNajvihih) {
		if (peaks.size() < uzmiPrvihNajvihih) {
			// log.warn("Ima samo {} pikova", peaks.size());
		}

		ProteinsUtils.get().sortByHeight(peaks, SORT.VECI_PREMA_MANJEMU);
		List<Peak> result = new ArrayList<Peak>();
		for (int i = 0; i < Math.min(uzmiPrvihNajvihih, peaks.size()); i++) {

			result.add(peaks.get(i));
		}
		peaks.clear();
		peaks.addAll(result);
	}

	/**
	 * - Kod sidra, ljevo se mora poklapati sa KAF, a sdesno s R ili K. Ako se
	 * ne poklapa onda zbrisati tu zadnju aa. Znaci obrisati aa sljevo i desno
	 * ako je za 186.08 bliza kraju, a nije do kraja!
	 * 
	 * Metoda odrapi prvi i zadnji aa ako nije do kraja i bliza je 186.08 kraju
	 * jednome.
	 * 
	 * @param peptidi
	 * @param peaks
	 */
	public static void checkFitToEnd(List<Peptid> peptidi, List<Peak> peaks, double kafMass, double massPrecursor) {
		ProteinsUtils.get().sortByMass(peaks);
		// double kafMass = KAF.getCentroidMass();

		// FIXME: treba provjeriti ili najbolje napraviti novi algoritam.
		for (Peptid p : peptidi) {
			// TODO trebalo bi biti sortirano po masi vec
			List<NadjenaAA> peptide = p.getAsList();
			if (peptide.size() > 1) {

				NadjenaAA firstAA = peptide.get(0);
				double firstAAmass = firstAA.peak1.getCentroidMass();
				if (firstAAmass != kafMass && firstAAmass < (kafMass + MAX_AA_MASS)) {
					// log.debug("{} Brisem s ljevog kraja aa {}",
					// p.getProtein(), firstAA);
					p.removeAA(firstAA);

				}

				NadjenaAA lastAA = peptide.get(peptide.size() - 1);
				double lastAAmass = lastAA.peak2.getCentroidMass();

				if (lastAA.aa.equalsIgnoreCase("R") || lastAA.aa.equalsIgnoreCase("K")) {
					if (lastAAmass != massPrecursor) {
						// log.debug("brisem s desnog kraja {}", lastAA);
						p.removeAA(lastAA);
					}
					continue;
				}

				if (lastAAmass > (massPrecursor - MAX_AA_MASS)) {
					// log.debug("{} Brisem s desnog kraja aa {}",
					// p.getProtein(), lastAA);
					p.removeAA(lastAA);

				}

			}

		}

	}

	public static List<NadjenaAA> getNnajboljihPikova(List<NadjenaAA> potencijalne, int numberOfBranch2) {
		ProteinsUtils.get().sortNadjeneByHeight(potencijalne, SORT.VECI_PREMA_MANJEMU);
		ArrayList<NadjenaAA> result = new ArrayList<NadjenaAA>();
		for (int i = 0; i < Math.min(numberOfBranch2, potencijalne.size()); i++) {
			result.add(potencijalne.get(i));
		}
		return result;
	}

	public static void main(String[] args) {

	}

	public static void removeOneKojisuSadrzaniUVecemuOPTIMIZIRANO(List<Peptid> peptidi) {
		// FIXME: nece raditi dobro uvjek. AA moze biti sadrzano u PEAADRAA

		if (Thread.currentThread().isInterrupted()) {
			return;
		}

		HashSet<Peptid> nonDuplicateSet = new HashSet<Peptid>(peptidi);
		Map<Integer, List<Peptid>> p = new HashMap<Integer, List<Peptid>>();

		for (Peptid peptid : nonDuplicateSet) {
			if (peptid == null) {
				// FIXME: od spot search dolazi u listi negdje null!
				log.error("Peptid je null!!");
				continue;
			}
			String seq = peptid.getPeptide();
			if (p.containsKey(seq.length())) {
				p.get(seq.length()).add(peptid);
			} else {
				ArrayList<Peptid> newList = new ArrayList<Peptid>();
				newList.add(peptid);
				p.put(seq.length(), newList);
			}
		}

		ArrayList<Integer> peptidiLength = new ArrayList<Integer>(p.keySet());
		Collections.sort(peptidiLength, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return -o1.compareTo(o2);
			}

		});
		boolean isFirst = true;
		int najveci = peptidiLength.get(0);
		int najmanji = peptidiLength.get(peptidiLength.size() - 1);
		for (int i = najveci; i >= najmanji; i--) {
			// for (Integer i : peptidiLength) {
			if (isFirst) {
				isFirst = false;
				continue;
			}

			List<Peptid> grupaVeca = p.get(i + 1);

			if (grupaVeca == null) {
				continue;
			}
			for (int j = 0; j <= i; j++) {
				List<Peptid> grupa = p.get(j);
				if (grupa == null) {
					continue;
				}
				ArrayList<Peptid> copyGrupa = new ArrayList<Peptid>(grupa);

				for (Peptid p1 : copyGrupa) {
					for (Peptid p2 : grupaVeca) {
						if (p2.getPeptide().contains(p1.getPeptide())) {
							grupa.remove(p1);
						}
					}
				}
			}

		}
		// log.debug("maknuo novi algoritam {}", countRemove);

		peptidi.clear();
		for (List<Peptid> ostali : p.values()) {
			peptidi.addAll(ostali);
		}
	}

	public static enum STRANA {
		UDESNO, ULJEVO
	}

}
