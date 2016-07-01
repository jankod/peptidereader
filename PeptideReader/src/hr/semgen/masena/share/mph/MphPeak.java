package hr.semgen.masena.share.mph;

import hr.semgen.masena.share.model.Peak;

import java.io.Serializable;

public class MphPeak implements Serializable, Comparable<MphPeak> {

	private static final long serialVersionUID = 6837816204401797135L;
	public float mass;
	public int height;

	public MphPeak() {
	}

	public MphPeak(Peak peak) {
		this.mass = peak.getCentroidMass();
		this.height = peak.getHeight();
	}

	public MphPeak(float mass, int height) {
		this.mass = mass;
		this.height = height;
		
	}
	
	@Override
	public String toString() {
		return "m: "+ mass + " h: "+ height;
	}

	/**
	 * Komparira po masi pikove.
	 */
	public final int compareTo(MphPeak o) {
		final float f1 = mass;
		final float f2 = o.mass;
		if (f1 < f2)
			return -1; // Neither val is NaN, thisVal is smaller
		if (f1 > f2)
			return 1; // Neither val is NaN, thisVal is larger

		int thisBits = Float.floatToIntBits(f1);
		int anotherBits = Float.floatToIntBits(f2);

		return (thisBits == anotherBits ? 0 : // Values are equal
				(thisBits < anotherBits ? -1 : // (-0.0, 0.0) or (!NaN, NaN)
						1)); // (0.0, -0.0) or (NaN, !NaN)
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + Float.floatToIntBits(mass);
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
		MphPeak other = (MphPeak) obj;
		if (height != other.height)
			return false;
		if (Float.floatToIntBits(mass) != Float.floatToIntBits(other.mass))
			return false;
		return true;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
