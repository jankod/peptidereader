package hr.semgen.masena.share.model;

import hr.semgen.masena.share.util.ProteinsUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.primitives.Doubles;

public class Peptid implements Serializable {
	private static final Logger log = LoggerFactory.getLogger(Peptid.class);

	private static final long serialVersionUID = 1L;
	// public List<NadjenaAA> aaList2 = new ArrayList<NadjenaAA>();

	private TreeSet<NadjenaAA> aaList = new TreeSet<NadjenaAA>(comparator);

	private StringBuilder cacheProt = null;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aaList == null) ? 0 : aaList.hashCode());
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
		Peptid other = (Peptid) obj;
		if (aaList == null) {
			if (other.aaList != null)
				return false;
		} else if (!aaList.equals(other.aaList))
			return false;
		return true;
	}

	private static final SerializableComparator<NadjenaAA> comparator = new SerializableComparator<NadjenaAA>() {

		private static final long serialVersionUID = 1L;

		public int compare(NadjenaAA o1, NadjenaAA o2) {
			return Doubles.compare(o1.peak1.getCentroidMass() + o1.peak2.getCentroidMass(), o2.peak1.getCentroidMass()
					+ o2.peak2.getCentroidMass());
		}
	};

	public Peptid() {
		aaList = new TreeSet<NadjenaAA>(comparator);
	}

	public Peptid(Peptid sidro) {
		this();

		this.aaList.addAll(sidro.aaList);
	}

	public void add(NasaoProteina res) {
		aaList.addAll(res.proteiniList);
		cacheProt = null;
	}

	public void addNew(NadjenaAA aa) {
		if (sadrziLista(aa)) {
			throw new RuntimeException("Vec postoji u listi " + aa + " lista size: " + aaList.size() + " " + aaList);
		}
		if (!aaList.add(aa)) {
			log.debug("Necu ovo u setu " + aa);
		}
		cacheProt = null;
	}

	public void addAll(List<NadjenaAA> listNadjenih) {
		aaList.addAll(listNadjenih);
		cacheProt = null;
	}

	public Peptid copy() {
		try {
			Peptid p = new Peptid(this);
			return p;
			// return (Peptid) ObjectCloner.deepCopy(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int lengthAA() {
		return aaList.size();
	}

	public java.util.List<NadjenaAA> getAsList() {
		ArrayList<NadjenaAA> res = new ArrayList<NadjenaAA>(aaList);
		return res;
	}

	public TreeSet<NadjenaAA> getAsListOriginal() {
		return aaList;
	}

	public String getPeptide() {
		if (cacheProt == null) {
			cacheProt = new StringBuilder(aaList.size());
			for (NadjenaAA aa : aaList) {
				cacheProt.append(aa.aa);
			}
		}
		return cacheProt.toString();
	}

	public boolean isEmpty() {
		return aaList.isEmpty();
	}

	/**
	 * Provjerava dali sadrzi lista aa. Ovo postoji zato sto originalna metoda
	 * od containt() koristi komparator koji je dodat u listu zbog sortiranja
	 * prema masi, pa krivo vraca
	 * 
	 * @param aa
	 * @return
	 */
	public boolean sadrziLista(NadjenaAA aa) {
		for (NadjenaAA l : aaList) {
			if (l.equals(aa)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Ne radi dobro!!!
	 * 
	 * @return
	 */
	public int sumPeaksHeight() {
		int h = 0;

		int lastRightHeight = 0;
		for (NadjenaAA aa : aaList) {
			aa.setMinMaxPeak();

			if (aa.peak1.isDummy()) {
				// l
				if (aa.peak1.getCentroidMass() == 0) {
					// nulti peak u polzitivu
					h += aa.peak1.getHeight();
					continue;
				}
				log.warn("Nebi se trebalo pojaviti!");
				continue;
			}

			if (aa.peak2.isDummy()) {
				// ponoviti
				h += aa.peak1.getHeight();
				continue;
			}

			if (ProteinsUtils.get().beatween(248.9, aa.peak1.getCentroidMass(), 0.5)) {

				// dodati zadnji ljevi jos jedamput
				h += aa.peak2.getHeight();

				continue;
			}
			// b.append(aa.aa).append(" " + aa.peak1.getHeight()).append(" ");
			h += aa.peak1.getHeight();
			lastRightHeight = aa.peak2.getHeight();
		}

		// b.append("Rihgh "+lastRightHeight);
		h += lastRightHeight;

		// log.debug(getProtein() + "\n"+ h + " "+b);
		return h;
	}

	/**
	 * Vrasa sumu mase svih pikova
	 * 
	 * @return
	 */
	public double sumPeaksMass() {
		double h = 0;
		for (NadjenaAA aa : aaList) {
			h += aa.peak1.getCentroidMass() + aa.peak2.getCentroidMass();
		}
		return h;
	}

	/**
	 * Vraca sumu mase svih aa u peptidu + voda 18.01
	 * 
	 * @return
	 */
	public double sumPeptideMass() {
		double h = 0;
		for (NadjenaAA aa : aaList) {
			h += ProteinsUtils.get().getMassFromAA(aa.aa);
		}
		return h + 18.010565;
	}

	/**
	 * Ovo je masa koja se nalazi u bazi! Ovu masu uzima kao sto i inace racuna
	 * masu peptida drugi libovi. Novo brzo izracunavanje mase + voda 18.01
	 * 
	 * @param peptide
	 * @return
	 */
	public static float calculateMassWidthH2O(String peptide) {
		final ProteinsUtils proteinsUtils = ProteinsUtils.get();
		float h = 0;
		for (int i = 0; i < peptide.length(); i++) {
			h += proteinsUtils.getMassFromAA(peptide.charAt(i));
		}
		// voda
		return h + H2O;
	}

	public static final float H2O = 18.01056F; // 18.010576

	/**
	 * Novo brzo izracunavanje mase
	 * 
	 * @param peptide
	 * @return
	 */
	public static final double calculateMass(String peptide) {
		double h = 0;
		final ProteinsUtils proteinsUtils = ProteinsUtils.get();
		for (int i = 0; i < peptide.length(); i++) {
			h += proteinsUtils.getMassFromAA(peptide.charAt(i));
		}
		// voda
		return h;
	}

	@Override
	public String toString() {
		return getPeptide() + " score:" + sumPeaksHeight();
	}

	/**
	 * Brise sve aa.
	 */
	public void clearAllaa() {
		this.aaList.clear();
		cacheProt = null;
	}

	public void removeAA(NadjenaAA nadjenaAA) {
		if (!this.aaList.remove(nadjenaAA)) {
			log.warn("Nisam obrisao aa {}", nadjenaAA);
		}
		cacheProt = null;
	}

}

interface SerializableComparator<T> extends Comparator<T>, Serializable {

}