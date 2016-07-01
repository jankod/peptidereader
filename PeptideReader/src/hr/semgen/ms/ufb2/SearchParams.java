package hr.semgen.ms.ufb2;

import hr.semgen.masena.ufb.DIVISIONS;

public class SearchParams {
	public double deltaMass = 0.3;
	public double deltaY = 0.3;
	public double deltaB = 0.3;
	public double filterLessThanIonsPercent = 51;

	public int uzmiOvolikoValjanih = 3000;

	public DIVISIONS[] divisions = DIVISIONS.values();

	/**
	 * Makni sve manje od 6. Znaci 5,4,3,2,1
	 */
	public short makniManjeUNizuOd = 6;

	/**
	 * tax id-evi novo
	 */
	public int[] taxids;

	
	/**
	 * Ako se searcha sa taxonomy. Nova metoda.
	 */
	public boolean searchWithTaxonomy = false;



	// novi parametri
}
