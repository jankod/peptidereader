package hr.semgen.masena.share.ufb3;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

public class UFB3TaxGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	private int redniBroj;

	private int taxID;
	private String taxName;
	private int ssNiz;
	private float rh;
	private float rhSS;
	private int ss;
	// private float mm;
	// private int mmCount;
	private int uniqueGI;
	
	public UFB3TaxGroup() {
	}
	
	public UFB3TaxGroup(int taxID, String taxName) {
		this.taxID = taxID;
		this.taxName = taxName;
	}
	
	

	

	@Override
	public String toString() {
		return "Taxon [taxID=" + taxID + ", taxName=" + taxName + ", ss=" + ss + "]";
	}





	private int hits;

//	@Override
	public void writeExternal(DataOutput out) throws IOException {
		out.writeInt(redniBroj);
		out.writeInt(taxID);
		out.writeUTF(taxName);
		out.writeInt(ssNiz);
		out.writeFloat(rh);
		out.writeFloat(rhSS);
		out.writeInt(ss);

		out.writeInt(uniqueGI);
		out.writeInt(hits);
	}

//	@Override
	public void readExternal(DataInput in) throws IOException, ClassNotFoundException {
		redniBroj = in.readInt();
		taxID = in.readInt();
		taxName = in.readUTF();
		ssNiz = in.readInt();
		rh = in.readFloat();
		rhSS = in.readFloat();
		ss = in.readInt();
		uniqueGI = in.readInt();
		hits = in.readInt();
	}

	public int getRedniBroj() {
		return redniBroj;
	}

	public void setRedniBroj(int redniBroj) {
		this.redniBroj = redniBroj;
	}

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

	public int getSsNiz() {
		return ssNiz;
	}

	public void setSsNiz(int ssNiz) {
		this.ssNiz = ssNiz;
	}

	public float getRh() {
		return rh;
	}

	public void setRh(float rh) {
		this.rh = rh;
	}

	public float getRhSS() {
		return rhSS;
	}

	public void setRhSS(float rhSS) {
		this.rhSS = rhSS;
	}

	public int getSs() {
		return ss;
	}

	public void setSs(int ss) {
		this.ss = ss;
	}

	// public float getMm() {
	// return mm;
	// }
	//
	// public void setMm(float mm) {
	// this.mm = mm;
	// }
	//
	// public int getMmCount() {
	// return mmCount;
	// }
	//
	// public void setMmCount(int mmCount) {
	// this.mmCount = mmCount;
	// }

	public int getUniqueGI() {
		return uniqueGI;
	}

	public void setUniqueGI(int uniqueGI) {
		this.uniqueGI = uniqueGI;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

}
