package hr.semgen.masena.share.mph;

import static hr.semgen.masena.share.mph.MphConstants.H2O;
import static hr.semgen.masena.share.mph.MphConstants.K;
import static hr.semgen.masena.share.mph.MphConstants.R;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pomocna klasa za izracunavanje.
 * 
 * @author tag
 * 
 */
public final class MphCalc {
	private static final Logger log = LoggerFactory.getLogger(MphCalc.class);

	/**
	 * <b>Ovo je nova verzija: ProteinCalculation.findLastRorKaa(originalPeaks,
	 * dev);.</b><br>
	 * 
	 * Trazi zadnju R ili K aa ako postoji i ako nadje vraca nadjenu aa.
	 * 
	 * Dakle zadnja amino kiselina mora biti ili K ili R, ako postoje obje
	 * mogucnosti (da mu odgovara i 146,10516 i 174,1131, OVO JE S VODOM
	 * VJEROJATNO) on uvijek mora izabrati vecu vrijednost po intenzitetu pika.
	 * 
	 * @param peaks
	 * @param precursorMass
	 * @param deltaPlusMinusR_or_K
	 * @return pronadjeni K ili R, ili null ako nis ne nadje.
	 */
	public static MphFoundedAA findLastRorK(TreeMap<Float, MphPeak> peaks, float precursorMass,
			float deltaPlusMinusR_or_K) {
		// double dev = Activator.getPreferencePlusMinusR_or_K();
		if (peaks.isEmpty()) {
			return null;
		}

		final float K_H2O = K + H2O;
		final float R_H2O = R + H2O;

		// zadnji mora biti masa prekursora
		MphPeak peakPrecursor = peaks.lastEntry().getValue();

		// zadnji mora biti isti kao i masaPrekusora
		if (peakPrecursor.mass != precursorMass) {
			throw new RuntimeException("Masa zadnjeg nije jednaka masi prekursora");
		}

		MphFoundedAA lastK = null;
		MphFoundedAA lastR = null;

		// TODO: dodati da trazi i bez H2O, pa da vidimo sto cemo dobiti.

		{ // racun za K
			float left = precursorMass - K_H2O - deltaPlusMinusR_or_K;
			float right = precursorMass - K_H2O + deltaPlusMinusR_or_K;
			// log.debug("left {} right {}", left, right);
			char foundedAA = 'K';

			final NavigableMap<Float, MphPeak> subMap = peaks.subMap(left, true, right, true);
			// log.debug(subMap.toString());
			if (!subMap.isEmpty()) {
				// uzmi onog sa najvecom masom ili sa najblizim pikom
				MphPeak peakLeft = findMostHeightPeak(subMap.values());
				lastK = new MphFoundedAA(peakLeft, peakPrecursor, foundedAA);
			}

		}
		{// racun za R
			float left = precursorMass - R_H2O - deltaPlusMinusR_or_K;
			float right = precursorMass - R_H2O + deltaPlusMinusR_or_K;
			char foundedAA = 'R';
			// log.debug("Za R ... left {} right {}", left, right);
			final NavigableMap<Float, MphPeak> subMap = peaks.subMap(left, true, right, true);
			// log.debug(subMap.toString());
			if (!subMap.isEmpty()) {
				// uzmi onog sa najvecom masom ili sa najblizim pikom
				MphPeak peakLeft = findMostHeightPeak(subMap.values());
				lastR = new MphFoundedAA(peakLeft, peakPrecursor, foundedAA);
			}
		}

		if (lastK != null && lastR != null) {
			// ako postoji i R i K onda vrati onaj sa ljevim vecim H pikom
			if (lastK.getLeftPeak().getHeight() > lastR.getLeftPeak().getHeight()) {
				return lastK;
			} else {
				return lastR;
			}
		}
		if (lastK != null) {
			return lastK;
		}
		if (lastR != null) {
			return lastR;
		}

		return null;
	}

	/**
	 * Vraca pik sa najvecim height.
	 * 
	 * @param peaks
	 * @return
	 */
	public static MphPeak findMostHeightPeak(Collection<MphPeak> peaks) {
		if (peaks.isEmpty()) {
			throw new RuntimeException("nema pikova");
		}
		MphPeak heigestPeak = null;
		for (MphPeak entry : peaks) {
			if (heigestPeak == null) {
				heigestPeak = entry;
				continue;
			}

			if (heigestPeak.getHeight() < entry.getHeight()) {
				heigestPeak = entry;
			}
		}
		return heigestPeak;
	}

}
