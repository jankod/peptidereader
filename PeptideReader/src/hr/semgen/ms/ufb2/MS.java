package hr.semgen.ms.ufb2;

import hr.semgen.masena.share.TwoValue;
import hr.semgen.masena.share.model.PeakFile;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Lightway MS klasa za brzi prijenost podataka. MS je uvjek negativ. Value
 * objekt. Ljepe se i rezultati matchanja na nju. Sadrzi i rezultate mathanja
 */
public class MS implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private float[] masses;
	private int[] height;

	private Map<Float, PrecursorMsms> precursorMsmsMap;

	/**
	 * Default konstruktor ako ce trebati za neku seriajlizaciju.
	 */
	public MS() {
	}

	/**
	 * @param id
	 * @param masses
	 * @param height
	 */
	public MS(long id, float[] masses, int[] height) {
		this.id = id;
		this.masses = masses;
		this.height = height;
	}

	/**
	 * Konstruktor za kopiranje. Uzima i MSMS childove.
	 * 
	 * @param ms
	 */
	public MS(PeakFile ms) {
		this.id = ms.getId();
		final TwoValue<float[], int[]> twoValueMS = ms.getMassesAndHeightAsArray();
		this.masses = twoValueMS.getT1();
		this.height = twoValueMS.getT2();

		final Collection<PeakFile> msmsChilds = ms.getMsmsChilds();
		if (msmsChilds != null) {

			for (PeakFile msmsPeakFile : msmsChilds) {
				PrecursorMsms precursor = this.getOrCrreatePrecursor(msmsPeakFile.getMassPrecursor());

				Msms msmsLight = new Msms(msmsPeakFile);
				precursor.addMsms(msmsLight);
			}
		}
	}

	private PrecursorMsms getOrCrreatePrecursor(Float massPrecursor) {
		final float floatMass = massPrecursor.floatValue();
		if (precursorMsmsMap == null) {
			precursorMsmsMap = new HashMap<Float, PrecursorMsms>();
			PrecursorMsms newPreMsms = new PrecursorMsms(floatMass);
			return newPreMsms;
		}

		if (precursorMsmsMap.containsKey(floatMass)) {
			return precursorMsmsMap.get(floatMass);
		} else {
			PrecursorMsms msms = new PrecursorMsms(floatMass);
			precursorMsmsMap.put(floatMass, msms);
			return msms;
		}
	}

	public int getPeaksSize() {
		if (masses == null) {
			return 0;
		}
		return masses.length;
	}

	public float getMass(int pos) {
		return masses[pos];
	}

	public int getHeght(int pos) {
		return height[pos];
	}

	// =====================================================================================
	// ============================GETTER_AND_SETTER========================================
	// =====================================================================================

	
	public Map<Float, PrecursorMsms> getPrecursorMsmsMap() {
		return precursorMsmsMap;
	}
	/**
	 * MS ID koji ima i PeakFile
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float[] getMasses() {
		return masses;
	}

	public void setMasses(float[] masses) {
		this.masses = masses;
	}

	public int[] getHeight() {
		return height;
	}

	public void setHeight(int[] height) {
		this.height = height;
	}

}
