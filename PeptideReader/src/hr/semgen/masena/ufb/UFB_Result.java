package hr.semgen.masena.ufb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UFB_Result implements Serializable {
	private transient static final Logger log = LoggerFactory.getLogger(UFB_Result.class);

	private static final long serialVersionUID = 1;

	public ArrayList<UFB_MSResult> msResultsList = new ArrayList<UFB_MSResult>();

	public UFB_Params params;

	/**
	 * Datum kad je zavrsena pretraga, stvara se na klijentu
	 */
	public Date dateFinished;

	
	/**
	 * TaxID => desc
	 */
	public HashMap<Integer, byte[]> globalTaxMap = new HashMap<Integer, byte[]>();
	
	
	
	
	public void setTaxIDnameMap(ConcurrentHashMap<Integer, byte[]> globalTaxIDsname) {
		final Set<Entry<Integer, byte[]>> entrySet = globalTaxIDsname.entrySet();
		for (Entry<Integer, byte[]> entry : entrySet) {
			globalTaxMap.put(entry.getKey(), entry.getValue());
		}
		
	}

	public void appenedData(UFB_Result result) {
		this.msResultsList.addAll(result.msResultsList);
		Set<Entry<Integer, byte[]>> f = result.globalTaxMap.entrySet();
		for (Entry<Integer, byte[]> entry : f) {
			globalTaxMap.put(entry.getKey(), entry.getValue());
		}

	}

	public String getTaxonNameByID(int taxonID) {
		final byte[] bytes = globalTaxMap.get(taxonID);
		if(bytes == null) {
//			log.error("Nema taxid: "+ taxonID);
//			throw new RuntimeException("Nema taxid: "+ taxonID);
			return "???";
		}
		return new String (bytes, UFB_Protein.ascii );
	}

}
