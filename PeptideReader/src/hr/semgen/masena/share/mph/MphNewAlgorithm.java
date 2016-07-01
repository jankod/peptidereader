package hr.semgen.masena.share.mph;

import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.Peptid;
import hr.semgen.ms.algor.IAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class MphNewAlgorithm implements IAlgorithm {

	public List<Peptid> run(List<Peak> peaksList, float massPrecursor, double devPlusMinus) {
		TreeMap<Float, MphPeak> peaks = new TreeMap<Float, MphPeak>();
		for (Peak peak : peaksList) {
			peaks.put(peak.getCentroidMass(), new MphPeak(peak));
		}

		return calc(peaks, massPrecursor, devPlusMinus);
		
	}

	private List<Peptid> calc(TreeMap<Float, MphPeak> peaks, float massPrecursor, double devPlusMinus) {
		ArrayList<Peptid> results = new ArrayList<Peptid>();
	
		
		final Peptid pep = new Peptid();
		results.add(pep);
		return null;
	}

}
