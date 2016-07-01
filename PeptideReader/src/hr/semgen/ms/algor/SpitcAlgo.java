package hr.semgen.ms.algor;

import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.Peptid;
import hr.semgen.masena.share.util.ProteinsUtils;
import hr.semgen.masena.share.util.ProteinsUtils.SORT;

import java.util.ArrayList;
import java.util.List;

public class SpitcAlgo implements IAlgorithm {

	private float percentDisgardedPeaksFromMargin;
	private int anchorLength;
	private int useMostIntensivPeaks;
	private int numberOfBranch;

	public SpitcAlgo(int anchorLength, float percentDisgardedPeaksFromMargin, int useMostIntensivPeaks,
			int numberOfBranch) {
		this.anchorLength = anchorLength;
		this.percentDisgardedPeaksFromMargin = percentDisgardedPeaksFromMargin;
		this.useMostIntensivPeaks = useMostIntensivPeaks;
		this.numberOfBranch = numberOfBranch;

	}

	public List<Peptid> run(List<Peak> peaksInput, float massPrecursor, double delta) {
		ArrayList<Peak> peaks = new ArrayList<Peak>(peaksInput);
		ProteinsUtils.get().sortByMass(peaks, SORT.MANJI_PREMA_VECEMU);

		// makni postotak ljevih i desnih
		SpitcAlgoUtil.removePercentLeftAndRight(peaks, percentDisgardedPeaksFromMargin);

		// onda uzmi X najvisljih pikova
		SpitcAlgoUtil.useMostIntensivPeaks(peaks, useMostIntensivPeaks);

		// medju tim pikovima trazi sidro duzine X
		Peptid anchor = SpitcAlgoUtil.findAnchor(peaks, anchorLength);

		SpitcAlgoUtil.brancheAnchor(anchor, numberOfBranch);

		return null;
	}

	// private Peak getMostHeightPeak(float peakMass, float delta) {
	// final SortedMap<Float, Peak> subMap = peaks.subMap(peakMass - delta,
	// peakMass + delta);
	// if (subMap.isEmpty()) {
	// return null;
	// }
	// final Collection<Peak> values = subMap.values();
	// Peak heigestPeak = null;
	// int maxH = Integer.MIN_VALUE;
	// for (Peak peak : values) {
	// if (peak.getHeight() > maxH) {
	// heigestPeak = peak;
	// }
	// }
	// return heigestPeak;
	// }
}
