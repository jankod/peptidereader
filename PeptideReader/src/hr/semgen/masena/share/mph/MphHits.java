package hr.semgen.masena.share.mph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Upravlja hitovima. Cuva najbolje hitove.
 * 
 * @author tag
 * 
 */

public class MphHits {
	private ArrayList<MphHit> hits = new ArrayList<MphHit>(1);
	public int num;

	public int bestIdentical = 0;
	public boolean containSS;

	// public ArrayList<MphSS> ss = null;

	public MphHit first() {
		return hits.get(0);
	}

	public ArrayList<MphHit> getHits() {
		return hits;
	}

	/**
	 * Zadrzi samo one sa identical i sa 2 manje od najboljeg identical.
	 * 
	 * @param first
	 */
	public void add(MphHit first) {
		if (hits.isEmpty()) {
			hits.add(first);
			return;
		}

		if (isSameHit(first)) {
			return;
		}

		boolean dodajSVEnajbolje = true;
		if (dodajSVEnajbolje) {
			if (first.blastHit.identical > bestIdentical - 2) {
				hits.add(first);
			}
			bestIdentical = Math.max(bestIdentical, first.blastHit.identical);
			return;
		}

		// Dovoljno je jedan usporediti
		final MphHit old = hits.get(0);
		CompareHitResult role = compareTwoHit(first, old);

		// ostavi samo najboljeg/najbolje
		switch (role) {
		case FIRST_IS_BETTER:
			hits.clear();
			hits.add(first);
			break;
		case SAME:
			hits.add(first);
			break;
		case OLD_IS_BETTER:
			break;
		default:
			throw new RuntimeException("Koji je ovo role: " + role);
		}
	}

	public void prepare() {
		// makni sve koji su za 2 manji od best identical
		final Iterator<MphHit> it = hits.iterator();
		while (it.hasNext()) {
			MphHit h = it.next();
			if (h.blastHit.identical + 2 < bestIdentical) {
				it.remove();
			}
		}
		Collections.sort(hits, hitComparator);

		// searchSS();
	}

	public void searchSS(ArrayList<MphHits> hitsArray, Map<Long, Long> brotherMap) {
		final Iterator<MphHits> it = hitsArray.iterator();
		while (it.hasNext()) {
			MphHits hitsOther = it.next();
			if (hitsOther == this) {
				continue;
			}

			searchSS(hitsOther, this, brotherMap);
		}
	}

	/**
	 * Da SS hit bude na first()
	 */
	public void putSShitOnTop() {
		final MphHit first = first();

		if (first.getSs() == null) {
			for (MphHit h : hits) {
				if (h.getSs() != null) {
					hits.remove(h);
					hits.add(0, h);
					// System.out.println("remove add "+ h);
					return;
				}
			}
		}
	}

	/**
	 * uzeti prvi hit po ekspentanciji i vidjeti da li mu su drugi i
	 * treci i isti po Protein part, ako jesu onda stavim na vrh onoga sa najmanjim GI.
	 * I onda tek gledam SS.
	 */
	public void putMinGiOntop(Map<Integer, String> seqMap) {
		MphHit first = first();
		// seqMap je seqID => seq
		String seq = seqMap.get(first.seqID);
		
		if(seq == null) {
			throw new RuntimeException("Nisam nasao seq za gi "+ first.gi);
		}
		String proteinPartFirst = first.createProteinPart(seq);

		// ako nema SS
		if (first.getSs() == null) {
			for (MphHit h : hits) {
				if (proteinPartFirst.equals(h.createProteinPart(seqMap.get(h.seqID)))) {
					if(h.getGi() < first.gi)  {
						first = h;
					}
				} else {
					break;
				}
			}
		}
		addToFirstPos(first);
	}

	private void addToFirstPos(MphHit h) {
		hits.remove(h);
		hits.add(0, h);
	}

	private void searchSS(MphHits hits1, MphHits hits2, Map<Long, Long> brotherMap) {
		final Iterator<MphHit> it1 = hits1.hits.iterator();
		final Iterator<MphHit> it2 = hits2.hits.iterator();
		while (it1.hasNext()) {
			MphHit h1 = it1.next();
			while (it2.hasNext()) {
				MphHit h2 = it2.next();
				if (isSS(h1, h2, brotherMap)) {
					h1.setSs(h2);
					h2.setSs(h1);
					hits1.containSS = true;
					hits2.containSS = true;
				}
			}

		}

	}

	public static void main(String[] args) {
		long msms1id = 21L;
		long msms2id = 22L;
		Map<Long, Long> brotherMap = new HashMap<Long, Long>();
		brotherMap.put(msms1id, msms2id);
		brotherMap.put(msms2id, msms1id);

		MphHit h1 = new MphHit();
		MphHit h2 = new MphHit();
		h1.gi = 2;
		h2.gi = 2;

		h1.msmsID = msms1id;
		h2.msmsID = msms2id;

		h1.blastHit = new MphBlastHit();
		h1.blastHit.subjectStart = 164;
		h1.blastHit.subjectEnd = 178;

		h2.blastHit = new MphBlastHit();
		h2.blastHit.subjectStart = 161;
		h2.blastHit.subjectEnd = 177;

		MphHits mh = new MphHits();
		System.out.println(mh.isSS(h1, h2, brotherMap));
		;

	}

