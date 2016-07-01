package hr.semgen.masena.share.mph;

import hr.semgen.masena.share.model.PeptidDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server result za MPH za jedan MS
 * 
 * @author tag
 * 
 */
public class MphResult implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * TAX_ID => TAXON
	 */
	private ArrayList<MphTaxonGroup> taxons = new ArrayList<MphTaxonGroup>(54);

	/**
	 * SEQ_ID => Protein seq
	 */
	public Map<Integer, String> proteinMap;

	private Map<Long, List<PeptidDTO>> msmsIDpeptides;

	public MphResult() {

	}

	public Map<Long, List<PeptidDTO>> getMsmsIDpeptides() {
		return msmsIDpeptides;
	}

	public void setMSpeptides(Map<Long, MphMSPeptides> msPeptides) {
		int count = 0;
		for (Entry<Long, MphMSPeptides> entry : msPeptides.entrySet()) {
			count += entry.getValue().getMsmsIDpeptides().size();
		}
		msmsIDpeptides = new HashMap<Long, List<PeptidDTO>>(count);

		for (Entry<Long, MphMSPeptides> entry : msPeptides.entrySet()) {
			msmsIDpeptides.putAll(entry.getValue().getMsmsIDpeptides());
		}
	}
private static final Logger log = LoggerFactory.getLogger(MphResult.class);

	/**
	 * Moze se postaviti na null da hessian ne save na disk sve.
	 * 
	 * @param taxon
	 */
	public void setTaxon(Map<Integer, MphTaxonGroup> taxon) {
		if (taxon == null) {
			this.taxons = null;
			log.warn("Null je taxon!!!");
			return;
		}
		this.taxons.addAll(taxon.values());
	}

	/**
	 * TAX_ID => TAXON
	 * 
	 * @return
	 */
	public ArrayList<MphTaxonGroup> getTaxon() {
		return taxons;
	}

	public void prepare(Map<Long, Long> brotherMap, Map<Integer, String> seqMap) {
		for (MphTaxonGroup t : taxons) {
			t.prepare(brotherMap, seqMap);
			t.countSS();
		}

		Collections.sort(taxons, new Comparator<MphTaxonGroup>() {

			public int compare(MphTaxonGroup o1, MphTaxonGroup o2) {
				if (o1.sumIdentical < o2.sumIdentical) {
					return 1;
				} else if (o1.sumIdentical > o2.sumIdentical) {
					return -1;
				} else {
					return 0;
				}
			}

		});
		int num = 1;
		for (MphTaxonGroup t : taxons) {
			t.num = num++;
		}

	}

}
