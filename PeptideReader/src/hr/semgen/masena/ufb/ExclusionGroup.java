package hr.semgen.masena.ufb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Sadrzi Peptide koje ne zelim u rezultatima
 * @author tag
 * 
 */
public class ExclusionGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	/**
	 * Cepanice koje su iskljucene
	 */
	private ArrayList<String> cepanice = new ArrayList<String>();

	/**
	 * ID od MSMS koji su iskljuceni
	 */
	private HashSet<Long> msmsIds = new HashSet<Long>();

	private long timeCreated;

	@Override
	public String toString() {
		return "Ex. Group " + name + "  peptides: " + cepanice.size() + " MSMS: "+ getMsmsIds().size();

	}
	public ExclusionGroup() {}
	public ExclusionGroup(String newGroupName) {
		this.name = newGroupName;
		this.timeCreated = System.currentTimeMillis();
	}

	public void setCepanice(ArrayList<String> cepanice) {
		this.cepanice = cepanice;
	}

	public ArrayList<String> getCepanice() {
		return cepanice;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setTimeCreated(long timeCreated) {
		this.timeCreated = timeCreated;
	}

	public long getTimeCreated() {
		return timeCreated;
	}

	public void addCepanica(String p) {
		if (p != null)
			getCepanice().add(p.toUpperCase());
	}

	public void removeCepanica(String pep) {
		if (pep != null)
			getCepanice().remove(pep.toUpperCase());
	}

	public void setMsmsIds(HashSet<Long> msmsIds) {
		if (msmsIds != null)
			this.msmsIds = msmsIds;
	}

	public HashSet<Long> getMsmsIds() {
		if (msmsIds == null) {
			msmsIds = new HashSet<Long>();
		}
		return msmsIds;
	}

	/**
	 * Vraca ime + broj selektiranice MSMS ova
	 * 
	 * @return
	 */
	public String getDescription() {
		if (msmsIds != null && msmsIds.isEmpty()) {
			return getName();
		}

		return getName() + "     excluded MSMS (" + msmsIds.size() + ")";
	}

	public void addMSMSID(long id) {
		getMsmsIds().add(id);
	}
	public void clearAllMSMS() {
		getMsmsIds().clear();
	}
}
