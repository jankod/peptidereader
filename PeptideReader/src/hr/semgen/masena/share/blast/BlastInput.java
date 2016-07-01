package hr.semgen.masena.share.blast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("serial")
public class BlastInput implements Serializable {

	public static void main(String[] args) {
		BlastInput b1 = new BlastInput();
		BlastInput b2 = new BlastInput();

		b1.BLAST_NUM_ALIGNMENTS = 22;
		b2.BLAST_NUM_ALIGNMENTS = 22;

		b1.peptides = new ArrayList<String>(Arrays.asList(new String[] { "pero", "perica" }));
		b2.peptides = new ArrayList<String>(Arrays.asList(new String[] { "pero", "perica" }));

		b1.BLAST_PARAM_DATABASE = DB.NR;
		b2.BLAST_PARAM_DATABASE = DB.NR;
		b1.BLAST_PARAM_WINDOW_SIZE = 22;
		b2.BLAST_PARAM_WINDOW_SIZE = 22;

		b1.plusMinus = 0.3f;
		b2.plusMinus = 0.3f;

		if (b1.hashCode() == b2.hashCode()) {
			System.out.println("DOBRO " + b1.hashCode() + " " + b2.hashCode());
		} else {
			System.err.println("LOSE");
		}
	}

	
	private String uniqueArray(int[] a) {
		if (a == null || a.length == 0)
			return null;
		String res = "";
		for (long l : a) {
			res +=l;
		}
		return res;
	}

	public static enum DB {
		/**
		 * NR
		 */
		NR,

		/**
		 * bakterijska baza iz nr
		 */
		BACT

	}

	
	/**
	 * Ako je positive onda je true
	 */
	public boolean positive = false;

	/**
	 * Dali da filtrira bazu sa gis tripsinolizane
	 */
	public boolean useTripticMass = true;

	public double massPrecursor;

	public ArrayList<String> peptides = new ArrayList<String>();

	/**
	 * Plus minus kada racuna mase koje odgovaraju
	 */
	public float plusMinus;

	/**
	 * Expectation value (E) threshold for saving hits. Inace vrjednost je 10
	 */
	public double BLAST_PARAM_E_VALUE = 20000;

	/**
	 * >=2 Word size for wordfinder algorithm
	 */
	public int BLAT_PARAM_WORD_SIZE = 2;

	public String BLAST_PARAM_MARTIX = "PAM30";

	/**
	 * >=0 Multiple hits window size, use 0 to specify 1-hit algorithm
	 */
	public int BLAST_PARAM_WINDOW_SIZE = 40;

	/**
	 * >=0 Minimum word score such that the word is added to the BLAST lookup
	 * table
	 */
	public double BLAST_PARAM_THRESHOLD = 11;

	/**
	 * U blastu je ovo: Max target sequences, a desc iz -help: => 0 Number of
	 * database sequences to show alignments for Default = `250'
	 */
	public int BLAST_NUM_ALIGNMENTS = 1000;

	/**
	 * Blast database
	 */
	public DB BLAST_PARAM_DATABASE = DB.NR;

	public boolean BLAST_ORGANISM_ALL = true;

	/**
	 * Jedino ako BLAST_ORGANISM_ALL == false
	 */
	public int[] BLAST_ORGANISM_TAXON;
	
	
	/**
	 * Dva kondiciona parametra, za lenght 4 i za vise
	 */
	public boolean userConditionParams = false;
	
	public int BLAST_NUM_ALIGNMENTS_OD_4 = 100000;
	public int BLAST_NUM_ALIGNMENTS_VECE_OD_4 = 20000;
	
	
	public String BLAST_PARAM_MARTIX_OD_4 = "PAM30";
	public String BLAST_PARAM_MARTIX_VECE_OD_4 = "BLOSUM80";
	
	
	
	public String shureShootPeptide;
	


