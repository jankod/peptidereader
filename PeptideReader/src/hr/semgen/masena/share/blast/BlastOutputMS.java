package hr.semgen.masena.share.blast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BlastOutputMS implements Serializable {

	private static final long serialVersionUID = 1L;

	private String msName;

	private Map<String, BlastOutput> msmsID_BlastOutputMap = new HashMap<String, BlastOutput>();

	
	public BlastOutputMS(String msName) {
		this.msName = msName;
	}
	
	public void setMsName(String msName) {
		this.msName = msName;
	}

	public String getMsName() {
		return msName;
	}

	/**
	 * Sinhronizirana metoda. Baca runtimeexception ako vec postoji unutra key msmsID.
	 * @param msmsID
	 * @param blastOutput
	 */
	public void add(String msmsID, BlastOutput blastOutput) {
		if (msmsID_BlastOutputMap.containsKey(msmsID)) {
			throw new RuntimeException("Vec postoji MSMS ID: " + msmsID);
		}
		synchronized (msmsID_BlastOutputMap) {
			msmsID_BlastOutputMap.put(msmsID, blastOutput);
		}
	}

	public void setMsmsID_BlastOutputMap(Map<String, BlastOutput> msmsID_BlastOutputMap) {
		this.msmsID_BlastOutputMap = msmsID_BlastOutputMap;
	}

	public Map<String, BlastOutput> getMsmsID_BlastOutputMap() {
		return msmsID_BlastOutputMap;
	}

}
