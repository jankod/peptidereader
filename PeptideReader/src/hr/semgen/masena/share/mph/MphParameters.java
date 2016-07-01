package hr.semgen.masena.share.mph;

import hr.semgen.masena.share.Taxon;
import hr.semgen.masena.share.model.PeakFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Parametri za algoritme, blast i analizu kasnju.
 * 
 * @author tag
 * 
 */
public class MphParameters implements Serializable {

	private static final long serialVersionUID = 9199849559472083048L;

	public final Algorithm algorithm = new Algorithm();
	public final Blast blast = new Blast();

	public float deltaLastRorK = 0.8f;

	public float deltaPeaks = 0.3F;

	/**
	 * TaxID po kojima se moze filtrirati, tj izraditi blast baza pa dalje.
	 * Sve podvrste ukljucene.
	 */
	public int[] filterByTaxIDs = new int[0];

	public ArrayList<PeakFile> selectedMS = new ArrayList<PeakFile>();

	/**
	 * Selektirani taxon, ali ne i podvrste.
	 */
	public Set<Taxon> selectedTaxon = new HashSet<Taxon>();

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public Blast getBlast() {
		return blast;
	}

	public float getDeltaLastRorK() {
		return deltaLastRorK;
	}

	public float getDeltaPeaks() {
		return deltaPeaks;
	}

	public int[] getFilterByTaxIDs() {
		return filterByTaxIDs;
	}

	public ArrayList<PeakFile> getSelectedMS() {
		return selectedMS;
	}

	public Set<Taxon> getSelectedTaxon() {
		return selectedTaxon;
	}

	public void setDeltaLastRorK(float deltaLastRorK) {
		this.deltaLastRorK = deltaLastRorK;
	}
	public void setDeltaPeaks(float deltaPeaks) {
		this.deltaPeaks = deltaPeaks;
	}

//	public ArrayList<Integer> selectedOrganisms = new ArrayList<Integer>();

	public void setFilterByTaxIDs(int[] filterByTaxIDs) {
		this.filterByTaxIDs = filterByTaxIDs;
	}
	
	public void setSelectedMS(ArrayList<PeakFile> selectedMS) {
		this.selectedMS = selectedMS;
	}

	public void setSelectedTaxon(Set<Taxon> selectedTaxon) {
		this.selectedTaxon = selectedTaxon;
	}


	/**
	 * Parametri algoritma
	 * 
	 * @author tag
	 * 
	 */
	public static class Algorithm {
		public int anchor_brojRacvanjaSidro = 5;



		public int anchor_length = 3;



		public int anchor_removePercentLeftRight = 1;



		public int anchor_takeFirstHighest = 80;



		public float deltaRorKlast = 0.8F;



		/**
		 * Minimalno koliko algoritam radi query seq.
		 */
		public int minQuerySeqLength = 5;



		/**
		 * Koliko generira query peptida za negative sa anchor algoritmom
		 */
		public int numNegativeAnchor = 5;



		public int numNegativeRightLeft = 5;



		public int numPostitiveRightLeft = 5;



		public float peakAaDelta = 0.3F;



		public int rightLeft_numberOfBranch = 5;



		public float rightRorKdelta = 0.8F;



		public boolean searchRorKwithH20 = true;



		public boolean searchRorKwithoutH20 = true;



		public boolean searchSorTwithoutH2O = true;



		private Algorithm() {
		}



		public int getAnchor_brojRacvanjaSidro() {
			return anchor_brojRacvanjaSidro;
		}



		public int getAnchor_length() {
			return anchor_length;
		}



		public int getAnchor_removePercentLeftRight() {
			return anchor_removePercentLeftRight;
		}



		public int getAnchor_takeFirstHighest() {
			return anchor_takeFirstHighest;
		}



		public float getDeltaRorKlast() {
			return deltaRorKlast;
		}



		public int getMinQuerySeqLength() {
			return minQuerySeqLength;
		}



		public int getNumNegativeAnchor() {
			return numNegativeAnchor;
		}



		public int getNumNegativeRightLeft() {
			return numNegativeRightLeft;
		}



		public int getNumPostitiveRightLeft() {
			return numPostitiveRightLeft;
		}



		public float getPeakAaDelta() {
			return peakAaDelta;
		}



		public int getRightLeft_numberOfBranch() {
			return rightLeft_numberOfBranch;
		}



		public float getRightRorKdelta() {
			return rightRorKdelta;
		}



