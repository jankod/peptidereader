package hr.semgen.masena.share.mph;


/**
 * Predlozena a.a. izmedju 2 pika.
 * @author tag
 *
 */
public class MphFoundedAA {

	/**
	 * a.a. uvjek je veliko slovo.
	 */
	private char aminoAcid;
	private MphPeak leftPeak;
	private MphPeak rightPeak;

	public MphFoundedAA() {
	}
	
	public MphFoundedAA(MphPeak left, MphPeak right, char aa) {
		if(left.getMass() > right.getMass()) {
			throw new RuntimeException("Veca je masa");
		}
		
		leftPeak = left;
		rightPeak = right;
		aminoAcid = aa;
	}
	
	@Override
	public String toString() {
		return "Founded aa: "+ aminoAcid + " range: "+ (rightPeak.getMass() - leftPeak.getMass()) + " right: "+ getRightPeak() + " left "+ getLeftPeak();
	}

	public char getAminoAcid() {
		return aminoAcid;
	}
	
	public void setAminoAcid(char aminoAcid) {
		this.aminoAcid = aminoAcid;
	}
	public MphPeak getLeftPeak() {
		return leftPeak;
	}
	public MphPeak getRightPeak() {
		return rightPeak;
	}
	
	public void setLeftPeak(MphPeak leftPeak) {
		this.leftPeak = leftPeak;
	}
	
	public void setRightPeak(MphPeak rightPeak) {
		this.rightPeak = rightPeak;
	}
}
