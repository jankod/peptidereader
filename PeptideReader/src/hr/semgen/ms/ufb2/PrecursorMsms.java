package hr.semgen.ms.ufb2;

import java.io.Serializable;

/**
 * Predstavlja jedan par brace, tj. za jedan precursor pozitive i negativ.
 * 
 */
public class PrecursorMsms implements Serializable {
	private static final long serialVersionUID = 1L;

	private float precursorMass;

	private Msms msmsPositive;
	private Msms msmsNegative;
	
	
//	
	
	
	public PrecursorMsms(float mass) {
		precursorMass = mass;
	}

	public void addMsms(Msms msms) {
		if (msms.isPositive()) {
			msmsPositive = msms;
		} else {
			msmsNegative = msms;
		}
	}

	// =====================================================================================
	// ============================GETTER_AND_SETTER========================================
	// =====================================================================================

	public float getPrecursorMass() {
		return precursorMass;
	}

	public void setPrecursorMass(float precursorMass) {
		this.precursorMass = precursorMass;
	}

	public Msms getMsmsPositive() {
		return msmsPositive;
	}

	public void setMsmsPositive(Msms msmsPositive) {
		this.msmsPositive = msmsPositive;
	}

	public Msms getMsmsNegative() {
		return msmsNegative;
	}

	public void setMsmsNegative(Msms msmsNegative) {
		this.msmsNegative = msmsNegative;
	}

}
