package hr.semgen.masena.ufb;

import hr.semgen.masena.share.util.Ion;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Predstavlja cepanicu jedinstvenu za poz ili neg.
 * 
 * @author tag
 * 
 */
public final class UFB_Protein implements Serializable {
	public static final long serialVersionUID = -2081390158664739511L;

	/**
	 * Koristi se za prikaz u Jface tabeli samo
	 */
	public int cacheNum;

	public ArrayList<Ion> ions;
	public ArrayList<GI> gis = new ArrayList<GI>();
	public String cepanica;
	public final int uNizuAA;
	public double msmsMatchPercent;

	// public double msMatchPercent;
	public transient static final Charset ascii = Charset.forName("ASCII");

	// public UFB_Protein() {
	//
	// }

	public UFB_Protein(String cepanica, ArrayList<Ion> ions, double percentMatch, int uNizuAA) {
		this.cepanica = cepanica;
		this.ions = ions;
		this.msmsMatchPercent = percentMatch;
		this.uNizuAA = uNizuAA;
	}

	@Override
	public String toString() {
		return " match: " + msmsMatchPercent + "%  niz: " + uNizuAA + "  " + cepanica + "  ";
	}

}
