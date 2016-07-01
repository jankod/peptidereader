package hr.semgen.masena.share.blast;

import hr.semgen.masena.share.ReturnRemoteObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Blast output je output jednog MSMS-a. Jedan MSMS ima vise peptida kojima je blastao pa tako i rezultate.
 * @author tag
 *
 */
public class BlastOutput extends ReturnRemoteObject {

	private static final long serialVersionUID = 1L;

	
	public String msmsID;
	
	public BlastOutput() {
	}

	public BlastOutput(String msmsID) {
		this.msmsID = msmsID;
	}
	
	public long timeForRunning;

	/**
	 * Peptide => result
	 */
	public Map<String, Set<BlastResult>> peptideResult = new HashMap<String, Set<BlastResult>>();


	public Map<String, Set<BlastResult>> getPeptideResult() {
		return peptideResult;
	}

	// public HashSet<FastaSeq> odgovarajuce = new HashSet<FastaSeq>();

}
