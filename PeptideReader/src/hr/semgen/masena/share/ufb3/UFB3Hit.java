package hr.semgen.masena.share.ufb3;

import hr.semgen.masena.share.util.Ion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class UFB3Hit implements Serializable {

	private static final long serialVersionUID = 1L;
	private long msmsID;
	private int niz;
	private float msmsMatchPercent;
	private int repGI;
	private ArrayList<Ion> ions;
	private boolean uniqueGI;

	/**
	 * GI-evi ovog taxona
	 */
	private long[] gis;
	private boolean containShureShoot;

	@Override
	public String toString() {
		return createPeptideString() + " " + msmsMatchPercent + "% niz=" + niz;
	}

	public UFB3Hit() {
	}

	/**
	 * Vraca jedan GI i to manji.
	 * 
	 * @return
	 */
	public long getMinGi() {
		long minGi = Long.MAX_VALUE;
		for (long gi : gis) {
			minGi = Math.min(gi, minGi);
		}
		return minGi;
	}

	public float getMsmsMatchPercent() {
		return msmsMatchPercent;
	}

	public void setMsmsMatchPercent(float msmsMatchPercent) {
		this.msmsMatchPercent = msmsMatchPercent;
	}

	public long getMsmsID() {
		return msmsID;
	}

	public void setMsmsID(long msmsID) {
		this.msmsID = msmsID;
	}

	public int getNiz() {
		return niz;
	}

	public void setNiz(int niz) {
		this.niz = niz;
	}

	public int getRepGI() {
		return repGI;
	}

	public void setRepGI(int repGI) {
		this.repGI = repGI;
	}

	public ArrayList<Ion> getIons() {
		return ions;
	}

	public void setIons(ArrayList<Ion> ions) {
		this.ions = ions;
	}

	public boolean isUniqueGI() {
		return uniqueGI;
	}

	public void setUniqueGI(boolean uniqueGI) {
		this.uniqueGI = uniqueGI;
	}

	public long[] getGis() {
		return gis;
	}

	public void setGis(long[] gis) {
		this.gis = gis;
	}

	public String createPeptideString() {
		StringBuilder p = new StringBuilder(ions.size());
		for (Ion ion : ions) {
			p.append(ion.getAA());
		}
		return p.toString();
	}

	public void setContainShureShoot(boolean containShureShoot) {
		this.containShureShoot = containShureShoot;
	}

	public boolean isContainShureShoot() {
		return containShureShoot;
	}

	
	class GI2 {
		public long gi;
		public int brojPonavljanja;
	}
	/**
	 * Trazi najveci broj ponavljanja odredenih peptida unutar vise gi-jeva i u
	 * slucaju jednakog broja pogodaka odabire najmanji gi od liste razlicitih
	 * gi-jeva sa jednakim brojem pogodaka.
	 * 
	 * @param otherHitFromThisTaxon
	 * @return
	 */
	public long getBestGI(Collection<UFB3Hit> otherHitFromThisTaxon) {
		long bestGI = Long.MAX_VALUE;
		int bestBrojPonavljanja = 0;
		final long[] gis = getGis();
		for (long gi : gis) {
			int brojPonavljanja = findBrojPonavljanjaUtaxonGrupi(gi, otherHitFromThisTaxon);
			if(brojPonavljanja > bestBrojPonavljanja) {
				bestGI = gi;
				bestBrojPonavljanja = brojPonavljanja;
				continue;
			}
			
			if(brojPonavljanja == bestBrojPonavljanja) {
				bestGI = Math.min(gi, bestGI);
			}
			
		}
		
		return bestGI;
	}

	/**
	 * Trazi koliko se puta ovaj gi pojavljuje u taxonu cijelom
	 * 
	 * @param gi
	 * @param selectedTaxonGroupa
	 * @return
	 */
	public static int findBrojPonavljanjaUtaxonGrupi(long gi, Collection<UFB3Hit> otherHitFromThisTaxon) {
		int c = 0;
		for (UFB3Hit h : otherHitFromThisTaxon) {
			final long[] gis = h.getGis();
			for (long otherGi : gis) {
				if (otherGi == gi) {
					c++;
				}
			}
		}
		return c;

	}

}
