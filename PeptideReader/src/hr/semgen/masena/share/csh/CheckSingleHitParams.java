package hr.semgen.masena.share.csh;

import hr.semgen.masena.share.model.PeakFile;
import hr.semgen.masena.ufb.UFB_Params;

public class CheckSingleHitParams {

	public PeakFile msms;
	public UFB_Params params;
	
	/**
	 * Ako je manji od 0 onda searcha sve taxone
	 */
	public int taxID;

}
