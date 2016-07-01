package hr.semgen.masena.ufb;

import java.io.Serializable;

public final class GI implements Serializable {

	public int taxonID;
	public long gi;
	
	/**
	 * GI desc
	 */
	private String desc;

	public GI() {
	}
	
	public GI(long gi, String desc, int taxonID) {
		this.gi = gi;
		this.desc = desc;
		this.taxonID = taxonID;
	}

	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	private static final long serialVersionUID = -5652075369080028085L;

	@Override
	public String toString() {
		return "gi="+gi + " " + " tax="+taxonID +" "+ desc;
	}

}
