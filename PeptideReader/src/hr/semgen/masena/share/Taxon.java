package hr.semgen.masena.share;

import java.io.Serializable;


/**
 * Ime i taxID
 *
 */
public class Taxon implements Serializable, Comparable<Taxon> {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (taxonId ^ (taxonId >>> 32));
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
		Taxon other = (Taxon) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (taxonId != other.taxonId)
			return false;
		return true;
	}

	private static final long serialVersionUID = 1L;

	private int taxonId;
	private String name;

	public Taxon() {
	}
	
	@Override
	public String toString() {
		return name +  " "+ taxonId;
	}

	public Taxon(int taxonId, String name) {
		this.taxonId = taxonId;
		this.name = name;
	}

	public void setTaxonId(int taxonId) {
		this.taxonId = taxonId;
	}

	public int getTaxonId() {
		return taxonId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int compareTo(Taxon o) {
		return Integer.valueOf(taxonId).compareTo(o.taxonId);
	}

}
