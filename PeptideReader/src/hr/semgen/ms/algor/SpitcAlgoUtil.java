package hr.semgen.ms.algor;

import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.Peptid;
import hr.semgen.masena.share.util.ProteinsUtils;
import hr.semgen.masena.share.util.ProteinsUtils.SORT;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpitcAlgoUtil {
	private static final Logger log = LoggerFactory.getLogger(SpitcAlgoUtil.class);

	/**
	 * Radi sortiranu mapu.
	 * 
	 * @param peaks
	 * @return {@link TreeMap} gdje je masa pika key a pik value.
	 */
	public static TreeMap<Float, Peak> toTreeMap(List<Peak> peaks) {
		TreeMap<Float, Peak> peaksTree = new TreeMap<Float, Peak>();
		for (Peak peak : peaks) {
			peaksTree.put(peak.getCentroidMass(), peak);
		}
		return peaksTree;
	}

	/**
	 * Treba traziti sidro uljevo i udesno
	 * 
	 * @param peaks
	 * @param anchorLength
	 * @return
	 */
	public static Peptid findAnchor(ArrayList<Peak> peaks, int anchorLength) {
		TreeMap<Float, Peak> tree = toTreeMap(peaks);
		
		tree.firstEntry();
		
		
		
		return null;
	}

	/**
	 * Ostavlja samo najintenzivnje pikove u spektru.
	 * @param peaks
	 * @param howIntensivPeaks
	 */
	public static void useMostIntensivPeaks(List<Peak> peaks, int howIntensivPeaks) {
		ProteinsUtils.get().sortByHeight(peaks, SORT.VECI_PREMA_MANJEMU);
		List<Peak> result = new ArrayList<Peak>();
		for (int i = 0; i < Math.min(howIntensivPeaks, peaks.size()); i++) {
			result.add(peaks.get(i));
		}
		peaks.clear();
		peaks.addAll(result);
	}

	public static void removePercentLeftAndRight(List<Peak> peaks, float percent) {
		List<Peak> result = new ArrayList<Peak>(peaks.size());

		double ukupno = peaks.size();

		double min = (ukupno * percent / 100);
		double max = ukupno * (1d - percent / 100);

		int index = 0;
		TreeSet<Float> bigMassRemoved = new TreeSet<Float>();
		TreeSet<Float> lowMassRemoved = new TreeSet<Float>();
		for (Peak peak : peaks) {
			index++;
			if (index < min) {
				lowMassRemoved.add(peak.getCentroidMass());
				continue;
			}
			if (index > max) {
				bigMassRemoved.add(peak.getCentroidMass());
				continue;
			}

			result.add(peak);
		}

		peaks.clear();
		peaks.addAll(result);
	}

	public static List<Peptid> brancheAnchor(Peptid anchor, int numberOfBranch) {
		return null;
	}

}