	private boolean isSS(MphHit h1, MphHit h2, Map<Long, Long> brotherMap) {
		if (h1.gi != h2.gi) {
			return false;
		}

		if (!isBrother(h1, h2, brotherMap)) {
			return false;
		}
		final int s1 = h1.blastHit.subjectStart;
		final int e1 = h1.blastHit.subjectEnd;

		final int s2 = h2.blastHit.subjectStart;
		final int e2 = h2.blastHit.subjectEnd;

		// dali je s2 ili e2 unutar rage s1-e1

		if (s2 >= s1 && s2 <= e1) {
			return true;
		}

		if (e2 >= s1 && e2 <= e1) {
			return true;
		}
		return false;
	}

	private boolean isBrother(MphHit h1, MphHit h2, Map<Long, Long> brotherMap) {
		if (!brotherMap.containsKey(h1.msmsID)) {
			return false;
		}
		final long posibleMSMS2id = brotherMap.get(h1.msmsID);
		if (h2.msmsID == posibleMSMS2id) {
			return true;
		}
		return false;
	}

	public static class MphSS {
		public final MphHit h2;

		public MphSS(MphHit h2) {
			this.h2 = h2;
		}
	}

	private static final Comparator<MphHit> hitComparator = new Comparator<MphHit>() {

		public int compare(MphHit o1, MphHit o2) {

			final float e1 = o1.blastHit.eValue;
			final float e2 = o2.blastHit.eValue;

			if (e1 > e2) {

				return 1;

			} else if (e1 < e2) {
				return -1;
			} else {

				final int i1 = o1.blastHit.identical;
				final int i2 = o2.blastHit.identical;

				if (i1 > i2) {
					return -1;
				} else if (i1 < i2) {
					return 1;
				}

			}
			return 0;
		}
	};

	/**
	 * Ako bilo koji od ovuh ima istu seq i start poziciju onda je isti hit
	 * 
	 * @param old
	 * @return
	 */
	private boolean isSameHit(MphHit old) {
		for (MphHit h : hits) {
			if (isSameHit(h, old)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Ako su oba hita ista, tj. ako je seqID i subjectStart isti.
	 * 
	 * @param old
	 * @param hit
	 * @return
	 */
	private final static boolean isSameHit(MphHit old, MphHit hit) {
		if (old.seqID == hit.seqID) {
			if (old.blastHit.subjectStart == hit.blastHit.subjectStart) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Komparira dva hita, first and old.
	 * 
	 * @param first
	 * @param old
	 * @return
	 */
	public final static CompareHitResult compareTwoHit(MphHit first, final MphHit old) {
		CompareHitResult role;

		// 1. Ko ima veci identical
		if (first.blastHit.identical > old.blastHit.identical) {

			role = CompareHitResult.FIRST_IS_BETTER;

		} else if (first.blastHit.identical < old.blastHit.identical) {
			role = CompareHitResult.OLD_IS_BETTER;
		} else {

			// 2. Ko ima veci percentage od positive matches
			if (first.blastHit.percentageOfpositiveMatches > old.blastHit.percentageOfpositiveMatches) {
				role = CompareHitResult.FIRST_IS_BETTER;
			} else if (first.blastHit.percentageOfpositiveMatches < old.blastHit.percentageOfpositiveMatches) {
				role = CompareHitResult.OLD_IS_BETTER;
			} else {
				// 3. Isti su, dodajem ga u listu
				role = CompareHitResult.SAME;
				// log.debug("Ovi su isti identical i %positives {}\n{}\n", hit,
				// old);
			}
		}
		return role;
	}

	/**
	 * Rezultat kompariranja 2 hita.
	 * 
	 * @author tag
	 * 
	 */
	public static enum CompareHitResult {
		/**
		 * Isti su
		 */
		SAME,
		/**
		 * Prvi je bolji.
		 */
		FIRST_IS_BETTER,

		/**
		 * Stari je bolji
		 */
		OLD_IS_BETTER
	}

	public static final HitsComparator hitsComparator = new HitsComparator();

	public static class HitsComparator implements Comparator<MphHits> {

		private HitsComparator() {
		}

		public int compare(MphHits o1, MphHits o2) {
			final MphHit first = o1.getHits().get(0);
			final MphHit old = o2.getHits().get(0);
			final CompareHitResult res = compareTwoHit(first, old);
			if (res == CompareHitResult.FIRST_IS_BETTER) {
				return -1;
			}
			if (res == CompareHitResult.OLD_IS_BETTER) {
				return 1;
			}
			return 0;
		}

	}

}
