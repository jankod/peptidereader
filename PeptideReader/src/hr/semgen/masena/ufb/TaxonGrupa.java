package hr.semgen.masena.ufb;

import hr.semgen.masena.share.util.CommonUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TaxonGrupa implements Serializable {
	private static final Logger log = LoggerFactory.getLogger(TaxonGrupa.class);

	private static final long serialVersionUID = 6563237663403431092L;
	protected String cacheText;

	/**
	 * Sluzi samo za exchangeProteinIfNeed() metodu.
	 */
	private Map<String, UFB_ProteinWrapper> cepanicaProteinMap = new HashMap<String, UFB_ProteinWrapper>(64);

	public int num = -1;

	// Set<UFB_Protein> proteins = new HashSet<UFB_Protein>();
	final public HashSet<UFB_ProteinWrapper> proteins = new HashSet<UFB_ProteinWrapper>(128);
	// public double relHtotal;

	// final public ArrayList<Serija> serije = new ArrayList<Serija>(5);

	private int SSniz = -111;

	private double sumRelHSS = -111;

	private int taxID;

	private String taxName;

	private double totalRelHcache = -111;

	public double zbrojMsMatchPercent = -111;

	public TaxonGrupa(int taxonID, String name) {
		taxID = taxonID;
		taxName = name;
	}

	public String getTaxName() {
		return taxName;
	}

	public int getTaxID() {
		return taxID;
	}

	/**
	 * Metoda koju treba ubrzati i izmjeniti!
	 * 
	 * @param protein
	 * @param msms
	 * @param ms
	 */
	// grupira se po sekvenci (moze biti ista ako je braca razlicita) kao i do
	// sada , i jos se uzima u obzir braca,
	// sto znaci da ista sekvenca iz pos i neg moze proci.
	// I onda racunamo Total rel. H od ovih koje uzimam u nizu sve, i po njima
	// sortiram.
	public void add(UFB_ProteinWrapper protein, UFB_MSMSresult msms, UFB_MSResult ms) {

		protein.msName = ms.name;
		protein.parentTaxonID = taxID;

		exchangeProteinIfNeed(protein);

		// cepanicaProteinMap.put(getKey(protein), protein);
	}

	//

	/**
	 * MS ID + MSMS ID
	 */
	private String getKey(UFB_ProteinWrapper protein) {
		return protein.parentMSMS.msmsID + protein.msName;
		// protein.protein.cepanica+
	}

	/**
	 * Suma svih reh H ali u SS samo.
	 * 
	 * @return
	 */
	public double countSummRelHshureShoot() {
		if (sumRelHSS < 0) {
			double sum = 0;

			for (UFB_ProteinWrapper p : proteins) {
				if (p.containShureShoot) {
					sum += p.relH;
				}
			}
			sumRelHSS = sum;
		}
		return sumRelHSS;
	}

	/**
	 * 
	 * @return true ako ijedan hit sadrzi SS
	 */
	public boolean containAnySS() {
		for (UFB_ProteinWrapper p : proteins) {
			if (p.containShureShoot) {
				return true;
			}
		}
		return false;
	}

	/**
	 * zbroj svih amino kis. Shure Shoot u nizu
	 * 
	 * @return
	 */
	public int countSumSSinSeries() {
		if (SSniz < 0) {
			int sum = 0;
			for (UFB_ProteinWrapper p : proteins) {
				if (p.containShureShoot) {
					sum += p.protein.uNizuAA;
				}
			}
			SSniz = sum;
		}
		return SSniz;
	}

	/**
	 * Suma svih rel h u taxon grupi
	 * 
	 * @return
	 */
	public double countTotalRelH() {
		if (totalRelHcache < 0) {
			double h = 0;
			for (UFB_ProteinWrapper prot : proteins) {
				h += prot.relH;
			}
			totalRelHcache = CommonUtils.roundToDecimals(h, 2);
		}
		return totalRelHcache;
	}

	/**
	 * Ako vec sadrzi protein sa istom cepanicm i predznak-om. Ako da onda gleda
	 * dali je relH bolji, u nizu bolji, ako da onda vraca true, tj. treba
	 * zamjeniti taj prot.
	 * 
	 * @param noviHit
	 */
	private void exchangeProteinIfNeed(UFB_ProteinWrapper noviHit) {

		final String noviProtKey = getKey(noviHit);
		UFB_ProteinWrapper stariHit = cepanicaProteinMap.get(noviProtKey);
		if (stariHit == null) {
			// if (!proteins.contains(noviProt)) {
			proteins.add(noviHit);
			cepanicaProteinMap.put(noviProtKey, noviHit);
			// }
		} else {
			// ako je isti precursor, a mora biti posto je isti MS_ID + MSMS_ID po key-u
			if (noviHit.parentMSMS.positive == stariHit.parentMSMS.positive) {
				// ako je isti predznak
				final int compareRelH = Double.compare(stariHit.relH, noviHit.relH);

				if (compareRelH < 0) { // stariProt je manji
					proteins.remove(stariHit);
					proteins.add(noviHit);
					cepanicaProteinMap.put(noviProtKey, noviHit);
				}

				if (compareRelH == 0) {
					// ista cepanica isti niz
					final int compareUnizu = Double.compare(stariHit.protein.uNizuAA,
							noviHit.protein.uNizuAA);

					if (compareUnizu < 0) { // relH vec ima u nizu.
						proteins.remove(stariHit);
						proteins.add(noviHit);
						cepanicaProteinMap.put(noviProtKey, noviHit);
					}

					// mozda je ista cepanica iz iste vrste.
//					if (compareUnizu == 0 && noviProt.protein.cepanica.equals(stariZaMaknutiProt.protein.cepanica)) {
//						ServerDubugUtil.write(noviProt, stariZaMaknutiProt);
//					}

				}
			}else {
				// STO JE S OVIME???? Ovo se ne izvrsava nikada.
				//log.warn("Ovo je: {} {}", stariHit, noviHit);
				
			}
		}
	}

	/**
	 * Uzima prvu rjec samo
	 * 
	 * @return
	 */
	public String getImeRoda() {
		String rod = "";
		for (int i = 0; i < taxName.length(); i++) {
			final char c = taxName.charAt(i);
			if (c == '\'' || c == '"' && i == 0) {
				continue;
			}
			if (Character.isWhitespace(c)) {
				break;
			}
			rod += c;
		}

		return rod;
	}

	/**
	 * Uzima prvu rjec i drugu.
	 * 
	 * @return
	 */
	public String getImeRoda2() {
		String rod = "";
		boolean firstWord = false;
		for (int i = 0; i < taxName.length(); i++) {
			final char c = taxName.charAt(i);
			if (c == '\'' || c == '"' && i == 0) {
				continue;
			}
			if (Character.isWhitespace(c)) {
				if (firstWord == false) {
					firstWord = true;
				} else {
					break;
				}
			}
			rod += c;
		}

		return rod;
	}

	@Override
	public String toString() {
		return taxName;
	}

	public int countUniqueProteins = 0;

	/**
	 * Vraca countShureshoot, countPogodjenihMSmatch
	 * 
	 * @return
	 */
	// public TwoValue<Integer, Integer> countShureShootAndMMcount() {
	// // if (countShureShoot == null) {
	// // nesmje se cache ovo je onda ne radi MS match podaci
	// return UFBTransformUtil.countShureShoot(this);
	// // }
	// // return countShureShoot;
	// }

}