package hr.semgen.masena.share.mph;

import hr.semgen.masena.share.model.PeakFile;

import java.util.ArrayList;

public class MphParametersBean {

	private final Algorithm algorithm = new Algorithm();
	private final Blast blast = new Blast();

	private float deltaPeaks = 0.3F;

	private ArrayList<Integer> selectedOrganisms = new ArrayList<Integer>();

	/**
	 * TaxID po kojima se moze filtrirati, tj izraditi blast baza pa dalje
	 */
	private int[] filterByTaxIDs = new int[0];

	private ArrayList<PeakFile> selectedMS = new ArrayList<PeakFile>();
	
	
	public float getDeltaPeaks() {
		return deltaPeaks;
	}

	public void setDeltaPeaks(float deltaPeaks) {
		this.deltaPeaks = deltaPeaks;
	}

	public ArrayList<Integer> getSelectedOrganisms() {
		return selectedOrganisms;
	}

	public void setSelectedOrganisms(ArrayList<Integer> selectedOrganisms) {
		this.selectedOrganisms = selectedOrganisms;
	}

	public int[] getFilterByTaxIDs() {
		return filterByTaxIDs;
	}

	public void setFilterByTaxIDs(int[] filterByTaxIDs) {
		this.filterByTaxIDs = filterByTaxIDs;
	}

	public ArrayList<PeakFile> getSelectedMS() {
		return selectedMS;
	}

