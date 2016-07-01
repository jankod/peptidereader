package hr.semgen.masena.share.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BioUtil {

	private static final Pattern pattternGI = Pattern.compile("gi\\|([0-9]*)\\|");

	
//	public static String align(String s1, String s2) throws Exception {
//
//		// The alphabet of the sequences. For this example DNA is chosen.
//		FiniteAlphabet alphabet = (FiniteAlphabet) ProteinTools.getAlphabet();
//		// Use a substitution matrix with equal scores for every match and every
//		// replace.
//		short match = 1;
//		short replace = -1;
//		SubstitutionMatrix matrix = new SubstitutionMatrix(alphabet, match, replace);
//
//		// Firstly, define the expenses (penalties) for every single operation.
//		short insert = 2;
//		short delete = 2;
//		short gapExtend = 2;
//		// Global alignment.
//		AlignmentAlgorithm aligner = new NeedlemanWunsch(match, replace, insert, delete, gapExtend, matrix);
//		Sequence query = ProteinTools.createProteinSequence(s1, "query");
//		Sequence target = ProteinTools.createProteinSequence(s2, "target");
//		// Perform an alignment and save the results.
//		aligner.pairwiseAlignment(query, // first sequence
//				target // second one
//		);
//		// Print the alignment to the screen
//		System.out.println("Global alignment with Needleman-Wunsch:\n" + aligner.toString());
//
//		// Perform a local alignment from the sequences with Smith-Waterman.
//		aligner = new SmithWaterman(match, replace, insert, delete, gapExtend, matrix);
//		// Perform the local alignment.
//		aligner.pairwiseAlignment(query, target);
//		System.out.println("\nLocal alignment with Smith-Waterman:\n" + aligner.toString());
//		return s2;
//
//	}

	
//	public static void main(String[] args) throws Exception {
//		align("GVDLEQVD", "RLPGVDLEQVNAK");
//		
//		
//	}
	/**
	 * Tragi GI brojeve u fasta headeru koji moze biti vise headera
	 * 
	 * @param line
	 * @return
	 */
	public static final ArrayList<Integer> findGI(String line) {
		ArrayList<Integer> gis = new ArrayList<Integer>();
		Matcher res = pattternGI.matcher(line);

		while (res.find()) {
			// log.debug("======'{}'", res.group(1));
			gis.add(Integer.parseInt(res.group(1)));

		}
		return gis;

	}
}
