package hr.semgen.ms.ufb2;

import hr.semgen.masena.share.TwoValue;
import hr.semgen.masena.share.model.PeakFile;

import java.io.Serializable;

/**
 * Lightway MSMS
 * 
 */
public class Msms implements Serializable {

	private static final long serialVersionUID = -989116263659174634L;

	/**
	 * ID ovog MSMS-a
	 */
	private long id;

	private float[] masses;
	private int[] height;

	/**
	 * Ako je pozitiv onda je ovo true, inace false
	 */
	private boolean positive;

	/**
	 * Konstruktor koji popunjava sve parametre klase.
	 * 
	 * @param id
	 *            ID od MSMS
	 * @param parentMSid
	 *            ID od parend MS
	 * @param masses
	 * @param height
	 * @param brotherID
	 *            ID od brata
	 * @param positive
	 *            dali je pozitiv ili negativ
	 */
	public Msms(long id, float[] masses, int[] height, boolean positive) {
		this.id = id;
		this.masses = masses;
		this.height = height;
		this.positive = positive;
	}

	public Msms(PeakFile msmsPeakFile) {
		this.id = msmsPeakFile.getId();
		this.positive = msmsPeakFile.isPositive();
		final TwoValue<float[], int[]> arr = msmsPeakFile.getMassesAndHeightAsArray();
		this.masses = arr.getT1();
		this.height = arr.getT2();
	}

	
	
	// =====================================================================================
	// ============================GETTER_AND_SETTER========================================
	// =====================================================================================

	/**
	 * @see <code>this.id</code>
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

	public boolean isPositive() {
		return positive;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
	}

}
