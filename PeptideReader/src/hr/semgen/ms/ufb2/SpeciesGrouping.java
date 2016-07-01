package hr.semgen.ms.ufb2;

import java.io.Serializable;

public final class SpeciesGrouping implements Serializable {
	private static final long serialVersionUID = 8138405756155089920L;

	/**
	 * taxonomical name
	 */
	private String taxName;

	/**
	 * taxID - taxID of species
	 */
	private int taxID;


	
	/**
	 * Predstavlja vrstu jednu. Constructor
	 * 
	 * @param taxonID
	 *            NCBI taxonomy ID
	 * @param taxName
	 *            NCBI taxonomy name
	 */
	public SpeciesGrouping(int taxonID, String taxName) {
		this.taxID = taxonID;
		this.taxName = taxName;
	}

	/**
	 * Vraca tax name i tax ID.
	 */
	@Override
	public String toString() {
		return taxName + "(" + taxID + ")";
	}

	// =====================================================================================
	// ============================GETTER_AND_SETTERS=======================================
	// =====================================================================================



	public int getTaxID() {
		return taxID;
	}

	public void setTaxID(int taxID) {
		this.taxID = taxID;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}
}
