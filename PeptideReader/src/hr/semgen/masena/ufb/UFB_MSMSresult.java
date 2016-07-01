package hr.semgen.masena.ufb;

import java.io.Serializable;
import java.util.ArrayList;

public class UFB_MSMSresult implements Serializable {

	private static final long serialVersionUID = 1L;

	public String msmsID;
	public String name;
	public boolean positive;
	
	

	public ArrayList<UFB_Protein> foundedProteins = new ArrayList<UFB_Protein>(0);
	
	/**
	 * Ako je founded protein 0
	 * @return
	 */
	public boolean isEmpty() {
		return foundedProteins.size() == 0;
	}
}
