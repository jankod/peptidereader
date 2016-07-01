package hr.semgen.masena.share.model;

import hr.semgen.masena.share.TwoValue;
import hr.semgen.masena.share.util.ProteinsUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * predstavlja jedan t2d file MSMS jedan ili MS jedan
 * 
 * @author janko
 * 
 */
public class PeakFile implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean positive = false;

	private List<Peak> allPeaks;
	private String name;

	private Float massPrecursor;
	private Date dateCreated;

	final public String msmsID;

	
	private MSMStype type = MSMStype.CAFF;
	// private LinkedList<FoundedPeptideState> foundedPeptidePeaks = new
	// LinkedList<FoundedPeptideState>();

	private List<NadjenaAA> nadjeniProteini = new LinkedList<NadjenaAA>();

	// Prije je bio hashSET
	private List<PeakFile> msmsChilds = new ArrayList<PeakFile>();

	private PeakFile parentMS = null;

	/**
	 * Ako je ovo derivat peak file od negog durgo nastao, tj. otvoren na double
	 * clik algoritam rezultat
	 */
	private PeakFile derivateOfPeakFile = null;

	/**
	 * Dali je ovo msms ili ms
	 */
	private boolean MS = false;

	private static Integer counter = 1;

	/**
	 * Novi long ID. Za stare podatke (serijalizirane objekte) nece biti kreiran
	 * pa ga treba preko getter kreirati.
	 */
	private Long id = null;

	public PeakFile() {
		dateCreated = new Date();
		id = createNewSpectarID();
		msmsID = RandomStringUtils.randomAlphanumeric(16) + counter++ + System.currentTimeMillis();
	}

	// private long createLongID() {
	// return UUID.randomUUID().getLeastSignificantBits();
	// // return (RandomUtils.nextLong() + counter++ +
	// // System.currentTimeMillis());
	// }

	public static long createNewSpectarID() {
		return UUID.randomUUID().getLeastSignificantBits();
		// return (RandomUtils.nextLong() + counter++ +
		// System.currentTimeMillis());
	}

	/**
	 * Vraca sve mase i visine ovog MS ili MSMS-a kao array double i int
	 * sortirane po masi.
	 */
	public TwoValue<float[], int[]> getMassesAndHeightAsArray() {
		ProteinsUtils.get().sortByMass(allPeaks);
		float[] masses = new float[allPeaks.size()];
		int[] height = new int[allPeaks.size()];
		int pos = 0;
		for (Peak p : allPeaks) {
			masses[pos] = Double.valueOf(p.getCentroidMass()).floatValue();
			height[pos] = p.getHeight();
		}

		TwoValue<float[], int[]> result = new TwoValue<float[], int[]>(masses, height);
		return result;
	}

	/**
	 * Novi long ID. Prije se koristio String ID <code>msmsID</code>.
	 * 
	 * @return long ID, ako je null zbog serijaliziranog objekta onda se napravi
	 *         novi ID.
	 */
	public long getId() {
		if (id == null) {
			// moze biti null ako je objekt od prije serijaliziran
			id = createNewSpectarID();
		}
		return id;
	}

	/**
	 * Svaki msmsID ima jedinstevni ID. Sada se koristi long <code>id</code>.
	 * 
	 * @return
	 */
	public String getMsmsID() {
		return msmsID;
	}

	/**
	 * kopija
	 * 
	 * @param peakFile
	 */
	public PeakFile(PeakFile peakFile) {
		this();
		this.allPeaks = new LinkedList<Peak>(peakFile.allPeaks);
		this.name = peakFile.name;
		this.setMassPrecursor(peakFile.getMassPrecursor());

	}

	@Override
	public String toString() {
		final String s = getName() + " " + (isMS() ? "MS " : "MS/MS ");
		if (!isMS()) {
			return s + (isPositive() ? " pos" : " neg");
		} else {
			return s;
		}
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (MS ? 1231 : 1237);
		result = prime * result + ((allPeaks == null) ? 0 : allPeaks.hashCode());
		result = prime * result + ((massPrecursor == null) ? 0 : massPrecursor.hashCode());
		result = prime * result + ((nadjeniProteini == null) ? 0 : nadjeniProteini.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parentMS == null) ? 0 : parentMS.hashCode());
		result = prime * result + (positive ? 1231 : 1237);
		return result;
	}

	private static final Logger log = LoggerFactory.getLogger(PeakFile.class);

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeakFile other = (PeakFile) obj;

		if (getId() == other.getId()) {
			return true;
		}

		if (MS != other.MS)
			return false;

		if (allPeaks == null) {
			if (other.allPeaks != null)
				return false;
		} else if (!allPeaks.equals(other.allPeaks))
			return false;
		if (massPrecursor == null) {
			if (other.massPrecursor != null)
				return false;
		} else if (!massPrecursor.equals(other.massPrecursor))
			return false;
		if (nadjeniProteini == null) {
			if (other.nadjeniProteini != null)
				return false;
		} else if (!nadjeniProteini.equals(other.nadjeniProteini))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parentMS == null) {
			if (other.parentMS != null)
				return false;
		} else if (!parentMS.equals(other.parentMS))
			return false;
		if (positive != other.positive)
			return false;
		return true;
	}

	/**
	 * Dodaje novi peak medu pikove
	 * 
	 * @param p
	 */
	public void addNewPeak(Peak p) {
		allPeaks.add(p);
		totalHeightCache = -11;
	}

	public void setAllPeaks(List<Peak> allPeaks) {
		this.allPeaks = allPeaks;
		totalHeightCache = -11;
	}

	public List<Peak> getAllPeaks() {
		if (allPeaks == null) {
			allPeaks = new LinkedList<Peak>();
		}
		return allPeaks;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * Vraca ime sa + ili - preznakom prije.
	 * 
	 * @return
	 */
	public String getNameWithSign() {
		if (isPositive()) {
			return "+" + name;
		}
		return "-" + name;
	}

	public void setMassPrecursor(Float massPrecursor) {
		this.massPrecursor = massPrecursor;
	}

	public Float getMassPrecursor() {
		return massPrecursor;
	}

	public Float getMassPrecursorMinusCAF() {
		return massPrecursor - 247.9445f;
	}

	/**
	 * Masa -247.9445
	 * 
	 * @return
	 */
	public double getMassPrecursorWithoutU() {
		return getMassPrecursorMinusCAF();
	}

	public void setNadjeniProteini(List<NadjenaAA> nadjeniProteini) {
		this.nadjeniProteini = nadjeniProteini;
	}

	public List<NadjenaAA> getNadjeniProteini() {
		if (nadjeniProteini == null) {
			nadjeniProteini = new LinkedList<NadjenaAA>();
		}
		return nadjeniProteini;
	}

	/**
	 * Da ne poziva izracun stalno isti.
	 */
	private float totalHeightCache = -11;

	/**
	 * Zbraja sve pikove, prvog i zadnjeg necu za sada u negativu. U pozitivu:
	 * mici samo desni dummy.
	 * 
	 * @return
	 */
	public float calculateTotalHeight() {
		if (totalHeightCache > 0) {
			return totalHeightCache;
		}
		float res = 0;
		boolean isFirst = true;
		ProteinsUtils.get().sortByMass(allPeaks);

		for (Peak p : allPeaks) {
			if (!positive && isFirst) {
				isFirst = false;
				continue;
			}
			if (!p.isDummy()) {
				res += p.getHeight();
			}
		}
		totalHeightCache = res;
		return res;
	}

	public void addMsmsChild(PeakFile msmsFile) {
		getMsmsChilds().add(msmsFile);
	}

	public Collection<PeakFile> getMsmsChilds() {
		return msmsChilds;
	}

	/**
	 * 
	 * @return new arraylist
	 */
	public List<PeakFile> getMsmsChildsList() {
		return new ArrayList<PeakFile>(getMsmsChilds());
	}

	public void setMS(boolean mS) {
		MS = mS;
	}

	public boolean isMS() {
		return MS;
	}

	public void setParentMS(PeakFile parentMS) {
		this.parentMS = parentMS;
	}

	public PeakFile getParentMS() {
		return parentMS;
	}

	public boolean removeChildMSMS(PeakFile forRemovePeak) {
		// posto mi equals ili hashcode ne rade dobro, moram rucno brisati
		for (PeakFile o : msmsChilds) {
			if (o == forRemovePeak) {
				return msmsChilds.remove(o);
			}
		}
		return msmsChilds.remove(forRemovePeak);

	}

	public void setDerivateOfPeakFile(PeakFile derivateOfPeakFile) {
		this.derivateOfPeakFile = derivateOfPeakFile;
	}

	public PeakFile getDerivateOfPeakFile() {
		return derivateOfPeakFile;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
	}

	public boolean isPositive() {
		return positive;
	}

	public boolean isNegative() {
		return !positive;
	}

	/**
	 * Gleda dali je isto ime i isti type: pos neg
	 * 
	 * @param peakFile
	 * @return
	 */
	public boolean containChildWithName(PeakFile peakFile) {
		for (PeakFile msms : getMsmsChilds()) {
			if (msms.getName().equals(peakFile.getName()) && (msms.isPositive() == peakFile.isPositive())) {
				return true;
			}
		}
		return false;
	}

	public MSMStype getType() {
		return type;
	}
	
	public void setType(MSMStype type) {
		this.type = type;
	}
	
	
}
