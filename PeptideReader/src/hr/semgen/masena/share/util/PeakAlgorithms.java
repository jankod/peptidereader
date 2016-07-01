package hr.semgen.masena.share.util;

import hr.semgen.masena.share.model.Peak;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Staticne metode za rad sa pikovima.
 * 
 * @author tag
 * 
 */
public class PeakAlgorithms {
	private static final Logger log = LoggerFactory.getLogger(PeakAlgorithms.class);

	/**
	 * -> X: input (masa) Vraca najveci pik jednak ili manji od X Note: ako
	 * takav peak ne postoji, vraca najblizi desni primjer: mase:
	 * [10,20,30,40,50], trazimo 5 => vraca pos[10] = 0 (uvijek ce biti 0 u ovom
	 * slucaju)
	 * 
	 * @param sortedPeaks
	 * @param fromMass
	 * @return
	 */
	public static final int getHighestPeakByMass(Peak[] sortedPeaks, double fromMass) {
		int pos = binSearchLow0(sortedPeaks, fromMass) - 1;
		// note: vraca 0 ako je fromMass manji od najmanjeg
		if (pos < 0)
			pos = 0;
		return pos;
	}

	/**
	 * Vraca pik koji je najblizi (po masi) zadanoj masi. note: trazi sa obje
	 * strane (lijevo i desno)
	 * 
	 * @param sortedPeaks
	 *            pikovi sortirani po masi.
	 * @param mass
	 *            masa po kojoj se trazi pik
	 * @return najblizi pik. note: ako je udaljenost do dva najbliza peaka
	 *         jednaka, uzimamo veci: -> npr: trazimo 5, peaks su [m=6, h=10],
	 *         [m=4, h=20] -> vraca [m=4,h=20] -> ako su jednako visoki, vracamo
	 *         lijevog [m=6,h=10] i [m=4,h=10], vracamo [m=4,h=10] note2:
	 *         numerical error moze uzrokovati cudno ponasanje peaks koji su
	 *         tocno u sredini (jer u praksi nisu tocno u sredini)
	 */
	public static Peak getClosestPeakByMass(Peak[] sortedPeaks, double mass) {
		// prepare result
		Peak resultPeak = null;
		// find closest peak:
		int startPos = getHighestPeakByMass(sortedPeaks, mass);
		// trivial: binary search returned last peak
		if (startPos == sortedPeaks.length - 1) {
			resultPeak = sortedPeaks[sortedPeaks.length - 1];
			// otherwise: search for closest peak
		} else {

			double deltaLeft = Math.abs(mass - sortedPeaks[startPos].getCentroidMass());
			double deltaRight = Math.abs(mass - sortedPeaks[startPos + 1].getCentroidMass());

			final int comp = Double.compare(deltaLeft, deltaRight);

			if (comp < 0)
				resultPeak = sortedPeaks[startPos];
			else if (comp > 0)
				resultPeak = sortedPeaks[startPos + 1];
			else if (comp == 0) {
				if (sortedPeaks[startPos].getHeight() > sortedPeaks[startPos + 1].getHeight())
					resultPeak = sortedPeaks[startPos];
				else if (sortedPeaks[startPos].getHeight() < sortedPeaks[startPos + 1].getHeight())
					resultPeak = sortedPeaks[startPos + 1];
				else if (sortedPeaks[startPos].getHeight() == sortedPeaks[startPos + 1].getHeight())
					resultPeak = sortedPeaks[startPos];
			}
		}
		return resultPeak;
	}

	/**
	 * Vraca niz pikova koji imaju masu vecu ili jednaku od leftMass i manju ili
	 * jednaku od rightMass.
	 * 
	 * @param sortedPeaks
	 * @param leftMass
	 * @param rightMass
	 * @return niz pikova
	 */
	public final static Peak[] getRange(Peak[] sortedPeaks, double leftMass, double rightMass) {
		int startPos = getHighestPeakByMass(sortedPeaks, leftMass);
		if (startPos != sortedPeaks.length - 1 && leftMass > sortedPeaks[startPos].getCentroidMass())
			startPos++;
		int endPos = getHighestPeakByMass(sortedPeaks, rightMass);
		if (rightMass >= sortedPeaks[endPos].getCentroidMass())
			endPos++;
		if (leftMass > rightMass)
			throw new RuntimeException("Exception: left > right! : " + leftMass + " > " + rightMass);
		return Arrays.copyOfRange(sortedPeaks, startPos, endPos);
	}

	// =====================================================================================
	// ============================POMOCNE_METODE===========================================
	// =====================================================================================

	/**
	 * Pomocna metoda as binary search low, but includes equality in search
	 * (mass == peak mass)
	 * 
	 * @param peaks
	 * @param searchMassLow
	 * @return
	 */
	private final static int binSearchLow0(Peak[] peaks, double searchMassLow) {
		int begin = 0;
		int end = peaks.length;
		int mid = 0;

		while (end > begin) {
			// mid = (begin + end) / 2;
			mid = (begin + end) >>> 1;
			if (peaks[mid].getCentroidMass() <= searchMassLow) {
				begin = mid + 1;
			} else {
				end = mid;
			}
		}
		return begin;
	}
}
