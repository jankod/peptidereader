package hr.semgen.ms.ufb2;

import hr.semgen.masena.share.model.PeakLite;

import java.io.Serializable;

/**
 * Ion Y ili B koji se metha na MSMS.
 * 
 * @author tag
 * 
 */
public final class IonMatch implements Serializable, Comparable<IonMatch> {

	private static final long serialVersionUID = 2837303317667704675L;

	public static enum Type {
		/**
		 * koriste se kod negativa
		 */
		B,

		/**
		 * Koriste se kod pozitiva
		 */
		Y
	}

	/**
	 * Kakav je match sa ionima bio.
	 * 
	 * @author tag
	 * 
	 */
	public static enum MatchType {
		NORMAL_MATCH, // 1
		K_OR_R_PLUS_H2O, // 2
		S_OR_T_WITHOUT_H2O, // 3
		IN_HOLE,		// 4
		K_OR_R_MINUS_H2O

	}

	public final static MatchType getByRankoBroj(int rankoBroj) {
		switch (rankoBroj) {
		case 1:
			return MatchType.NORMAL_MATCH;
		case 2:
			return MatchType.K_OR_R_PLUS_H2O;
		case 3:
			return MatchType.S_OR_T_WITHOUT_H2O;
		case 4:
			return MatchType.IN_HOLE;
		case 5:
			return MatchType.K_OR_R_MINUS_H2O;
		default:
			throw new RuntimeException("Nije dobar rankoBroj: " + rankoBroj);
		}
	}

	private MatchType matchType = null;

	/**
	 * Peak koji je najblizi b ionu.
	 */
	// private Peak closestPeak;

	private final char aa;
	private final float mass;
	private final short position;
	private boolean match = false;

	/**
	 * 
	 */
	private float closestPeakMass;
	private int closestPeakHeight;

	/**
	 * 
	 * @param aa
	 * @param mass
	 * @param position
	 *            mora pocinjati od 1, da bi radio BionUtils.match tocno
	 */
	public IonMatch(final char aa, final float mass, final short position) {
		this.aa = aa;
		this.mass = mass;
		this.position = position;
	}

	/**
	 * 
	 * @param aa
	 * @param massTotal
	 * @param position
	 *            mora pocinjati od 1, da bi radio BionUtils.match tocno
	 */
	public IonMatch(String aa, float mass, short position) {
		if (aa.length() != 1) {
			throw new RuntimeException("a.a. nije 1 char: " + aa);
		}
		this.aa = aa.charAt(0);
		this.mass = mass;
		this.position = position;
	}

	public char getAA() {
		return aa;
	}

	public double getMass() {
		return mass;
	}

	public float getMassPlusCAF() {
		return mass + 247.9445F;
	}

	public int getPosition() {
		return position;
	}

	@Override
	public String toString() {
		if (closestPeakMass != 0) {
			return "Ion: " + aa + " mass: " + mass + " match: " + isMatch() + "     " + matchType + "  Height:"
					+ closestPeakMass;
		}

		return "Ion: " + aa + " mass: " + mass + " match: " + isMatch() + "     " + matchType;
	}

	public int compareTo(final IonMatch o) {
		return position < o.position ? -1 : (position == o.position ? 0 : 1);
	}

	public void setMatch(final boolean match) {
		this.match = match;
	}

	/**
	 * Dali se matcha sa peakom, korist se kod MSMS match.
	 * 
	 * @return
	 */
	public boolean isMatch() {
		return match;
	}

	/**
	 * Najveci H
	 * 
	 * @return
	 */
	public PeakLite getClosestPeakLite() {
		return new PeakLite(closestPeakHeight, closestPeakMass);
	}

	public void setClosestPeakHeight(int closestPeakHeight) {
		this.closestPeakHeight = closestPeakHeight;
	}

	public int getClosestPeakHeight() {
		return closestPeakHeight;
	}

	public void setClosestPeakMass(float closestPeakMass) {
		this.closestPeakMass = closestPeakMass;
	}

	public float getClosestPeakMass() {
		return closestPeakMass;
	}

	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public String getMatchTypeString() {
		if (matchType == MatchType.NORMAL_MATCH) {
			return "Normal";
		}
		if (matchType == MatchType.K_OR_R_PLUS_H2O) {
			return "K or R + H20";
		}
		if (matchType == MatchType.IN_HOLE) {
			return "In hole";
		}
		if (matchType == MatchType.S_OR_T_WITHOUT_H2O) {
			return "S or T - H20";
		}
		return " - ";
	}
}
