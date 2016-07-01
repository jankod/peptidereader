package hr.semgen.masena.share.mph;

import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.PeakFile;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MphMS implements Serializable {

	private static final long serialVersionUID = -2561550749516856550L;
	private static final Logger log = LoggerFactory.getLogger(MphMS.class);

	private long id;
	private TreeSet<MphPeak> peaks;
	private TreeMap<Float, MphMsmsBrother> brothers;

	/**
	 * Konstruktor, koji od starog MS-a, stvara novi.
	 * 
	 * @param ms
	 */
	public MphMS(PeakFile ms) {
		if (!ms.isMS()) {
			throw new RuntimeException("This is not MS!");
		}
		id = ms.getId();
		peaks = new TreeSet<MphPeak>();

		for (Peak peak : ms.getAllPeaks()) {
			MphPeak p = new MphPeak(peak);
			peaks.add(p);
		}

		brothers = new TreeMap<Float, MphMS.MphMsmsBrother>();
		final Collection<PeakFile> msmsChilds = ms.getMsmsChilds();
		for (PeakFile msms : msmsChilds) {
			MphMsms newMsms = new MphMsms(msms);
			addNewMsms(newMsms);
		}
	}

	private void addNewMsms(MphMsms msms) {
		MphMsmsBrother brother = brothers.get(msms.getPrecursorMass());
		if (brother == null) {
			brother = new MphMsmsBrother();
			brothers.put(msms.getPrecursorMass(), brother);
		}
		brother.add(msms);
	}

	/**
	 * MSMS brother wraper
	 * 
	 * @author tag
	 * 
	 */
	public static class MphMsmsBrother implements Serializable, Comparable<MphMsms> {
		private static final long serialVersionUID = -1589310975804067774L;
		public MphMsms negative;
		public MphMsms positive;

		float getMassPrecursor() {
			if (negative != null) {
				return negative.getPrecursorMass();
			}
			if (positive != null) {
				return positive.getPrecursorMass();
			}
			throw new RuntimeException("Not set one MSMS!");
		}

		public void add(MphMsms msms) {
			if (msms.isPositive()) {
				ifNotNull(positive, "prepisujem preko varijable");
				positive = msms;
			} else {
				ifNotNull(positive, "prepisujem preko varijable");
				negative = msms;
			}

		}

		public int compareTo(MphMsms o) {
			// TODO napraviti da radi sa statickom metodom radi brzine
			return Float.valueOf(getMassPrecursor()).compareTo(o.getPrecursorMass());
		}

		public String toString() {
			if (negative != null && positive != null) {
				return "neg: " + negative.getPrecursorMass() + " pos: " + positive.getPrecursorMass();
			}
			if (negative != null) {
				return "neg: " + negative.getPrecursorMass();
			}
			if (positive != null) {
				return "pos: " + positive.getPrecursorMass();
			}
			
			return "In brother pos and neg are null !!!";
		}
	}

	/**
	 * 
	 * @return mapu parova masa prekusora => MSMS (poz. i neg.) .
	 */
	public TreeMap<Float, MphMsmsBrother> getBrothers() {
		return brothers;
	}

	public static void ifNotNull(MphMsms msms, String msg) {
		if (msms != null) {
			throw new RuntimeException(msg);
		}
	}

	public long getId() {
		return id;
	}

	public TreeSet<MphPeak> getPeaks() {
		return peaks;
	}

}