	public void setSelectedMS(ArrayList<PeakFile> selectedMS) {
		this.selectedMS = selectedMS;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public Blast getBlast() {
		return blast;
	}

	

	public static class Blast {
		private final String[] MATRIX_ALL = new String[] { "PAM30", "PAM70", "BLOSUM80", "BLOSUM62", "BLOSUM45" };
		private String matrix = "PAM30";
		private String evalue = "20000";
		private String wordSize = "2";
		private String maxTargetSeq = "500";
		
		
		public String getMatrix() {
			return matrix;
		}
		public void setMatrix(String matrix) {
			this.matrix = matrix;
		}
		public String getEvalue() {
			return evalue;
		}
		public void setEvalue(String evalue) {
			this.evalue = evalue;
		}
		public String getWordSize() {
			return wordSize;
		}
		public void setWordSize(String wordSize) {
			this.wordSize = wordSize;
		}
		public String getMaxTargetSeq() {
			return maxTargetSeq;
		}
		public void setMaxTargetSeq(String maxTargetSeq) {
			this.maxTargetSeq = maxTargetSeq;
		}
		public String[] getMATRIX_ALL() {
			return MATRIX_ALL;
		}

	}

	/**
	 * Parametri algoritma
	 * 
	 * @author tag
	 * 
	 */
	public static class Algorithm {
		public int anchor_length = 3;
		private int anchor_removePercentLeftRight = 1;
		private int anchor_takeFirstHighest = 80;

		private int anchor_brojRacvanjaSidro = 5;

		private int rightLeft_numberOfBranch = 5;

		private float rightRorKdelta = 0.8F;

		private float peakAaDelta = 0.3F;

		private int numNegativeAnchor = 5;
		private int numNegativeRightLeft = 5;
		private int numPostitiveRightLeft = 5;

		private boolean searchSorTwithoutH2O = true;

		private float deltaRorKlast = 0.8F;
		private boolean searchRorKwithH20 = true;
		private boolean searchRorKwithoutH20 = true;
		
		/**
		 * Koliko generira query peptida za negative sa anchor algoritmom
		 */
		private int numQueryPeptideForAnchorNegative;
		private int numQueryPeptideForRightLeftNegative;
		
		private int numQueryPeptideForRightLeftPositive;
		
		
		
		public int getAnchor_length() {
			return anchor_length;
		}

		public void setAnchor_length(int anchor_length) {
			this.anchor_length = anchor_length;
		}

		public int getAnchor_removePercentLeftRight() {
			return anchor_removePercentLeftRight;
		}

		public void setAnchor_removePercentLeftRight(int anchor_removePercentLeftRight) {
			this.anchor_removePercentLeftRight = anchor_removePercentLeftRight;
		}

		public int getAnchor_takeFirstHighest() {
			return anchor_takeFirstHighest;
		}

		public void setAnchor_takeFirstHighest(int anchor_takeFirstHighest) {
			this.anchor_takeFirstHighest = anchor_takeFirstHighest;
		}

		public int getAnchor_brojRacvanjaSidro() {
			return anchor_brojRacvanjaSidro;
		}

		public void setAnchor_brojRacvanjaSidro(int anchor_brojRacvanjaSidro) {
			this.anchor_brojRacvanjaSidro = anchor_brojRacvanjaSidro;
		}

		public int getRightLeft_numberOfBranch() {
			return rightLeft_numberOfBranch;
		}

		public void setRightLeft_numberOfBranch(int rightLeft_numberOfBranch) {
			this.rightLeft_numberOfBranch = rightLeft_numberOfBranch;
		}

		public float getRightRorKdelta() {
			return rightRorKdelta;
		}

		public void setRightRorKdelta(float rightRorKdelta) {
			this.rightRorKdelta = rightRorKdelta;
		}

		public float getPeakAaDelta() {
			return peakAaDelta;
		}

		public void setPeakAaDelta(float peakAaDelta) {
			this.peakAaDelta = peakAaDelta;
		}

		public int getNumNegativeAnchor() {
			return numNegativeAnchor;
		}

		public void setNumNegativeAnchor(int numNegativeAnchor) {
			this.numNegativeAnchor = numNegativeAnchor;
		}

		public int getNumNegativeRightLeft() {
			return numNegativeRightLeft;
		}

		public void setNumNegativeRightLeft(int numNegativeRightLeft) {
			this.numNegativeRightLeft = numNegativeRightLeft;
		}

		public int getNumPostitiveRightLeft() {
			return numPostitiveRightLeft;
		}

		public void setNumPostitiveRightLeft(int numPostitiveRightLeft) {
			this.numPostitiveRightLeft = numPostitiveRightLeft;
		}

		public boolean isSearchSorTwithoutH2O() {
			return searchSorTwithoutH2O;
		}

		public void setSearchSorTwithoutH2O(boolean searchSorTwithoutH2O) {
			this.searchSorTwithoutH2O = searchSorTwithoutH2O;
		}

		public float getDeltaRorKlast() {
			return deltaRorKlast;
		}

		public void setDeltaRorKlast(float deltaRorKlast) {
			this.deltaRorKlast = deltaRorKlast;
		}

		public boolean isSearchRorKwithH20() {
			return searchRorKwithH20;
		}

		public void setSearchRorKwithH20(boolean searchRorKwithH20) {
			this.searchRorKwithH20 = searchRorKwithH20;
		}

		public boolean isSearchRorKwithoutH20() {
			return searchRorKwithoutH20;
		}

		public void setSearchRorKwithoutH20(boolean searchRorKwithoutH20) {
			this.searchRorKwithoutH20 = searchRorKwithoutH20;
		}

		public int getNumQueryPeptideForAnchorNegative() {
			return numQueryPeptideForAnchorNegative;
		}

		public void setNumQueryPeptideForAnchorNegative(int numQueryPeptideForAnchorNegative) {
			this.numQueryPeptideForAnchorNegative = numQueryPeptideForAnchorNegative;
		}

		public int getNumQueryPeptideForRightLeftNegative() {
			return numQueryPeptideForRightLeftNegative;
		}

		public void setNumQueryPeptideForRightLeftNegative(int numQueryPeptideForRightLeftNegative) {
			this.numQueryPeptideForRightLeftNegative = numQueryPeptideForRightLeftNegative;
		}

		public int getNumQueryPeptideForRightLeftPositive() {
			return numQueryPeptideForRightLeftPositive;
		}

		public void setNumQueryPeptideForRightLeftPositive(int numQueryPeptideForRightLeftPositive) {
			this.numQueryPeptideForRightLeftPositive = numQueryPeptideForRightLeftPositive;
		}



		private Algorithm() {
		}
	}

}
