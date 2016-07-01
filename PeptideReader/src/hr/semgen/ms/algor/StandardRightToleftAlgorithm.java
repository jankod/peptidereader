package hr.semgen.ms.algor;

import hr.semgen.masena.share.model.NadjenaAA;
import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.Peptid;
import hr.semgen.masena.share.util.ProteinCalculation;
import hr.semgen.masena.share.util.ProteinsUtils;
import hr.semgen.masena.share.util.ProteinsUtils.SORT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardRightToleftAlgorithm implements IAlgorithm {
	private static final Logger				log							= LoggerFactory
			.getLogger(StandardRightToleftAlgorithm.class);

	private int								brojRacvanja				= 1;

	private boolean							addDummyPrecursorPeak		= true;

	private static Comparator<NadjenaAA>	comparatorNadjenih;

	private int								maxNumberResultBeforeStop	= Integer.MAX_VALUE;

	private boolean							stopedBecoseMaxResult		= false;

	private final double					peakAaDelta;

	final boolean							searchSorTwithoutH2O;

	private final boolean					searchRorKwithH2O;

	private final boolean					searchRorKwithoutH2O;

	/**
	 * Ako je zaustavljeno zbog max broja rezultata
	 * 
	 * @return
	 */
	public boolean isStopedBecoseMaxResult() {
		return stopedBecoseMaxResult;
	}

	public StandardRightToleftAlgorithm(int brojRacvanja, double peakAaDelta, boolean searchSorTwithoutH2O,
			boolean searchRorKwithH2O, boolean searchRorKwithoutH2O) {
		this.brojRacvanja = brojRacvanja;
		this.peakAaDelta = peakAaDelta;
		this.searchSorTwithoutH2O = searchSorTwithoutH2O;
		this.searchRorKwithH2O = searchRorKwithH2O;
		this.searchRorKwithoutH2O = searchRorKwithoutH2O;

	}

	public List<Peptid> run(List<Peak> peaks, float massPrecursor, double devPlusMinus) {
		// double dev = Activator.getPreferencePlusMinusR_or_K();
		double dev = devPlusMinus;
		List<Peptid> peptidi = new ArrayList<Peptid>();

		ProteinCalculation.removeBiggerThenMass(peaks, massPrecursor);
		ProteinCalculation.removeLessThanMass248(peaks);

		if (addDummyPrecursorPeak) ProteinCalculation.addDummyPrecursorPeak(massPrecursor, peaks);

		NadjenaAA desnaAA = ProteinCalculation.findLastRorKaa(peaks, dev, searchRorKwithH2O, searchRorKwithoutH2O);

		if (desnaAA == null) {
			// nije nasao percursor
			// log.debug("Nisam nasao desnu precursor aa!");
			return peptidi;
		}

		ProteinsUtils.get().sortByMass(peaks, SORT.VECI_PREMA_MANJEMU);

		Peak pocniOdPeak = getLjeviPik(desnaAA);

		trazi(peaks, peptidi, desnaAA, pocniOdPeak, brojRacvanja, searchSorTwithoutH2O);

		for (Peptid peptid : peptidi) {
			ProteinsUtils.get().checkAndReplaceTwoAA(peptid, peaks, peakAaDelta);

		}

		// OVO bi trebao biti KAFF
		ProteinsUtils.get().sortByMass(peaks);
		Peak KAF = peaks.get(0);
		SidroAlgorithm3.checkFitToEnd(peptidi, peaks, KAF.getCentroidMass(), massPrecursor);

		return new ArrayList<Peptid>(new HashSet<Peptid>(peptidi));
	}

	public void trazi(List<Peak> peaks, List<Peptid> peptidi, NadjenaAA desnaAA, Peak pocniOdPeak, int brojRacvanja,
			boolean searchSorTwithoutH2O) {
		Peptid p = new Peptid();
		p.addNew(desnaAA);

		List<NadjenaAA> nadjene = getLjevoDesnoPotencijalne(pocniOdPeak, peaks, peakAaDelta, searchSorTwithoutH2O);
		if (nadjene.isEmpty()) {
			return;
		}

		for (int i = 0; i < Math.min(brojRacvanja, nadjene.size()); i++) {
			Peptid copy = p.copy();
			NadjenaAA nadjena = nadjene.get(i);

			racvaj(copy, peptidi, nadjena, peaks, brojRacvanja);
		}

	}

	/**
	 * Radi rekurziju, trazi peakove sa rojem racvanja uljevo od trenutnog aa
	 * {@link NadjenaAA} nadjena
	 * 
	 * @param copy
	 * @param peptidi
	 * @param nadjena
	 * @param peaks
	 * @param brojRacvanja
	 */
	public void racvaj(Peptid copy, List<Peptid> peptidi, NadjenaAA nadjena, List<Peak> peaks, int brojRacvanja) {
		copy.addNew(nadjena);

		if (peptidi.size() > maxNumberResultBeforeStop) {
			stopedBecoseMaxResult = true;
			return;
		}
		if (Thread.currentThread().isInterrupted()) {
			return;
		}
		// log.debug("nasao: {}", copy);

		Peak pocniOdPeak = getLjeviPik(nadjena);
		List<NadjenaAA> nadjene = getLjevoDesnoPotencijalne(pocniOdPeak, peaks, peakAaDelta, searchSorTwithoutH2O);
		if (nadjene.isEmpty()) {
			peptidi.add(copy);
			// if(peptidi.size() % 5000 == 0) {
			// log.debug("peptida nasao: {}", peptidi.size());
			// }
			// log.debug("Nasao: {}", copy.getProtein());
			return;
		}

		for (int i = 0; i < Math.min(brojRacvanja, nadjene.size()); i++) {
			Peptid kopija = copy.copy();
			NadjenaAA uzeta = nadjene.get(i);
			racvaj(kopija, peptidi, uzeta, peaks, brojRacvanja);
		}
	}

	/**
	 * Sortira tako da ona sa najvecim pikom je na pocetku u listi. Ovisi kako
	 * je sortirana lista peaks, tako i trazi potencijalne.
	 * 
	 * @param odPik
	 * @param peaks
	 * @return
	 */
	public static List<NadjenaAA> getLjevoDesnoPotencijalne(final Peak odPik, List<Peak> peaks, double peakAaDelta,
			boolean searchSorTwithoutH2O) {
		List<NadjenaAA> nadjene = new ArrayList<NadjenaAA>();
		boolean start = false;
		if (Thread.currentThread().isInterrupted()) {
			return nadjene;
		}
		for (Peak peak : peaks) {

			if (!(start) && peak == odPik) {
				start = true;
				continue;
			}
			// NE RADI DOBRO, PROVJERITI!!!
			// if( Math.abs(odPik.getCentroidMass() - peak.getCentroidMass()) >
			// (300 + peakAaDelta) ) {
			// break; // veca razlika nema smisla traziti
			// }

			if (start) {
				String aa = ProteinCalculation.odgovaraMasiNekoj(odPik, peak, peakAaDelta, searchSorTwithoutH2O);
				if (aa != null) {
					NadjenaAA nadjenaAA = new NadjenaAA(odPik, peak, aa);
					nadjene.add(nadjenaAA);
				}
			}
		}

		if (comparatorNadjenih == null) comparatorNadjenih = new Comparator<NadjenaAA>() {
			public int compare(NadjenaAA o1, NadjenaAA o2) {
				Peak mjeljivi1 = null;
				Peak mjeljivi2 = null;

				if (o1.peak1 == odPik) {
					mjeljivi1 = o1.peak2;
				} else {
					mjeljivi1 = o1.peak1;
				}

				if (o2.peak1 == odPik) {
					mjeljivi2 = o2.peak2;
				} else {
					mjeljivi2 = o2.peak1;
				}
				return -Integer.valueOf(mjeljivi1.getHeight()).compareTo(mjeljivi2.getHeight());

			}

		};
		Collections.sort(nadjene, comparatorNadjenih);

		return nadjene;
	}

	public static Peak getLjeviPik(NadjenaAA desnaAA) {
		if (desnaAA.peak1.getCentroidMass() < desnaAA.peak2.getCentroidMass()) {
			return desnaAA.peak1;
		}
		return desnaAA.peak2;
	}

	// private List<NadjenaAA> getDesnoPotencijalne(Peak odPeak, List<Peak>
	// peaks) {
	//
	// ArrayList<NadjenaAA> resultList = new ArrayList<NadjenaAA>();
	//
	// boolean sadaGledaj = false;
	//
	// for (Peak trenutni : peaks) {
	// if (trenutni == odPeak) {
	// sadaGledaj = true;
	// continue;
	// }
	//
	// if (sadaGledaj) {
	// String aa = ProteinCalculation.odgovaraMasiNekoj(trenutni, odPeak,
	// ProteinCalculation.MASS_DEVIATION_AA, searchSorTwithoutH2O);
	// if (aa != null) {
	// NadjenaAA nadjenaAA = new NadjenaAA(trenutni, odPeak, aa);
	// resultList.add(nadjenaAA);
	// }
	// }
	//
	// }
	// return null;
	// }

	public void setAddDummyPrecursorPeak(boolean addDummyPrecursorPeak) {
		this.addDummyPrecursorPeak = addDummyPrecursorPeak;
	}

	public boolean isAddDummyPrecursorPeak() {
		return addDummyPrecursorPeak;
	}

	public void setMaxNumberResultBeforeStop(int maxNumberResultBeforeStop) {
		this.maxNumberResultBeforeStop = maxNumberResultBeforeStop;
	}

	public int getMaxNumberResultBeforeStop() {
		return maxNumberResultBeforeStop;
	}

}
