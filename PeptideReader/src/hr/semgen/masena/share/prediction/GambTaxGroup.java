package hr.semgen.masena.share.prediction;

import hr.semgen.masena.share.ufb3.UFB3Hit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Grupa od nekoliko taxona sa hitovima al da hitovi ne prelaze 20 000
 */
public class GambTaxGroup {
	private HashMap<Integer, ArrayList<UFB3Hit>> taxonGroups;

	private int maxMembers = 19800;

	public GambTaxGroup(int maxMembers) {
		this();
		this.maxMembers = maxMembers;
	}

	public GambTaxGroup() {
		taxonGroups = new HashMap<Integer, ArrayList<UFB3Hit>>();
	}

	public HashMap<Integer, ArrayList<UFB3Hit>> getTaxonGroups() {
		return taxonGroups;
	}

	public int getMaxMembers() {
		return maxMembers;
	}

	/**
	 * Ako je ovo previse hitova za ovu grupu
	 * 
	 * @param hits2
	 * @return
	 */
	public boolean isTooMatch(ArrayList<UFB3Hit> hits) {
		int hitsCount = countHits();
		if ((hitsCount + hits.size()) > maxMembers) {
			return true;
		}
		return false;
	}

	public int countHits() {
		int c = 0;
		for (Entry<Integer, ArrayList<UFB3Hit>> entry : taxonGroups.entrySet()) {
			c += entry.getValue().size();
		}
		return c;
	}

	public void add(ArrayList<UFB3Hit> hits, int taxID) {
		if (isTooMatch(hits)) {
			throw new RuntimeException("Too match hits: " + countHits() + " + " + hits.size());
		}

		if (taxonGroups.containsKey(taxID)) {
			throw new RuntimeException("Exist taxon " + taxID);
		}

		taxonGroups.put(taxID, hits);
	}

}
