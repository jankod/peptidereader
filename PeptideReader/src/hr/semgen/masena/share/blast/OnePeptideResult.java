package hr.semgen.masena.share.blast;

import java.io.Serializable;
import java.util.ArrayList;

public class OnePeptideResult implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	String peptide;
	int num;
	
	ArrayList<BlastResult> res = new ArrayList<BlastResult>();

}
