package hr.semgen.masena.share.util;

import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.PeakLite;

import java.io.Serializable;

public final class Ion implements Serializable, Comparable<Ion> {

	private static final long serialVersionUID = -7614597137868307849L;

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
		IN_HOLE,	// 4
		K_OR_R_MINUS_H2O // 5

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
			throw new RuntimeException("Nije dobar rankoBroj: "+ rankoBroj);
		}
	}

	private MatchType matchType = null;


	private  char aa;
	private double mass;
	private int position;
	private boolean match = false;
	private PeakLite closestPeakLite;

	
	public Ion() {
	}
	
	/**
	 * 
	 * @param aa
	 * @param mass
	 * @param position
	 *            mora pocinjati od 1, da bi radio BionUtils.match tocno
	 */
	public Ion(final char aa, final double mass, final int position) {
		this.aa = aa;
		this.mass = CommonUtils.roundToDecimals(mass, 5);
		this.position = position;
	}

	/**
	 * 
	 * @param aa
	 * @param massTotal
	 * @param position
	 *            mora pocinjati od 1, da bi radio BionUtils.match tocno
	 */
	public Ion(String aa, double massTotal, int position) {
		if (aa.length() != 1) {
			throw new RuntimeException("a.a. nije 1 char: " + aa);
		}
		this.aa = aa.charAt(0);
		this.mass = CommonUtils.roundToDecimals(massTotal, 5);
		this.position = position;
	}

	
	public void setAA(char aa) {
		this.aa = aa;
	}
	public char getAA() {
		return aa;
	}

	
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	public double getMass() {
		return mass;
	}

	public double getMassPlusCAF() {
		return mass + ProteinConstants.CAFF;
	}
	
	public double getMassPlusH3() {
		return mass + ProteinConstants.H3_NEW;
	}
	
	public double getMassPlusH4() {
		return mass + ProteinConstants.H4;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	@Override
	public String toString() {
		if(closestPeakLite != null)  {
			return  "Ion: " + aa + " mass: " + mass + " match: " + isMatch() + "     " + matchType + "  Height:"+ closestPeakLite.getHeight() + " pos: "+ position;
		}
		
		return  "Ion: " + aa + " mass: " + mass + " match: " + isMatch() + "     " + matchType + " pos: "+ position;
	}

	public int compareTo(final Ion o) {
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
		return closestPeakLite;
	}

	/**
	 * Najveci H
	 * 
	 * @param closestPeak
	 */
	public void setClosestPeak(final Peak closestPeak) {
		this.closestPeakLite = new PeakLite(closestPeak);
	}
	
	public void setClosestPeakLite(PeakLite closestPeakLite) {
		this.closestPeakLite = closestPeakLite;
	}

	public Peak getClosestPeak() {
		Peak p = new Peak(closestPeakLite.getCentroidMass(), closestPeakLite.getHeight());
		return p;
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
		if(matchType == MatchType.K_OR_R_MINUS_H2O) {
			return "K or R - H2O";
		}
		return " - ";
	}

	
}