package hr.semgen.masena.share.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppModel implements Serializable {
	private static final Logger log = LoggerFactory.getLogger(AppModel.class);
	private static final long serialVersionUID = 1L;

	private AppModel() {
	}

	private Set<PeakFile> peakFiles = new HashSet<PeakFile>();

	private transient static AppModel instance;

	public static final synchronized AppModel get() {
		if (instance == null) {
			instance = new AppModel();
		}
		return instance;
	}

	/**
	 * Vraca sve peakove bez MS parenta i sve MS-ove. Ne i one pikove koji se
	 * nalaze unitar MS-a.
	 * 
	 * @return
	 */
	public Set<PeakFile> getPeakFiles() {
		return peakFiles;
	}

	/**
	 * Postavlja novi app model
	 * 
	 * @param appModel
	 */
	public void replaceData(AppModel appModel) {
		instance = appModel;
	}

	/**
	 * Trazi peakfile koji je equals as p (ime i mass precursor)
	 * 
	 * @param p
	 * @return
	 */
	public PeakFile findRealPeakFile(PeakFile p) {
		for (PeakFile thisP : peakFiles) {
			if (thisP.equals(p)) {
				return thisP;
			}
			if (thisP.isMS()) {
				List<PeakFile> msmsChildsList = thisP.getMsmsChildsList();
				for (PeakFile peakFile : msmsChildsList) {
					if (peakFile.equals(p)) {
						return peakFile;
					}
				}
			}
		}

		return null;
	}

	public Set<PeakFile> getAllMS() {
		return getAllMSPeakFiles();
	}

	/**
	 * Vraca sve MS ovog workspace.
	 * 
	 * @return
	 */
	public Set<PeakFile> getAllMSPeakFiles() {
		Set<PeakFile> ms = new HashSet<PeakFile>();
		for (PeakFile p : peakFiles) {
			if (p.isMS())
				ms.add(p);
		}
		return ms;
	}

	/**
	 * Treba isprazniti kod switchanja. Prazni ApplicationWorkbench
	 */
	private static Map<String, PeakFile> idMSMSglobalCacheMap = new HashMap<String, PeakFile>();

	/**
	 * 
	 * @return hashMap IDmsms => MSMS (PeakFile)
	 */
	public static Map<String, PeakFile> getMapIDmsms() {
		if (idMSMSglobalCacheMap.isEmpty()) {
			createNewMSMScache_Map();
		}
		return idMSMSglobalCacheMap;
	}

	public static void createNewMSMScache_Map() {
		idMSMSglobalCacheMap.clear();
		final Set<PeakFile> allMSPeakFiles = AppModel.get().getAllMSPeakFiles();
		for (PeakFile ms : allMSPeakFiles) {
			for (PeakFile msms : ms.getMsmsChilds()) {
				idMSMSglobalCacheMap.put(msms.msmsID, msms);
			}
		}
	}

	/**
	 * Treba isprazniti kod switchanja. Prazni ApplicationWorkbench
	 */
	private static Map<Long, PeakFile> idLongMSMSglobalCacheMap = new HashMap<Long, PeakFile>(48);

	public Map<Long, PeakFile> getLongMapIDmsms() {
		if (idLongMSMSglobalCacheMap.isEmpty()) {
			createNewLongMSMScache_Map();
		}
		return idLongMSMSglobalCacheMap;
	}

	public static void createNewLongMSMScache_Map() {
		idLongMSMSglobalCacheMap.clear();
		final Set<PeakFile> allMSPeakFiles = AppModel.get().getAllMSPeakFiles();
		for (PeakFile ms : allMSPeakFiles) {
			for (PeakFile msms : ms.getMsmsChilds()) {
				// log.debug("msms id {}", msms.getId());
				idLongMSMSglobalCacheMap.put(msms.getId(), msms);
			}
		}
		// log.debug("Sada mapa ima MSMS-ova {}",
		// idLongMSMSglobalCacheMap.size());
	}

	/**
	 * Vraca true ako posto peakfile sa ovim name i precusorom, bio on ms ili
	 * msms ?? MS nema prekursora??
	 * 
	 * @param name
	 * @param massPrecursor
	 * @return
	 */
	public boolean existNameAndPrecursor(String name, Float massPrecursor) {
		Set<PeakFile> peakFiles = AppModel.get().getPeakFiles();
		for (PeakFile peakFile : peakFiles) {
			if (name.equals(peakFile.getName()) && massPrecursor.equals(peakFile.getMassPrecursor())) {
				return true;
			}
			if (peakFile.isMS()) {
				List<PeakFile> msmsChildsList = peakFile.getMsmsChildsList();
				for (PeakFile msms : msmsChildsList) {
					if (name.equals(msms.getName()) && massPrecursor.equals(msms.getMassPrecursor())) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean existMSname(String name) {
		Set<PeakFile> peakFiles = AppModel.get().getPeakFiles();
		for (PeakFile peakFile : peakFiles) {
			if (peakFile.isMS() && peakFile.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public void addMS(PeakFile ms) {
		if (!getPeakFiles().add(ms)) {
			log.warn("Nisam dodao MS: " + ms);
		}
	}

	public PeakFile getMSbyName(String msNameParent) {
		Set<PeakFile> peakFiles = AppModel.get().getPeakFiles();
		for (PeakFile peakFile : peakFiles) {
			if (peakFile.isMS() && peakFile.getName().equalsIgnoreCase(msNameParent)) {
				return peakFile;
			}
		}
		return null;
	}

	/**
	 * Postavlja prazne podatke, kada se change workspace dir bez podatak..
	 */
	public void setEmptyData() {
		peakFiles = new HashSet<PeakFile>();
	}

	public AppModel createNewAppModel() {
		return new AppModel();
	}

	public void removeMS(PeakFile[] msArr) {
		for (PeakFile peakFileMS : msArr) {

			if (!peakFiles.remove(peakFileMS)) {
				log.error("nisam maknuo ms " + peakFileMS);
				throw new RuntimeException("nisam maknuo");
			}
		}
	}

	/**
	 * Posto su u SET-u ne mice ih dobro jer hashcode gleda vjerojatno
	 * hashCode() od msms-a
	 * 
	 * @param array
	 */
	public void removeMSByID(PeakFile[] msArrZaMaknuti) {
		ArrayList<PeakFile> ostaviti = new ArrayList<PeakFile>();
		for (PeakFile msZaMaknuti : msArrZaMaknuti) {

			for (PeakFile msIn : peakFiles) {
				if (msZaMaknuti.getId() == msIn.getId()) {
					// maknuti
				} else {
					// ostaviti
					ostaviti.add(msIn);
				}
			}
		}
		peakFiles.clear();
		peakFiles.addAll(ostaviti);
	}

	/**
	 * Ako je ovo spitc
	 */
	private boolean isSpitc = false;

	public void setSpitc(boolean isSpitc) {
		this.isSpitc = isSpitc;
	}

	public boolean isSpitc() {
		return isSpitc;
	}

	public boolean isESI() {

		return isESI;
	}

	public void setESI(boolean isESI) {
		this.isESI = isESI;
	}

	private boolean isESI = false;

}
