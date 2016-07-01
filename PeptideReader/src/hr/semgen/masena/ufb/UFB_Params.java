package hr.semgen.masena.ufb;

import hr.semgen.masena.share.Taxon;

import java.io.Serializable;
import java.util.Set;

/**
 * Parametri od klijenta. Koristi se i u UFB2.
 * 
 * @author tag
 * 
 */
public class UFB_Params implements Serializable {

	/**
	 * Ako je gel based onda se mora oznaciti odmah za UFB 3
	 */
	public boolean gelBased = false;

	/**
	 * Moze biti ESI recimo
	 */
	public String dataType = null; 
	// parametri Ultra Fast Blast UFB
	// delta mass
	// delta Y
	// delta B
	// show 50%

	private static final long serialVersionUID = -8556467968425107093L;
	public double deltaMass = 0.3;
	public double deltaY = 0.3;
	public double deltaB = 0.3;
	public double filterLessThanIonsPercent = 30;

	public int uzmiOvolikoValjanih = 6000;

	public DIVISIONS[] divisions = DIVISIONS.values();

	/**
	 * Makni sve manje od 6. Znaci 5,4,3,2,1
	 */
	public short makniManjeUNizuOd = 4;

	/**
	 * Peptide koje ne zelim u rezultatima
	 */
	public ExclusionGroup exclusionGroup;

	/**
	 * tax id-evi novo
	 */
	public int[] taxids;

	/**
	 * Ako se searcha sa taxonomy. Nova metoda.
	 */
	public boolean searchWithTaxonomy = false;

	/**
	 * Ovo sluzi samo za sejvanje koji je taxon user selektirao kada drugi put
	 * otvori dialog.
	 */
	public Set<Taxon> selectedTaxon;

	public float bestRelHPercent = 5;

	/**
	 * Da li da koristi relH
	 */
	public boolean useBestRelHPercent = false;

	/**
	 * Ako je true onda radi gamberger nad svima
	 */
	public boolean useLearningMachinePrediction = false;

	/**
	 * Koliko prvih najboljih vrsta da uzme za kalkulaciju LMP
	 */
	public int useLMPfirstBestSpecies;

}
