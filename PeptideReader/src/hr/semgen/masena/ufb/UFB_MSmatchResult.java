package hr.semgen.masena.ufb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Rezultat MS match-a, u postocima
 * 
 * @author tag
 * 
 */
public final class UFB_MSmatchResult implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Svaki GI se matcha na workspace pojedinacno
	 */
	public HashMap<Long, GiMSmatchResult> giCountMatch = new HashMap<Long, GiMSmatchResult>();

	@Override
	public String toString() {
		
		return  "gis: " + giCountMatch.size();
	}
	
	

	/**
	 * Predstavlja result za jedan GI tj. protein
	 * @author tag
	 *
	 */
	public static final class GiMSmatchResult implements Serializable {
		public GiMSmatchResult(int countMSmatch, int totalMScount) {
			this.countMatch = countMSmatch;
			this.totalMScount = totalMScount;
		}

		private static final long serialVersionUID = 1L;
		
		
		/**
		 * Koliko se pozitivno matchalo cepanica.
		 */
		public int countMatch;
		
		/**
		 * Koliko je ukupno cepanica islo na match.
		 */
		public int totalMScount;
		
		/**
		 * Cepanice jednog proteina.
		 */
		public ArrayList<String> cepanice = new ArrayList<String>();

		
		/**
		 * Postotak matchanja.
		 * @return
		 */
		public double getPercentage() {
			if(totalMScount == 0) {
				return 0;
			}
			final double d = ((double)countMatch / (double)totalMScount) * 100;
			return d;
		}
	}

}
