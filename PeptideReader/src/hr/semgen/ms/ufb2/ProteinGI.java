package hr.semgen.ms.ufb2;

import java.io.Serializable;

public final class ProteinGI implements Serializable {

	private static final long serialVersionUID = 1234L;

	
	private long gi;
	
	/**
	 * NCBI taxonomy ID ovog GI-a
	 */
	private int taxID;
	
	private String description;

	
	// =====================================================================================
	// ============================GETTER_AND_SETTER========================================
	// =====================================================================================
	
	public long getGi() {
		return gi;
	}

	public void setGi(long gi) {
		this.gi = gi;
	}

	public int getTaxID() {
		return taxID;
	}

	public void setTaxID(int taxID) {
		this.taxID = taxID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
