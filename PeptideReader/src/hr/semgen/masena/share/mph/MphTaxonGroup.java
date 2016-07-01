package hr.semgen.masena.share.mph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rezultati za pojedini taxon. Grupirani po taxonu, cuvaju se za pojedini MSMS.
 * 
 * @author tag
 * 
 */
public class MphTaxonGroup {
	private static final Logger log = LoggerFactory.getLogger(MphTaxonGroup.class);

	/**
	 * MSMS_ID => HITS, poslje se postavlja na null
	 */
	private HashMap<Long, MphHits> _hits = new HashMap<Long, MphHits>(512);

	private ArrayList<MphHits> hitsArray = null;

	public int num = 1;
	public int taxID;
	public String desc;
	public int sumIdentical;

	public int countSS = 0;

	public void add(MphHit hit) {
		MphHits mHits = _hits.get(hit.msmsID);
		if (mHits == null) {
			mHits = new MphHits();
			_hits.put(hit.msmsID, mHits);

			// da li moze ovo?
			if (hitsArray == null) {
				hitsArray = new ArrayList<MphHits>();
			}
			hitsArray.add(mHits);
		}
		mHits.add(hit);
	}

	public ArrayList<MphHits> getHits() {
		return hitsArray;
	}

	public void prepare(Map<Long, Long> brotherMap, Map<Integer, String> seqMap) {
		hitsArray = new ArrayList<MphHits>(_hits.values());
		Collections.sort(hitsArray, MphHits.hitsComparator);
		for (MphHits h : hitsArray) {
			sumIdentical += h.first().blastHit.identical;
		}
		_hits = null;

		int num = 1;
		// SpringStopWatch s = SpringStopWatch.startNew("prepare ss");

		for (MphHits h : hitsArray) {
			h.num = num++;
			h.prepare();
			h.searchSS(hitsArray, brotherMap);
			h.putSShitOnTop();
			
			
			h.putMinGiOntop(seqMap);
		}
	}
	
	
	/**
	 * Postavlja hits na null da hessian ne save na disk.
	 */
	public void setUpHitsNull() {
		this.hitsArray = null;
	}

	/**
	 * Kada user klikne na taxon, onda ucita sa diska hitove pa ih postavi u
	 * ovaj objekt!
	 * 
	 * @param taxon
	 */
	public void setHitsFromDisk(ArrayList<MphHits> taxon) {
		this.hitsArray = taxon;
	}

	public void countSS() {
		for (MphHits hits : getHits()) {
			for (MphHit hit : hits.getHits()) {
				if (hit.getSs() != null) {
					countSS++;
					break;
				}
			}
		}
	}

}
