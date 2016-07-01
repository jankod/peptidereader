package hr.semgen.masena.share.model;

import java.io.Serializable;



public class NadjenaAA implements Serializable {
	private static final long serialVersionUID = 1L;
	public String aa;
	public Peak peak1;
	public Peak peak2;

	public NadjenaAA() {
	}

	public NadjenaAA(Peak p1, Peak p2, String aa) {
		
		// ovo je vazno jer se ostali oslanjaju na pravilnost da je peak1 manje mase od peak2, tj. da je ljevi.
		this.peak1 = min(p1, p2);
		this.peak2 = max(p1, p2);
		this.aa = aa;

	}

	private Peak max(Peak p1, Peak p2) {
		if(p1.getCentroidMass() > p2.getCentroidMass()) {
			return p1;
		}
		return p2;
	}

	private Peak min(Peak p1, Peak p2) {
		if(p1.getCentroidMass() < p2.getCentroidMass()) {
			return p1;
		}
		return p2;
	}

	@Override
	public String toString() {
		return aa + "["+ peak1 + " "+ peak2 +"]";
	}

	/**
	 * U hashCode i equals ide samo peak1 i peak2, ne i aa, jer moze user odrediti drugacju aa, za odredjene pikove,
	 * np I umjesto L. Pa poslje hashMap u Grafu, nemoze naci to.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((peak1 == null) ? 0 : peak1.hashCode());
		result = prime * result + ((peak2 == null) ? 0 : peak2.hashCode());
		return result;
	}

	/**
	 * Jednako je ako je jednaki pikovi.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NadjenaAA other = (NadjenaAA) obj;
		if (peak1 == null) {
			if (other.peak1 != null)
				return false;
		} else if (!peak1.equals(other.peak1))
			return false;
		if (peak2 == null) {
			if (other.peak2 != null)
				return false;
		} else if (!peak2.equals(other.peak2))
			return false;
		return true;
	}

	/**
	 * Provjerava dali je peak1 manji od peak2 te setupira ih.
	 */
	public void setMinMaxPeak() {
		Peak p1 = this.peak1;
		Peak p2 = this.peak2;
		this.peak1 = min(p1, p2);
		this.peak2 = max(p1, p2);
	}
}
