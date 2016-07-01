package hr.semgen.ms.ufb2;

import java.io.Serializable;

public class PrecursorHit implements Serializable {

	private static final long serialVersionUID = 8071814985331796900L;
	public static final class MsmsMatchHit {

		/**
		 * Koliko se metcha cepanica na MSMS %
		 */
		float msmsMatchPercent;
	}

	/**
	 * <a href="https://sites.google.com/site/masenaspktro/task---ms-match">
	 * Google site</a> problem: nije vezano za hit vec za protein koji je vezan
	 * za hit, ali hit moze biti iz vise proteina
	 */
	public static final class MsMatchHit {

		/**
		 * Za koji GI sam radio match.
		 */
		int gi;

		/**
		 * Koliko se pozitivno matchalo cepanica.
		 */
		public int countMatch;

		/**
		 * Koliko je ukupno cepanica islo na match.
		 */
		public int totalMScount;
	}

}
