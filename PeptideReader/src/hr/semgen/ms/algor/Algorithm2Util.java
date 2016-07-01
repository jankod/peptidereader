package hr.semgen.ms.algor;

import hr.semgen.masena.share.model.BrotherPeakFile;
import hr.semgen.masena.share.model.PeakFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Algorithm2 je nova pobolsana oznaka za nove algoritme koji ce biti testirani.
 * @author tag
 * 
 */
public class Algorithm2Util {

	
	/**
	 * Kreira bracu od MSMS-a.
	 * @param ms
	 * @return
	 */
	public final static ArrayList<BrotherPeakFile> createBrother(PeakFile ms) {
		if (!ms.isMS()) {
			throw new RuntimeException("Nije ms!");
		}

		Map<Float, BrotherPeakFile> precursorBrotherMap = new HashMap<Float, BrotherPeakFile>();
		final List<PeakFile> msmsChilds = ms.getMsmsChildsList();
		Collections.sort(msmsChilds, new Comparator<PeakFile>() {

			public int compare(PeakFile o1, PeakFile o2) {
				return o1.getMassPrecursor().compareTo(o2.getMassPrecursor());
			}
		});

		for (PeakFile msms : msmsChilds) {

			final Float massPrecursor = msms.getMassPrecursor();
			if (!precursorBrotherMap.containsKey(massPrecursor)) {
				precursorBrotherMap.put(massPrecursor, new BrotherPeakFile());
			}

			final BrotherPeakFile brother = precursorBrotherMap.get(massPrecursor);
			if (msms.isPositive()) {
				brother.setPos(msms);
			} else { // negative
				brother.setNeg(msms);
			}

		}

		return new ArrayList<BrotherPeakFile>(precursorBrotherMap.values());

	}
}
