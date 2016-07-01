package hr.semgen.masena.share.prediction;

import java.io.Serializable;
import java.util.Map;

public class GambTaxCSV implements Serializable{
	private static final long serialVersionUID = 3374952438823227175L;
	
	private int taxID;
	private Map<Long, GambCSV> msmsIDcsvMap;
	
	
	public int getTaxID() {
		return taxID;
	}
	public void setTaxID(int taxID) {
		this.taxID = taxID;
	}
	public Map<Long, GambCSV> getMsmsIDcsvMap() {
		return msmsIDcsvMap;
	}
	public void setMsmsIDcsvMap(Map<Long, GambCSV> msmsIDcsvMap) {
		this.msmsIDcsvMap = msmsIDcsvMap;
	}
}
