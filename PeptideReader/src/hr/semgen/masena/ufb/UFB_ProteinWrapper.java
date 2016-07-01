package hr.semgen.masena.ufb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Formatter;

/**
 * Predstavlja jedan hit.
 * 
 * @author tag
 * 
 */
public class UFB_ProteinWrapper implements Serializable {
	private static final long serialVersionUID = 4563857885570594604L;

	public int parentTaxonID;

	final public UFB_Protein protein;

	final public UFB_MSMSresult parentMSMS;
	public double relH;
	public String msName;
	/**
	 * Akpo sadrzi shureshoot onda je true, tj ako ima brata sa istom cepanicom
	 */
	public boolean containShureShoot = false;

	/**
	 * MS match %
	 */
	public double msMatchPercent = -1;

	/**
	 * Sve Cepanice od ovog proteina, pune se samo kod ms match.
	 */
	public ArrayList<String> msMatchCepanice;

	/**
	 * Koliko je matchalo cepanica
	 */
	public int msCountMatch = -1;

	public int msTotalCount = -1;
	/**
	 * Ako je gel based
	 */
	public boolean gelBased = false;

	public String cacheText;

	/**
	 * koliko se puta ponavlja ovaj protein u drugim hitovima vrste. (uzima se
	 * za najveci GI, posto jedan hit ima vise GI-eva)
	 */
	public int ponavljanjeGIa = -1;
	/**
	 * Shure shoot
	 */
	public UFB_ProteinWrapper ss;

	
	/**
	 * Unique GI
	 */
	public boolean uniqueProtein;

	public UFB_ProteinWrapper(UFB_Protein prot, UFB_MSMSresult msmsResult) {
		protein = prot;
		parentMSMS = msmsResult;
	}

	@Override
	public boolean equals(Object obj) {
		return this.hashCode() == obj.hashCode();
	}

	public String formatedToString() {
		Formatter f = new Formatter();
		String res = f
				.format("%-4s %10s Niz %2d MSMS %6.2f%%  MS %5.2f%% [%7s]  ", msName.trim(), parentMSMS.name.trim(),
						protein.uNizuAA, protein.msmsMatchPercent, msMatchPercent, msCountMatch + "/" + msTotalCount)
				.out().toString();
		f.close();
		return res;
	}

	@Override
	public int hashCode() {
		return protein.hashCode();
	}

	@Override
	public String toString() {
		return parentMSMS.name +" "+ parentMSMS.positive + " " + protein.toString();
	}
}