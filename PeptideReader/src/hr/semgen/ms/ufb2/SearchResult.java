package hr.semgen.ms.ufb2;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import java.io.Serializable;
import java.util.Date;

public class SearchResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
//	private List<MS> workspaceList;
	
	
	private Date startDate;
	
	private long durrationMiles;
	
	
	public Date getStartDate() {
		return startDate;
	}
	
	public long getDurrationMiles() {
		return durrationMiles;
	}
	
	public TLongObjectHashMap<ProteinGI> getGiGlobalCache() {
		return giGlobalCache;
	}
	
	public TIntObjectHashMap<String> getTaxonGlobalCache() {
		return taxonGlobalCache;
	}
	/**
	 * Sadrzi gi ID => GI objekt
	 */
	private TLongObjectHashMap<ProteinGI> giGlobalCache = new TLongObjectHashMap<ProteinGI>();
	
	
	/**
	 * Sadrzi NCBI taxonomy ID => String taxonomy description
	 */
	private TIntObjectHashMap<String> taxonGlobalCache = new TIntObjectHashMap<String>();
	
	
}
