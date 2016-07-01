package hr.semgen.masena.share.mph;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Predstavlja hitove blasta za jedan MSMS
 * 
 * @author tag
 * 
 */
public class MphMsmsHit implements Serializable {

	private static final long serialVersionUID = -3001122359015065944L;

	private long msmsID;
	private ArrayList<MphBlastHit> hits = new ArrayList<MphBlastHit>();

	
	
	public void setHits(ArrayList<MphBlastHit> hits) {
		this.hits = hits;
	}

	public ArrayList<MphBlastHit> getHits() {
		return hits;
	}

	public void setMsmsID(long msmsID) {
		this.msmsID = msmsID;
	}

	public long getMsmsID() {
		return msmsID;
	}

	// private List<MphHit> hits = new ArrayList<MphHit>();
	//
	// public void setHits(List<MphHit> hits) {
	// this.hits = hits;
	// }
	//
	// public List<MphHit> getHits() {
	// return hits;
	// }

}
