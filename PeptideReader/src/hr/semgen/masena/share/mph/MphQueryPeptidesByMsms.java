package hr.semgen.masena.share.mph;

import hr.semgen.masena.share.model.PeakFile;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class MphQueryPeptidesByMsms implements Serializable{

	private static final long serialVersionUID = 1L;

	private float precursorMass;

	private PeakFile negativMsms;
	private PeakFile positivieMsms;

	private HashSet<String> queryPeptidesNegative = new HashSet<String>();
	private HashSet<String> queryPeptidesPositive = new HashSet<String>();

	public void addNegativePeptides(List<String> peptides) {
		prepareAndAdd(peptides, queryPeptidesNegative);

	}

	public void addPositivePeptides(List<String> peptides) {
		prepareAndAdd(peptides, queryPeptidesPositive);
	}

	/**
	 * Mice manje od 4 aa query-e.
	 * 
	 * @param pep
	 * @return
	 */
	public boolean isQueryPeptideOK(String pep) {
		if (pep.length() < 4) {
			return false;
		}
		return true;
	}

	/**
	 * Mica manje od 4 aa, sve L u I, i sve Q u K!
	 * 
	 * @param peptides
	 * @param queryPeptides
	 */
	private void prepareAndAdd(List<String> peptides, Set<String> queryPeptides) {
		for (String peptid : peptides) {
			String pep = peptid;
			pep = StringUtils.replaceChars(pep, "L", "I");
			pep = StringUtils.replaceChars(pep, "Q", "K");

			if (isQueryPeptideOK(pep))
				queryPeptides.add(pep);
		}
	}

	public HashSet<String> getQueryPeptidesNegative() {
		return queryPeptidesNegative;
	}

	public HashSet<String> getQueryPeptidesPositive() {
		return queryPeptidesPositive;
	}

	public void setPrecursorMass(float precursorMass) {
		this.precursorMass = precursorMass;
	}

	public float getPrecursorMass() {
		return precursorMass;
	}

	public void setNegativMsms(PeakFile negativMsms) {
		this.negativMsms = negativMsms;
	}

	public PeakFile getNegativMsms() {
		return negativMsms;
	}

	public void setPositivieMsms(PeakFile positivieMsms) {
		this.positivieMsms = positivieMsms;
	}

	public PeakFile getPositivieMsms() {
		return positivieMsms;
	}
}
