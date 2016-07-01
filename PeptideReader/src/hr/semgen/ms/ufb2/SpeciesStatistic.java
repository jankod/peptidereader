package hr.semgen.ms.ufb2;

import java.io.Serializable;

/**
 * Parametri izracunati za jednu vrsti.
 * 
 */
public class SpeciesStatistic implements Serializable {
	private static final long serialVersionUID = 6689296236916112473L;

	/**
	 * Suma SS (shure shoot) u nizu.
	 */
	private int ssSeriesSum;

	/**
	 * Suma RH (relativ height) hitova. Zaukruzena na dvije decimale.
	 */
	private float rhSum;

	/**
	 * Suma RH (relative height) SS (shure shoot-ova) hitova. Zaokruzena na
	 * dvije decimale.
	 */
	private float rnSSSum;

	/**
	 * Suma SS (shure shoot) hitova,
	 */
	private int ssSum;

	/**
	 * Suma MS match hitova. Zaokruzena na dvije decimale.
	 */
	private float mmSum;

	/**
	 * Suma pogodakaka MM (MS match) hitova.
	 */
	private int mmHitSum;

	/**
	 * Defaultni konstruktor. Ne radi nista za sada.
	 */
	public SpeciesStatistic() {
	}

	// =====================================================================================
	// ============================GETTER_AND_SETTERS=======================================
	// =====================================================================================

	public int getSsSeriesSum() {
		return ssSeriesSum;
	}

	public void setSsSeriesSum(int ssSeriesSum) {
		this.ssSeriesSum = ssSeriesSum;
	}

	public float getRhSum() {
		return rhSum;
	}

	public void setRhSum(float rhSum) {
		this.rhSum = rhSum;
	}

	public float getRnSSSum() {
		return rnSSSum;
	}

	public void setRnSSSum(float rnSSSum) {
		this.rnSSSum = rnSSSum;
	}

	public int getSsSum() {
		return ssSum;
	}

	public void setSsSum(int ssSum) {
		this.ssSum = ssSum;
	}

	public float getMmSum() {
		return mmSum;
	}

	public void setMmSum(float mmSum) {
		this.mmSum = mmSum;
	}

	public int getMmHitSum() {
		return mmHitSum;
	}

	public void setMmHitSum(int mmHitSum) {
		this.mmHitSum = mmHitSum;
	}

}
