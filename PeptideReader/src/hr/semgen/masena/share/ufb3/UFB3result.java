package hr.semgen.masena.share.ufb3;

import hr.semgen.masena.share.prediction.GambResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UFB3result implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<UFB3TaxGroup> taxon;

	// private boolean gelBased = false;

	/**
	 * Ako je gelBased onda je u pitanju samo jedan MS
	 */
	// private long msIDfromGelBased = 0;

	/**
	 * taxonID => hits
	 */
	private HashMap<Integer, ArrayList<UFB3Hit>> hits;

	
	/**
	 * TaxID => {msmsID => gambRes}
	 * Moze biti null.
	 */
	private Map<Integer, Map<Long, GambResult>> gambergerResult;

	public UFB3result() {
		hits = new HashMap<Integer, ArrayList<UFB3Hit>>();
		taxon = new ArrayList<UFB3TaxGroup>();
	}

	public void setHits(HashMap<Integer, ArrayList<UFB3Hit>> hits) {
		this.hits = hits;
	}

	/**
	 * taxonID => hits
	 * 
	 * @return
	 */
	public HashMap<Integer, ArrayList<UFB3Hit>> getHits() {
		return hits;
	}

	public void setTaxon(ArrayList<UFB3TaxGroup> taxon) {
		this.taxon = taxon;
	}

	public ArrayList<UFB3TaxGroup> getTaxon() {
		return taxon;
	}

	@Override
	public String toString() {
		String r = "";
		if (getTaxon() != null) {
			r += "taxona: " + getTaxon().size();
		}
		if (hits != null) {
			int c = 0;
			for (ArrayList<UFB3Hit> ah : hits.values()) {
				c += ah.size();
			}
			r += " hits: " + c;
		}
		return r;
	}

	public void addTaxon(UFB3TaxGroup taxGroup) {
		if (taxon == null) {
			taxon = new ArrayList<UFB3TaxGroup>(128 * 4);
		}
		taxon.add(taxGroup);
	}

	public void setGambergerResult(Map<Integer, Map<Long, GambResult>> gambergerResult) {
		this.gambergerResult = gambergerResult;
	}

	public Map<Integer, Map<Long, GambResult>> getGambergerResult() {
		return gambergerResult;
	}
}
