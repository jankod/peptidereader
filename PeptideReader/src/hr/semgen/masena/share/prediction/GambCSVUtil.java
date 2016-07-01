package hr.semgen.masena.share.prediction;

import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.util.Ion;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class GambCSVUtil {

	public static String createGambCSV(final ArrayList<Ion> ions, final String peptide, final Float massPrecursor,
			final boolean isPositive, final List<Peak> allPeaks) {
		int[] intensi = new int[allPeaks.size()];
		float[] mase = new float[allPeaks.size()];
		int i = 0;
		for (Peak peak : allPeaks) {
			intensi[i] = peak.getHeight();
			mase[i] = peak.getCentroidMass();
			i++;
		}
		float[] aaMase = new float[ions.size()];
		i = 0;
		for (Ion ion : ions) {
			if (isPositive) {
				aaMase[i] = (float) ion.getMass();
			} else {
				aaMase[i] = (float) ion.getMassPlusCAF();
			}
			i++;
		}

		StringWriter buf = new StringWriter(4600);
		// masa prekursora | poz/neg | mase | intenziteti | peptid | mase aa
		buf.write(massPrecursor.toString());
		buf.write("\t");
		buf.write(convertPositive(isPositive));
		buf.write("\t");
		buf.write(convertToMass(mase));
		buf.write("\t");
		buf.write(convertTointenzitete(intensi));
		buf.write("\t");
		buf.write(peptide);
		buf.write("\t");
		buf.write(convertToMass(aaMase));
		return buf.toString();
	}

	private static String convertToMass(float[] mass) {
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

	private static String convertTointenzitete(int[] inte) {
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

	private static String convertPositive(boolean positive) {
		if (positive) {
			return "+";
		} else {
			return "-";
		}
	}
}
