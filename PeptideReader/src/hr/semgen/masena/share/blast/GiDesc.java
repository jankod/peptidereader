package hr.semgen.masena.share.blast;

import java.io.Serializable;

public class GiDesc implements Serializable {
	private static final long serialVersionUID = -8066330823635412160L;

	public long gi;
	public String desc;

	public int taxID;

	public String taxName;

	public short div;

	public GiDesc() {}
	public GiDesc(long gi, String desc, int taxID, String taxName, short div) {
		this.gi = gi;
		this.desc = desc;
		this.taxID = taxID;
		this.taxName = taxName;
		this.div = div;
	}

	@Override
	public String toString() {
		return gi + " " + desc;
	}
}
