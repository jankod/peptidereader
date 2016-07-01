package hr.semgen.masena.share.mph;

import hr.semgen.masena.share.model.PeptidDTO;

import java.util.HashMap;
import java.util.List;


/**
 * Peptidi pronadjeni algoritmom na msms-u. Ovdje su peptidi jednoga ms-a.
 * @author tag
 *
 */
public class MphMSPeptides {

//	private final long msID;

	/**
	 * MSMS ID => peptides
	 */
	private HashMap<Long, List<PeptidDTO>> msmsIDpeptides = new HashMap<Long, List<PeptidDTO>>();
	public MphMSPeptides(long msID) {
//		this.msID = msID;
	}

	public void put(long msmsID, List<PeptidDTO> peptides) {
		msmsIDpeptides.put(msmsID, peptides);
	}
	
	public HashMap<Long, List<PeptidDTO>> getMsmsIDpeptides() {
		return msmsIDpeptides;
	}

	
	
}