		public boolean isSearchRorKwithH20() {
			return searchRorKwithH20;
		}



		public boolean isSearchRorKwithoutH20() {
			return searchRorKwithoutH20;
		}



		public boolean isSearchSorTwithoutH2O() {
			return searchSorTwithoutH2O;
		}
		public void setAnchor_brojRacvanjaSidro(int anchor_brojRacvanjaSidro) {
			this.anchor_brojRacvanjaSidro = anchor_brojRacvanjaSidro;
		}
		public void setAnchor_length(int anchor_length) {
			this.anchor_length = anchor_length;
		}

		public void setAnchor_removePercentLeftRight(int anchor_removePercentLeftRight) {
			this.anchor_removePercentLeftRight = anchor_removePercentLeftRight;
		}

		public void setAnchor_takeFirstHighest(int anchor_takeFirstHighest) {
			this.anchor_takeFirstHighest = anchor_takeFirstHighest;
		}

		public void setDeltaRorKlast(float deltaRorKlast) {
			this.deltaRorKlast = deltaRorKlast;
		}

		public void setMinQuerySeqLength(int minQuerySeqLength) {
			this.minQuerySeqLength = minQuerySeqLength;
		}
		
		
		
		public void setNumNegativeAnchor(int numNegativeAnchor) {
			this.numNegativeAnchor = numNegativeAnchor;
		}
		public void setNumNegativeRightLeft(int numNegativeRightLeft) {
			this.numNegativeRightLeft = numNegativeRightLeft;
		}
		public void setNumPostitiveRightLeft(int numPostitiveRightLeft) {
			this.numPostitiveRightLeft = numPostitiveRightLeft;
		}

		public void setPeakAaDelta(float peakAaDelta) {
			this.peakAaDelta = peakAaDelta;
		}

		public void setRightLeft_numberOfBranch(int rightLeft_numberOfBranch) {
			this.rightLeft_numberOfBranch = rightLeft_numberOfBranch;
		}
		public void setRightRorKdelta(float rightRorKdelta) {
			this.rightRorKdelta = rightRorKdelta;
		}
		public void setSearchRorKwithH20(boolean searchRorKwithH20) {
			this.searchRorKwithH20 = searchRorKwithH20;
		}
		public void setSearchRorKwithoutH20(boolean searchRorKwithoutH20) {
			this.searchRorKwithoutH20 = searchRorKwithoutH20;
		} 
		
		

		public void setSearchSorTwithoutH2O(boolean searchSorTwithoutH2O) {
			this.searchSorTwithoutH2O = searchSorTwithoutH2O;
		}
	}

	public static class Blast {
		public String evalue = "200000";
		public String gapExtend = "1";
		/**
		 * Ne radi dobro BLAST ako se promjeni na nesto drugo, od predvidjenih kombinacija gapopen i gapextend
		 */
		public String gapOpen = "9";
		public String matrix = "PAM30";
		public final String[] MATRIX_ALL = new String[] { "PAM30", "PAM70", "BLOSUM80", "BLOSUM62", "BLOSUM45" };
		public String maxTargetSeq = "500";
		public String treshold = "11";
		
		public String windowSize = "40";
		public String wordSize = "2";
		public String getEvalue() {
			return evalue;
		}
		public String getGapExtend() {
			return gapExtend;
		}
		public String getGapOpen() {
			return gapOpen;
		}
		public String getMatrix() {
			return matrix;
		}
		public String[] getMATRIX_ALL() {
			return MATRIX_ALL;
		}
		public String getMaxTargetSeq() {
			return maxTargetSeq;
		}
		public String getTreshold() {
			return treshold;
		}
		public String getWindowSize() {
			return windowSize;
		}
		public String getWordSize() {
			return wordSize;
		}
		public void setEvalue(String evalue) {
			this.evalue = evalue;
		}
		public void setGapExtend(String gapExtend) {
			this.gapExtend = gapExtend;
		}
		public void setGapOpen(String gapOpen) {
			this.gapOpen = gapOpen;
		}
		public void setMatrix(String matrix) {
			this.matrix = matrix;
		}
		public void setMaxTargetSeq(String maxTargetSeq) {
			this.maxTargetSeq = maxTargetSeq;
		}
		public void setTreshold(String treshold) {
			this.treshold = treshold;
		}
		public void setWindowSize(String windowSize) {
			this.windowSize = windowSize;
		}
		public void setWordSize(String wordSize) {
			this.wordSize = wordSize;
		}

	}

}
