package hr.semgen.masena.share.prediction;

import java.io.Serializable;
import java.io.StringWriter;

public class GambCSV implements Serializable {
	private static final long serialVersionUID = 6933281771045844239L;
	
	float massPrecurosr;
	boolean isPositive;
	float[] mass;
	int[] intensities;
	String peptide;
	float[] aaMass;

	public GambCSV(float massPrecurosr, boolean isPositive, float[] mass, int[] intensities, String peptide,
			float[] aaMass) {
		super();
		this.massPrecurosr = massPrecurosr;
		this.isPositive = isPositive;
		this.mass = mass;
		this.intensities = intensities;
		this.peptide = peptide;
		this.aaMass = aaMass;
	}

	public String toCSV() {
		StringWriter buf = new StringWriter();
		// masa prekursora | poz/neg | mase | intenziteti | peptid | mase aa
		buf.write(massPrecurosr + "");
		buf.write("\t");
		buf.write(convertPositive(isPositive));
		buf.write("\t");
		buf.write(convertToMass(mass));
		buf.write("\t");
		buf.write(convertTointenzitete(intensities));
		buf.write("\t");
		buf.write(peptide);
		buf.write("\t");
		buf.write(convertToMass(aaMass));
		buf.write("\n");
		String result = buf.toString();
		return result;
	}

	private String convertToMass(float[] mass) {
		StringBuilder r = new StringBuilder(mass.length * 7);
		boolean isFirst = true;
		for (float m : mass) {
			if (!isFirst)
				r.append(", ");
			r.append(m);
			isFirst = false;
		}
		return r.toString();
	}

	private String convertTointenzitete(int[] inte) {
		StringBuilder r = new StringBuilder(inte.length * 3 + 7);
		boolean isFirst = true;
		for (int i : inte) {
			if (!isFirst)
				r.append(", ");
			r.append(i);
			isFirst = false;
		}
		return r.toString();
	}

	private String convertPositive(boolean positive) {
		if (positive) {
			return "+";
		} else {
			return "-";
		}
	}
}