	/**
	 * Sve osim peptida nede u ovo jer ide jedan po jedan peptid
	 * @return
	 */
	public String toUniqueString() {
		String uniqueString = this.BLAST_PARAM_MARTIX +"-"+ this.BLAST_NUM_ALIGNMENTS +"-"+ this.BLAST_PARAM_E_VALUE
				+"-"+ this.BLAST_PARAM_THRESHOLD +"-"+ this.BLAST_PARAM_WINDOW_SIZE +"-"+ this.BLAT_PARAM_WORD_SIZE
				+"-"+ this.plusMinus +"-"+ this.BLAST_ORGANISM_ALL
				+"-"+ uniqueArray(this.BLAST_ORGANISM_TAXON)
				+"-"+this.positive+"-"+this.BLAST_PARAM_DATABASE.name()+"-"+this.useTripticMass+"-"+this.userConditionParams;
		if(userConditionParams) {
			uniqueString += BLAST_NUM_ALIGNMENTS_OD_4+"-"+this.BLAST_NUM_ALIGNMENTS_VECE_OD_4+"-"+
			BLAST_PARAM_MARTIX_OD_4+"-"+BLAST_PARAM_MARTIX_VECE_OD_4;
		}
		return uniqueString;
		
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + BLAST_NUM_ALIGNMENTS;
		result = prime * result + (BLAST_ORGANISM_ALL ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(BLAST_ORGANISM_TAXON);
		result = prime * result + ((BLAST_PARAM_DATABASE == null) ? 0 : BLAST_PARAM_DATABASE.ordinal());
		long temp;
		temp = Double.doubleToLongBits(BLAST_PARAM_E_VALUE);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((BLAST_PARAM_MARTIX == null) ? 0 : BLAST_PARAM_MARTIX.hashCode());
		temp = Double.doubleToLongBits(BLAST_PARAM_THRESHOLD);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + BLAST_PARAM_WINDOW_SIZE;
		result = prime * result + BLAT_PARAM_WORD_SIZE;
		temp = Double.doubleToLongBits(massPrecursor);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((peptides == null) ? 0 : peptides.hashCode());
		temp = Double.doubleToLongBits(plusMinus);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (positive ? 1231 : 1237);
		result = prime * result + (useTripticMass ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlastInput other = (BlastInput) obj;
		if (BLAST_NUM_ALIGNMENTS != other.BLAST_NUM_ALIGNMENTS)
			return false;
		if (BLAST_ORGANISM_ALL != other.BLAST_ORGANISM_ALL)
			return false;
		if (!Arrays.equals(BLAST_ORGANISM_TAXON, other.BLAST_ORGANISM_TAXON))
			return false;
		if (BLAST_PARAM_DATABASE != other.BLAST_PARAM_DATABASE)
			return false;
		if (Double.doubleToLongBits(BLAST_PARAM_E_VALUE) != Double.doubleToLongBits(other.BLAST_PARAM_E_VALUE))
			return false;
		if (BLAST_PARAM_MARTIX == null) {
			if (other.BLAST_PARAM_MARTIX != null)
				return false;
		} else if (!BLAST_PARAM_MARTIX.equals(other.BLAST_PARAM_MARTIX))
			return false;
		if (Double.doubleToLongBits(BLAST_PARAM_THRESHOLD) != Double.doubleToLongBits(other.BLAST_PARAM_THRESHOLD))
			return false;
		if (BLAST_PARAM_WINDOW_SIZE != other.BLAST_PARAM_WINDOW_SIZE)
			return false;
		if (BLAT_PARAM_WORD_SIZE != other.BLAT_PARAM_WORD_SIZE)
			return false;
		if (Double.doubleToLongBits(massPrecursor) != Double.doubleToLongBits(other.massPrecursor))
			return false;
		if (peptides == null) {
			if (other.peptides != null)
				return false;
		} else if (!peptides.equals(other.peptides))
			return false;
		if (Double.doubleToLongBits(plusMinus) != Double.doubleToLongBits(other.plusMinus))
			return false;
		if (positive != other.positive)
			return false;
		if (useTripticMass != other.useTripticMass)
			return false;
		return true;
	}
}
