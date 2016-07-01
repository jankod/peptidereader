package hr.semgen.masena.share.blast;

import java.io.Serializable;

/**
 * Parametri za searchnanje blasta
 * @author tag
 *
 */
public class MSSearchBlastParamModel implements Serializable {

	public static enum MATRIX {
		PAM30, PAM70, BLOSUM80, BLOSUM62, BLOSUM45;
	}

	public MATRIX[] getALLMatrix() {
		return MATRIX.values();
	}

	private static final long serialVersionUID = 1L;

	private float eValue = 300000;
	private MATRIX matrixOd4 = MATRIX.PAM30;
	private MATRIX matrixVeceOd4 = MATRIX.BLOSUM80;

	private float plusMinus = 0.3f;
	private int wordSize = 2;
	private int windowSiye = 40;
	private float threashold = 11;
	private int maxNumResultOd4 = 100000;
	private int maxNumResultVeceOd4 = 20000;
	
	
	private double plusMinusR_or_K = 0.5;

	private BlastInput.DB database = BlastInput.DB.NR;

	public double geteValue() {
		return eValue;
	}

	public void seteValue(float eValue) {
		this.eValue = eValue;
	}

	public float getPlusMinus() {
		return plusMinus;
	}

	public void setPlusMinus(float plusMinus) {
		this.plusMinus = plusMinus;
	}

	public int getWindowSize() {
		return windowSiye;
	}

	public void setWindowSiye(int windowSiye) {
		this.windowSiye = windowSiye;
	}

	public double getThreashold() {
		return threashold;
	}

	public void setThreashold(float threashold) {
		this.threashold = threashold;
	}

	public void setMaxNumResultOd4(int maxNumResultOd4) {
		this.maxNumResultOd4 = maxNumResultOd4;
	}

	public int getMaxNumResultOd4() {
		return maxNumResultOd4;
	}

	public void setMaxNumResultVeceOd4(int maxNumResultVeceOd4) {
		this.maxNumResultVeceOd4 = maxNumResultVeceOd4;
	}

	public int getMaxNumResultVeceOd4() {
		return maxNumResultVeceOd4;
	}

	public void setMatrixOd4(MATRIX matrixOd4) {
		this.matrixOd4 = matrixOd4;
	}

	public MATRIX getMatrixOd4() {
		return matrixOd4;
	}

	public void setMatrixVeceOd4(MATRIX matrixVeceOd4) {
		this.matrixVeceOd4 = matrixVeceOd4;
	}

	public MATRIX getMatrixVeceOd4() {
		return matrixVeceOd4;
	}

	public void setWordSize(int wordSize) {
		this.wordSize = wordSize;
	}

	public int getWordSize() {
		return wordSize;
	}

	public void setDatabase(BlastInput.DB database) {
		this.database = database;
	}

	public BlastInput.DB getDatabase() {
		return database;
	}

	public void setPlusMinusR_or_K(double plusMinusR_or_K) {
		this.plusMinusR_or_K = plusMinusR_or_K;
	}

	public double getPlusMinusR_or_K() {
		return plusMinusR_or_K;
	}

}
