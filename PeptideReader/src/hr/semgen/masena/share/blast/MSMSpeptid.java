package hr.semgen.masena.share.blast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MSMSpeptid implements Serializable{

	
	private static final long serialVersionUID = 1L;
	/**
	 * ID od MSMS
	 */
	public String msmsID;
	
	/**
	 * Peptidi sa kojima je searchan blast
	 */
	public Set<String> peptides = new HashSet<String>();
	
	
	/**
	 * Rezultati blasta za ove peptide
	 */
	public List<BlastResult> blastResults = new ArrayList<BlastResult>();
	
	public MSMSpeptid(String msmsID) {
		this.msmsID = msmsID;
	}
	
	public MSMSpeptid(String msmsID, Set<String> peptides) {
		this.msmsID = msmsID;
		this.peptides = peptides;
	}
}
