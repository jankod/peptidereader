package hr.semgen.masena.share.ufb3;

import hr.semgen.masena.share.TwoValue;
import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.PeakFile;
import hr.semgen.masena.share.util.CommonUtils;
import hr.semgen.masena.share.util.Ion;
import hr.semgen.masena.share.util.PeptideUtils;
import hr.semgen.masena.share.util.SpringStopWatch;
import hr.semgen.masena.ufb.GI;
import hr.semgen.masena.ufb.TaxonGrupa;
import hr.semgen.masena.ufb.UFB_MSMSresult;
import hr.semgen.masena.ufb.UFB_MSResult;
import hr.semgen.masena.ufb.UFB_Protein;
import hr.semgen.masena.ufb.UFB_ProteinWrapper;
import hr.semgen.masena.ufb.UFB_Result;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.primitives.Floats;

public class UFB3TransformUtil {

	public static String getRelIntezitet(Ion ion, UFB_Protein prot, double totalHeight) {

		return CommonUtils.roundToDecimals(ion.getClosestPeak().getHeight() / totalHeight, 4) + "";
	}

	/**
	 * Trazi samo po imenu i da nisu predznaci isti. Vjerojatno nije bas tocna
	 * metoda.
	 * 
	 * @param msms
	 * @return null ako ne nadje nis
	 */
	public static UFB_MSMSresult findBrotherByName(UFB_MSMSresult msms, UFB_Result result) {
		for (UFB_MSResult ms : result.msResultsList) {
			if (ms.msmsList.contains(msms)) {

				final ArrayList<UFB_MSMSresult> msmsList = ms.msmsList;
				for (UFB_MSMSresult msms2 : msmsList) {
					if (msms2.name.equals(msms.name) && msms.positive != msms2.positive) {
						return msms2;
					}
				}

				return null;
			}

		}
		return msms;
	}

	public static UFB_MSMSresult findMSMS(UFB_Protein prot, UFB_Result result) {
		for (UFB_MSResult ms : result.msResultsList) {
			final ArrayList<UFB_MSMSresult> msmsList = ms.msmsList;
			for (UFB_MSMSresult ufb_MSMSresult : msmsList) {
				if (ufb_MSMSresult.foundedProteins.contains(prot)) {
					return ufb_MSMSresult;
				}
			}
		}
		return null;
	}

	/**
	 * Trazi dali u listi ima neki msms sa istom masom prekursora i sa
	 * razlicitim predznakom.
	 * 
	 * @param msmsList
	 * @param msms
	 * @return vraca null ako ne nadje nis.
	 */
	public static PeakFile findBrother(Collection<PeakFile> msmsList, PeakFile msms) {
		for (PeakFile peakFile : msmsList) {
			if (peakFile.getMassPrecursor() == msms.getMassPrecursor() && peakFile.isPositive() != msms.isPositive()) {
				return peakFile;
			}
		}
		return null;
	}

	public static int sortirajProteine(UFB_Protein p1, UFB_Protein p2, PeakFile msms) {
		if (p1.uNizuAA > p2.uNizuAA) {

			return -1;
		}
		if (p1.uNizuAA < p2.uNizuAA) {
			return 1;
		}

		if (p1.uNizuAA == p2.uNizuAA) {

			if (p1.msmsMatchPercent > p2.msmsMatchPercent) {
				return -1;
			}
			if (p1.msmsMatchPercent < p2.msmsMatchPercent) {
				return 1;
			}

			// FIXME: ovdje matchaj po MS match

			// final PeakFile msms =
			// idMSMSglobalCacheMap.get(getSelectedMSMS().msmsID);
			double rh1 = PeptideUtils.realtiveHeightIntensity(msms, p1.ions);
			double rh2 = PeptideUtils.realtiveHeightIntensity(msms, p2.ions);

			if (rh1 > rh2) {
				return -1;
			}
			if (rh1 < rh2) {
				return 1;
			}
		}
		try {
			// ako su ovdje isti onda po imenu sortiraj
			final int res = p1.gis.get(0).getDesc().compareTo(p2.gis.get(0).getDesc());
			if (res == 0) {
				// onda neka ljevi bude prvi
				return -1;
			}
			return res;
		} catch (NullPointerException e) {
			// ovo se dogodi kada desc nije zadan
			return 0;
		}
	}

	/**
	 * Vraca razliku izmedju closest pika i iona.
	 * 
	 * @param b
	 * @param isPositive
	 * @return
	 */
	public static String getDeltaPeak(Ion b, boolean isPositive) {
		if (b.getClosestPeak() == null) {
			return "&nbsp";
		}

		if (isPositive) {
			double abs = Math.abs(b.getMass() - b.getClosestPeak().getCentroidMass());
			if (b.getClosestPeak().getCentroidMass() < b.getMass()) {
				abs *= -1;
			}
			return CommonUtils.roundToDecimals(abs, 4) + "";
		}

		double abs = Math.abs(b.getMassPlusCAF() - b.getClosestPeak().getCentroidMass());
		if (b.getClosestPeak().getCentroidMass() < b.getMassPlusCAF()) {
			abs *= -1;
		}
		return CommonUtils.roundToDecimals(abs, 4) + "";
	}

	public static void main(String[] args) throws IOException {
		SpringStopWatch s = SpringStopWatch.startNew("deserijalizacija");

		// od workspace nova bica
		File f1 = new File("C:\\tmp\\serijaliziraniRezultati\\idMSMS_PeakFileMap1265332389650521725.hess");
		File f2 = new File("C:/tmp/serijaliziraniRezultati/msResultOnly1353542176957623590.hess");
		File f3 = new File("c:/tmp/serijaliziraniRezultati/result1284220345242734215.hess");

		// od workspace 2 MS perfomance
		// f1 = new File("C:\\tmp\\idMSMS_PeakFileMap.hess");
		// f2 = new File("C:/tmp/msResultOnly.hess");
		// f3 = new File("c:/tmp/result.hess");

		Map<String, PeakFile> idMSMS_PeakFileMap = (Map<String, PeakFile>) HessianUtilShare.deserialize(f1
				.getAbsolutePath());

		UFB_MSResult msResultOnly = (UFB_MSResult) HessianUtilShare.deserialize(f2.getAbsolutePath());

		UFB_Result result = (UFB_Result) HessianUtilShare.deserialize(f3.getAbsolutePath());
		// long s1 = ObjectSizer.getObjectGraphSize(result);
		// long s2 = ObjectSizer.getObjectSize(result);
		// log.debug("Object size {} {}", s1, s2);

		log.debug("Deserijalizirano " + s.prettyPrintMy());
		s.start("transfrom");

		float relHpercent = 51;

		transformData3(idMSMS_PeakFileMap, msResultOnly, result, relHpercent);
		// transformDataTocSV(idMSMS_PeakFileMap, msResultOnly, result,
		// relHpercent);
		log.debug("Finish " + s.prettyPrintMy());
	}

	public static void main222(String[] args) {
		System.out.println(calculatePercentigFrom(24, 5));
	}

	static class T {
		/**
		 * MSMS name => brother
		 */
		HashMap<String, Brother> brothers = new HashMap<String, UFB3TransformUtil.Brother>(256);
		int id;
		String name;

		public T(int taxonID, String name) {
			id = taxonID;
			this.name = name;
		}
	}

	/**
	 * Nemora biti obavezno prisutna oba brata.
	 * 
	 * @author tag
	 * 
	 */
	static class Brother {
		ArrayList<UFB3Hit> pos = new ArrayList<UFB3Hit>(32);
		ArrayList<UFB3Hit> neg = new ArrayList<UFB3Hit>(32);

		UFB3Hit bestPos;
		UFB3Hit bestNeg;

		public boolean isSS() {
			if (bestNeg != null && bestPos != null) {
				if (bestNeg.createPeptideString().equals(bestPos.createPeptideString())) {
					return true;
				}
			}
			return false;
		}

		@Override
		public String toString() {
			if (bestNeg != null && bestPos != null) {
				if (bestNeg.createPeptideString().equals(bestPos.createPeptideString())) {
					return "SS -" + bestNeg.toString() + " & +" + bestPos.toString();
				}
				return "-" + bestNeg.toString() + " & +" + bestPos.toString();
			}

			if (bestNeg != null) {
				return "-" + bestNeg.toString();
			}
			if (bestPos != null) {
				return "+" + bestPos.toString();
			}
			return "???";
		}

		/**
		 * Ako ima samo pos ili samo neg
		 * 
		 * @return
		 */
		public boolean isSolo() {
			if (pos.isEmpty()) {
				return true;
			}
			if (neg.isEmpty()) {
				return true;
			}
			return false;
		}

		/**
		 * Vraca hits od pos ako nije empty ili neg ako nije empty. Tj. solo
		 * 
		 * @return
		 */
		public ArrayList<UFB3Hit> getHitsFromSolo() {
			if (!pos.isEmpty()) {
				return pos;
			}
			return neg;
		}

		public void addSoloBestHit(UFB3Hit bestHit) {
			if (pos.isEmpty()) {
				bestNeg = bestHit;
			} else {
				bestPos = bestHit;
			}
		}
	}

	public static void transformDataTocSV(Map<String, PeakFile> idMSMS_PeakFileMap, UFB_MSResult msResultOnly,
			UFB_Result result, float bestRelHPercent) throws IOException {
		// HashMap<Long, PeakFile> idPeakMap =
		// createIDlongMSMSMap(idMSMS_PeakFileMap);
		BufferedWriter w = new BufferedWriter(new FileWriter(new File("c:/tmp/result.csv")));

		for (UFB_MSResult ms : result.msResultsList) {

			if (msResultOnly != null) { // filtriraj ovo, gel based
				if (ms != msResultOnly) {
					continue;
				}
			}

			for (UFB_MSMSresult msms : ms.msmsList) {

				// reduceByRelH(msms, bestRelHPercent, idMSMS_PeakFileMap);

				PeakFile msmsPeakFile = idMSMS_PeakFileMap.get(msms.msmsID);
				for (UFB_Protein protein : msms.foundedProteins) {

					float relH = PeptideUtils.realtiveHeightIntensity(msmsPeakFile, protein.ions);
					for (GI gi : protein.gis) {
						w.append("0\t" + gi.taxonID + "\t");
						// gi.taxonID;
						w.append(gi.gi + "\t");
						// gi.gi
						w.append(msmsPeakFile.getId() + "\t");
						// msmsPeakFile.msmsID
						w.append((msms.positive ? "1" : "0") + "\t");
						// msms.positive
						w.append(protein.cepanica + "\t");
						// protein.cepanica
						w.append(protein.uNizuAA + "\t");
						// protein.uNizuAA
						w.append(relH + "\t");
						// relH
						w.append(protein.msmsMatchPercent + "\t");
						// protein.msmsMatchPercent

						w.append(msmsPeakFile.getMassPrecursor() + "");
						w.append("\n");
					}
				}
			}
		}

		w.close();
	}

	static class Tax {

		private final int taxonID;
		private Map<Float, NegPos> msmsMap = new HashMap<Float, NegPos>();
		private final String name;

		float sumRHss = 0;
		float sumRH = 0;

		public Tax(int taxonID, String name) {
			this.taxonID = taxonID;
			this.name = name;
		}

		public void add(GI gi, UFB_Protein protein, PeakFile msmsPeakFile) {
			NegPos negPos = msmsMap.get(msmsPeakFile.getMassPrecursor());
			if (negPos == null) {
				negPos = new NegPos();
				msmsMap.put(msmsPeakFile.getMassPrecursor(), negPos);
			}
			negPos.add(gi, protein, msmsPeakFile);
		}

		public int getTaxonID() {
			return taxonID;
		}

		public String getName() {
			return name;
		}
	}

	static class NegPos {
		MSMS neg;
		MSMS pos;

		@Override
		public String toString() {
			return "NegPos [neg=" + neg + "\npos=" + pos + "]";
		}

		public void add(MSMS msms) {
		}

		public void add(GI gi, UFB_Protein protein, PeakFile msmsPeakFile) {
			if (msmsPeakFile.isNegative()) {
				if (neg == null) {
					neg = new MSMS();
				}
				neg.add(gi, protein, msmsPeakFile);
			} else { // positive
				if (pos == null) {
					pos = new MSMS();
				}
				pos.add(gi, protein, msmsPeakFile);
			}

		}

		public int removeAllExceptSS(BrotherSS bestSS) {
			int countRemoved = 0;
			{
				Iterator<Hit> itNeg = neg.hits.iterator();
				while (itNeg.hasNext()) {
					UFB3TransformUtil.Hit hit = itNeg.next();
					if (!hit.equals(bestSS.hitNeg)) {
						itNeg.remove();
						countRemoved++;
					} else {
						hit.ss = true;
					}
				}
			}
			Iterator<Hit> itPos = pos.hits.iterator();
			while (itPos.hasNext()) {
				UFB3TransformUtil.Hit hit = itPos.next();
				if (!hit.equals(bestSS.hitPos)) {
					itPos.remove();
					countRemoved++;
				} else {
					hit.ss = true;
				}
			}
			if (neg.hits.size() != 1) {
				log.error("Nije 1 {}", neg.hits);
			}
			if (pos.hits.size() != 1) {
				log.error("Nije 1 {}", neg.hits);
			}
			return countRemoved;
		}

		public final void removeAllExceptBestRelHhit2() {
			if (pos != null)
				removeByRelH(pos.hits);
			if (neg != null)
				removeByRelH(neg.hits);
		}

		private final void removeByRelH(ArrayList<Hit> hits) {

			if (hits == null || hits.isEmpty()) {
				return;
			}
			Hit bestHit = null;
			for (Hit h : hits) {
				if (bestHit == null) {
					bestHit = h;
					continue;
				}

				if (h.relH > bestHit.relH) {
					bestHit = h;
					continue;
				} else if (h.relH == bestHit.relH) {
					if (h.prot.msmsMatchPercent > bestHit.prot.msmsMatchPercent) {
						bestHit = h;
						continue;
					}
					// else if (h.prot.msmsMatchPercent ==
					// bestHit.prot.msmsMatchPercent) {
					// ovo su jednaki hitovi valjda samo drugi GI
					// log.debug("Sto je ovo: {} {}", h, bestHit);
					// }
				}
			}
			ArrayList<Hit> ostaviHit = new ArrayList<UFB3TransformUtil.Hit>(1);
			ostaviHit.add(bestHit);
			hits.retainAll(ostaviHit);
		}

		/**
		 * 
		 */
		@Deprecated
		public void removeAllExceptBestRelHhit() {
			if (pos != null) {
				float bestRelH = 0;
				for (Hit h : pos.hits) {
					bestRelH = Math.max(bestRelH, h.relH);
				}
				Iterator<Hit> itPos = pos.hits.iterator();
				while (itPos.hasNext()) {
					UFB3TransformUtil.Hit hit = itPos.next();
					if (hit.relH < bestRelH) {
						itPos.remove();
					}
				}
				if (pos.hits.size() != 1) {
					log.error("Nije 1 vec {} {}", pos.hits.size(), pos.hits);
				}
			}

			if (neg != null) {
				float bestRelH = 0;
				for (Hit h : neg.hits) {
					bestRelH = Math.max(bestRelH, h.relH);
				}
				Iterator<Hit> itNeg = neg.hits.iterator();
				while (itNeg.hasNext()) {
					UFB3TransformUtil.Hit hit = itNeg.next();
					if (hit.relH < bestRelH) {
						itNeg.remove();
					}
				}

				if (neg.hits.size() != 1) {
					log.error("Nije 1");
				}
			}
		}
	}

	static class MSMS {
		private float precursorMass = 0;
		private ArrayList<Hit> hits = new ArrayList<Hit>(6);
		private HashSet<String> cepaniceUnique = new HashSet<String>(4);

		@Override
		public String toString() {
			return "MSMS [precursorMass=" + precursorMass + ", hits=" + hits + "]";
		}

		public void add(GI gi, UFB_Protein protein, PeakFile msmsPeakFile) {
//			AssertUtil.notNull(gi);
//			AssertUtil.notNull(protein);
//			AssertUtil.notNull(msmsPeakFile);

			
			if(cepaniceUnique.contains(protein.cepanica)) {
				// ako vec sadrzi tu cepanicu, onda mi netreba
				return;
			}
			
			// TODO: provjeriti da li vec postoji hit sa ovakovim sekvencom
			// razliciti GI i sekvencija moze biti ovjde, treba grupirati jos po
			// sekvenciji.
			hits.add(new Hit(gi, protein, msmsPeakFile));
			// if (hits.size() > 5) {
			// log.debug("hits "+ hits );
			// }

			if (precursorMass == 0) {
				precursorMass = msmsPeakFile.getMassPrecursor();
			} else {
				if (precursorMass != msmsPeakFile.getMassPrecursor()) {
					throw new RuntimeException("Nije isti prekursor " + precursorMass + " "
							+ msmsPeakFile.getMassPrecursor());
				}
			}

		}

		// public ArrayList<Hit> getHits() {
		// return hits;
		// }

	}

	static final class Hit {
		boolean ss = false;
		/**
		 * Redni broj kad je poresano po orderu.
		 */
		int relHorderNumber = -1;
		float relH;

		GI gi;
		UFB_Protein prot;
		PeakFile msmsPeakFile;

		public Hit(GI gi, UFB_Protein protein, PeakFile msmsPeakFile) {
			this.gi = gi;
			prot = protein;
			this.msmsPeakFile = msmsPeakFile;
		}

		@Override
		public String toString() {
			return "\nHit [ss=" + ss + ", relHorderNumber=" + relHorderNumber + ", relH=" + relH + ", gi=" + gi
					+ ", prot=" + prot + ", msmsPeakFile=" + msmsPeakFile + "]";
		}

	}

	@Deprecated
	public static UFB3result transformData3(Map<String, PeakFile> idMSMS_PeakFileMap, UFB_MSResult msResultOnly,
			UFB_Result result, float bestRelHPercent) {

		SpringStopWatch s = SpringStopWatch.startNew("createIDlongMSMSMap");
		HashMap<Long, PeakFile> idPeakMap = createIDlongMSMSMap(idMSMS_PeakFileMap);
		s.stop().start("2");
		HashMap<Integer, Tax> taxons = new HashMap<Integer, UFB3TransformUtil.Tax>(3000);
		// 1. grupiranje po takxonu.

		for (UFB_MSResult ms : result.msResultsList) {

			if (msResultOnly != null) { // filtriraj ovo, gel based
				if (ms != msResultOnly) {
					continue;
				}
			}
			for (UFB_MSMSresult msms : ms.msmsList) {
				PeakFile msmsPeakFile = idMSMS_PeakFileMap.get(msms.msmsID);
				for (UFB_Protein protein : msms.foundedProteins) {

					for (GI gi : protein.gis) {
						Tax tax = taxons.get(gi.taxonID);
						if (tax == null) {
							byte[] desc = result.globalTaxMap.get(gi.taxonID);
							if (desc != null) {
								tax = new Tax(gi.taxonID, new String(desc));
							} else {
								tax = new Tax(gi.taxonID, new String("???"));
							}
							taxons.put(gi.taxonID, tax);
						}
						// if(protein.cacheNum == -333) {
						// log.debug("Evo ga prot: {}", protein);
						// }
						tax.add(gi, protein, msmsPeakFile);
					}
				}
			}
		}

		s.stop().start("3. relH taxon");
		// 2. Napraviti da uzme iz svake liste (hitovi jednog MSMS blizanca) X%
		// najboljih po relH-u. X neka bude parametar za birati.
		ArrayList<Integer> taxonIDforRemoved = new ArrayList<Integer>();
		for (Entry<Integer, Tax> entry : taxons.entrySet()) {

			Tax tax = entry.getValue();
			Set<Entry<Float, NegPos>> msmsEntryes = tax.msmsMap.entrySet();

			boolean containSS = false;
			for (Entry<Float, NegPos> en : msmsEntryes) {
				NegPos posNeg = en.getValue();
				// if(posNeg.neg != null && posNeg.neg.precursorMass ==
				// 2892.8096F && tax.taxonID == 559292) {
				// log.debug("Evo ga "+ posNeg.neg.precursorMass +
				// " tax bi trebalo 559292 a je= {}", tax.taxonID);
				// nasao = true;
				// }
				reduceByRelH(posNeg, bestRelHPercent, idPeakMap);

				// 3. Tada trazi u toj listi SS, te uzimi samo SS ako postoje.
				List<BrotherSS> ss = findSS(posNeg);
				if (!ss.isEmpty()) {
					BrotherSS bestSS = findBestSS(ss);
					posNeg.removeAllExceptSS(bestSS);
				} else {
					posNeg.removeAllExceptBestRelHhit2();
				}
				// if(nasao) {
				// log.debug("pos neg {}", posNeg);
				// }

				// sada je u listi ostao samo jedan hit
			}

			// makni ne SS taxon
			if (containSS == false) {
				taxonIDforRemoved.add(tax.taxonID);
			}
		} // end tax

		s.stop().start("populateUFB3results");
		UFB3result ressss = populateUFB3results(taxons);
		log.debug(s.stop().prettyPrintMy());
		return ressss;
	}

	private static UFB3result populateUFB3results(HashMap<Integer, Tax> taxons) {
		UFB3result res = new UFB3result();
		res.setHits(new HashMap<Integer, ArrayList<UFB3Hit>>(taxons.size()));
		Collection<Tax> values = taxons.values();

		for (Tax tax : values) {
			Set<Entry<Float, NegPos>> entrySet = tax.msmsMap.entrySet();
			for (Entry<Float, NegPos> entry : entrySet) {
				NegPos v = entry.getValue();
				if (v.neg != null) {
					for (Hit hit : v.neg.hits) {
						tax.sumRH += hit.relH;
						if (hit.ss) {
							tax.sumRHss += hit.relH;
						}
					}
				}
				if (v.pos != null) {
					for (Hit hit : v.pos.hits) {
						tax.sumRH += hit.relH;
						if (hit.ss) {
							tax.sumRHss += hit.relH;
						}
					}
				}
			}
		}
		for (Tax tax : values) {
			UFB3TaxGroup t = new UFB3TaxGroup();
			t.setTaxID(tax.taxonID);
			t.setTaxName(tax.getName());
			t.setRh(tax.sumRH);
			t.setRhSS(tax.sumRHss);
			Set<Entry<Float, NegPos>> entrySet = tax.msmsMap.entrySet();
			res.addTaxon(t);
			ArrayList<UFB3Hit> hitsList = new ArrayList<UFB3Hit>(entrySet.size() * 2);
			for (Entry<Float, NegPos> entry : entrySet) {
				NegPos v = entry.getValue();
				if (v.neg != null) {
					for (Hit hit : v.neg.hits) {
						UFB3Hit ufb3hit = toUFB3hit(hit.prot, hit.msmsPeakFile, hit);
						hitsList.add(ufb3hit);
					}
				}
				if (v.pos != null) {
					for (Hit hit : v.pos.hits) {
						UFB3Hit ufb3hit = toUFB3hit(hit.prot, hit.msmsPeakFile, hit);
						hitsList.add(ufb3hit);
					}
				}
			}
			res.getHits().put(tax.taxonID, hitsList);
		}
		return res;
	}

	/**
	 * 4. Ako postoji vise SS, onda zbraja redni broj svakog hita (kad su
	 * poredani po relH) te uzima SS par sa manjim rednim brojem. Ako je isti
	 * zbroj rednih brojeva, onda uzima par koji ima pojedinacno najmanji redni
	 * broj (#1 i #7 je loslje od #2 i #2 ali #1 i #3 je bolje od #2 i #2).
	 * 
	 * @param ss
	 * @return
	 */
	private static BrotherSS findBestSS(List<BrotherSS> ss) {
		BrotherSS bestSS = null;
		for (BrotherSS s : ss) {
			if (bestSS == null) {
				bestSS = s;
				continue;
			}

			if (s.sumOrderNumber() < bestSS.sumOrderNumber()) {
				bestSS = s;
			} else if (s.sumOrderNumber() == bestSS.sumOrderNumber()) {
				if (s.getMinRelHorderNumber() < bestSS.getMinRelHorderNumber()) {
					bestSS = s;
				}

			}
		}
		return bestSS;
	}

	private static List<BrotherSS> findSS(NegPos posNeg) {
		List<BrotherSS> brotherSSs = new ArrayList<UFB3TransformUtil.BrotherSS>();
		if (posNeg.neg != null && !posNeg.neg.hits.isEmpty()) {
			if (posNeg.pos != null && !posNeg.pos.hits.isEmpty()) {

				for (Hit neg : posNeg.neg.hits) {
					for (Hit pos : posNeg.pos.hits) {
						String cepNeg = neg.prot.cepanica;
						String cepPos = pos.prot.cepanica;
						boolean negSign = neg.msmsPeakFile.isPositive();
						boolean posSign = pos.msmsPeakFile.isPositive();
						if (cepNeg.equals(cepPos) && negSign != posSign) {
							brotherSSs.add(new BrotherSS(neg, pos));
						}

					}
				}

			}
		}

		return brotherSSs;

	}

	static class BrotherSS {
		private Hit hitPos;
		private Hit hitNeg;

		public BrotherSS(Hit neg, Hit pos) {
			hitNeg = neg;
			hitPos = pos;
		}

		public int sumOrderNumber() {
			return hitNeg.relHorderNumber + hitPos.relHorderNumber;
		}

		public int getMinRelHorderNumber() {
			if (hitNeg.relHorderNumber < hitPos.relHorderNumber) {
				return hitNeg.relHorderNumber;
			} else {
				return hitPos.relHorderNumber;
			}
		}
	}

	/**
	 * Ostavlja u listi hits samo <b>bestRelHPercent</b> % najboljih hitova. Ova
	 * metoda mjenja listu hits.
	 * 
	 * @param posNeg
	 * @param bestRelHPercent
	 * @param idPeakMap
	 */
	private static void reduceByRelH(NegPos posNeg, float bestRelHPercent, HashMap<Long, PeakFile> idPeakMap) {
		if (posNeg.neg != null && !posNeg.neg.hits.isEmpty()) {
			ArrayList<Hit> hits = posNeg.neg.hits;
			reduceByRelH3(hits, bestRelHPercent, idPeakMap);
		}

		if (posNeg.pos != null && !posNeg.pos.hits.isEmpty()) {
			ArrayList<Hit> hits = posNeg.pos.hits;
			reduceByRelH3(hits, bestRelHPercent, idPeakMap);
		}

	}

	/**
	 * Ostavlja u listi hits samo <b>bestRelHPercent</b> % najboljih hitova. Ova
	 * metoda mjenja listu hits. Postavlja relH i relHorder u hit objekat.
	 * 
	 * @param hits
	 *            mjenja se lista unutar metode.
	 * @param bestRelHPercent
	 *            postotak koliko ih ostavljam u listi od po najboljem relH
	 * @param idPeakMap
	 */
	private static void reduceByRelH3(ArrayList<Hit> hits, float bestRelHPercent, HashMap<Long, PeakFile> idPeakMap) {
		int bestInList = calculatePercentigFrom(hits.size(), bestRelHPercent);

		for (Hit h : hits) {
			float relH = PeptideUtils.realtiveHeightIntensity(idPeakMap.get(h.msmsPeakFile.getId()), h.prot.ions);
			h.relH = relH;
		}
		sortByRelH3(hits);
		int relHOrder = 1;
		for (Hit h : hits) {
			h.relHorderNumber = relHOrder++;
		}

		Iterator<Hit> iter = hits.iterator();
		int count = 0;
		while (iter.hasNext()) {
			iter.next();
			if (count > bestInList) {
				iter.remove();

			}
			count++;
		}

	}

	private static Comparator<Hit> relHhitComprarator = new Comparator<Hit>() {
		public int compare(Hit o1, Hit o2) {
			return -Floats.compare(o1.relH, o2.relH);
		}
	};

	private static void sortByRelH3(ArrayList<Hit> hits) {
		Collections.sort(hits, relHhitComprarator);
	}

	/**
	 * Najveci relH na pocetku liste.
	 * 
	 * @author tag
	 * 
	 */
	static class RelHcomparator implements Comparator<UFB_Protein> {

		private final IdentityHashMap<UFB_Protein, Float> prottRelH;

		RelHcomparator(final IdentityHashMap<UFB_Protein, Float> prottRelH) {
			this.prottRelH = prottRelH;

		}

		public final int compare(UFB_Protein o1, UFB_Protein o2) {
			Float r1 = prottRelH.get(o1);
			Float r2 = prottRelH.get(o2);
			return -Floats.compare(r1, r2);
		}

	}

	private static int calculatePercentigFrom(int total, float percent) {
		if (percent == 0) {
			return 0;
		}
		if (percent < 0) {
			throw new RuntimeException("percentage is less then zerro " + percent);
		}
		if (total <= 0) {
			return 0;
		}

		int res = (int) (total / 100F * percent);
		if (res < 1) {
			res = 1;
		}
		return res;
	}

	private static HashMap<Long, PeakFile> createIDlongMSMSMap(Map<String, PeakFile> idMSMS_PeakFileMap) {
		Set<Entry<String, PeakFile>> entrySet = idMSMS_PeakFileMap.entrySet();
		HashMap<Long, PeakFile> m = new HashMap<Long, PeakFile>(idMSMS_PeakFileMap.size());
		for (Entry<String, PeakFile> entry : entrySet) {
			m.put(entry.getValue().getId(), entry.getValue());
		}
		return m;
	}

	static class BestSS {
		UFB3Hit neg;
		UFB3Hit pos;
		int currentScore = 0;

		void addNew(UFB3Hit neg, UFB3Hit pos) {
			if (neg == null) {
				this.neg = neg;
				this.pos = pos;
			} else {
				int score = calculateScore(neg, pos);
				if (score > currentScore) { // TODO: sto ako dvoje ima isti
											// score?
					currentScore = score;
					this.neg = neg;
					this.pos = pos;
				}
			}

		}

		public boolean isEmpty() {
			return neg == null;
		}

		private int calculateScore(UFB3Hit n, UFB3Hit p) {
			return n.getNiz() + p.getNiz();
		}
	}

	private static UFB3Hit toUFB3hit(UFB_Protein protein, PeakFile msmsPeakFile, Hit hit) {
		UFB3Hit h = new UFB3Hit();
		ArrayList<GI> gis = protein.gis;
		long[] gisLong = new long[gis.size()];
		for (int i = 0; i < gis.size(); i++) {
			gisLong[i] = gis.get(i).gi;
		}
		h.setContainShureShoot(hit.ss);
		h.setIons(protein.ions);
		h.setMsmsID(msmsPeakFile.getId());
		h.setMsmsMatchPercent((float) protein.msmsMatchPercent);
		h.setNiz(protein.uNizuAA);
		h.setGis(toGis(protein.gis));
		return h;
	}

	private static long[] toGis(ArrayList<GI> gis) {
		long[] g = new long[gis.size()];
		int i = 0;
		for (GI gi : gis) {
			g[i++] = gi.gi;
		}
		return g;
	}

	public static HashMap<Integer, TaxonGrupa> transformData(Map<String, PeakFile> idMSMS_PeakFileMap,
			UFB_MSResult msResultOnly, UFB_Result result) {

		// saveToDisk(idMSMS_PeakFileMap, msResultOnly, result);

		if (result == null) {
			return new HashMap<Integer, TaxonGrupa>(0);
		}
		HashMap<Integer, TaxonGrupa> taxID_TaxonGroupMap = new HashMap<Integer, TaxonGrupa>(3000);
		final SpringStopWatch s = SpringStopWatch.startNew("Transformacija 1");
		StringBuilder taxonNotFound = new StringBuilder(300);
		final List<UFB_MSResult> msResultsList = result.msResultsList;
		for (UFB_MSResult msResult : msResultsList) {

			if (msResultOnly != null) { // filtriraj ovo, gel based
				if (msResult != msResultOnly) {
					// log.debug("Necu "+ msResult);
					continue;
				} else {
					// log.debug("Uzimam : "+ msResult);
				}

			}

			for (UFB_MSMSresult msms : msResult.msmsList) {
				for (UFB_Protein protein : msms.foundedProteins) {

					PeakFile msmsPeakFile = idMSMS_PeakFileMap.get(msms.msmsID);
					// AppModel.getMapIDmsms();
					if (msmsPeakFile == null) {
						log.debug("sadrzi idMSMS: {}" + idMSMS_PeakFileMap);
						// znaci da je user obrisao neke MSMS iz workspace
						// vjerojatno
						throw new RuntimeException("Nemogu naci MSMS " + msms.name + " id:" + msms.msmsID
								+ ". Vjerojatno je obriso user.");

					}
					final double relH = PeptideUtils.realtiveHeightIntensity(msmsPeakFile, protein.ions);

					for (GI gi : protein.gis) {
						TaxonGrupa taxonGroup;
						if (taxID_TaxonGroupMap.containsKey(gi.taxonID)) {
							taxonGroup = taxID_TaxonGroupMap.get(gi.taxonID);
						} else {
							byte[] taxonDesc = result.globalTaxMap.get(gi.taxonID);
							if (taxonDesc == null) {
								taxonNotFound.append(gi.taxonID).append("\n");
								taxonDesc = "???".getBytes(UFB_Protein.ascii);
							}
							taxonGroup = new TaxonGrupa(gi.taxonID, new String(taxonDesc, UFB_Protein.ascii));
							taxID_TaxonGroupMap.put(gi.taxonID, taxonGroup);
						}
						final UFB_ProteinWrapper proteinWrapper = new UFB_ProteinWrapper(protein, msms);

						// ako je identican protein ovdje, onda idi dalje
						if (taxonGroup.proteins.contains(proteinWrapper)) {
							continue;
						}

						proteinWrapper.relH = relH;

						taxonGroup.add(proteinWrapper, msms, msResult);

					}

				}
			}
		}
		s.stop().start("Transformacija 2  pronadjiBrata");

		// printTaxonStatistics(taxID_TaxonGroupMap);

		// Mice sve koji imaju manje od 10% hitova (od najveceg broja hitova u
		// nekom taxonu) ako ih ima vise od 3000
		reducirajAkoImaViseOdVrsta(taxID_TaxonGroupMap, 3000);

		{ // nadji bracu, te oznaci shure shoot
			for (TaxonGrupa taxonGrupa : taxID_TaxonGroupMap.values()) {
				// log.debug("size: "+ taxonGrupa.proteins.size() );
				final HashSet<UFB_ProteinWrapper> proteins = taxonGrupa.proteins;
				final Iterator<UFB_ProteinWrapper> it = proteins.iterator();
				while (it.hasNext()) {
					UFB_ProteinWrapper prot = it.next();
					if (!prot.containShureShoot) {
						UFB_ProteinWrapper protBrat = pronadjiBrata(prot, proteins);
						if (protBrat != null) {
							prot.containShureShoot = true;
							protBrat.containShureShoot = true;
							prot.ss = protBrat;
							protBrat.ss = prot;
							// log.debug("nasao");
						}
					}

				}
			}

		}
		s.stop();
		log.debug(s.prettyPrintMy());
		return taxID_TaxonGroupMap;
	}

	/**
	 * Ako ima vrsta vise od <i>viseOd</i> onda reduciraj mapu tj. sve koji
	 * imaju manje od 5 hita.
	 * 
	 * @param taxID_TaxonGroupMap
	 *            Kratko traje
	 * @param viseOd
	 */
	private static void reducirajAkoImaViseOdVrsta(HashMap<Integer, TaxonGrupa> taxID_TaxonGroupMap, int viseOd) {
		if (taxID_TaxonGroupMap.size() > viseOd) {

			ArrayList<Integer> maknutiTaxID = new ArrayList<Integer>(taxID_TaxonGroupMap.size());
			final SpringStopWatch s = SpringStopWatch.startNew("reduciranje");

			ArrayList<TaxonGrupa> taxs = new ArrayList<TaxonGrupa>(taxID_TaxonGroupMap.values());

			int maxSize = 0;
			for (TaxonGrupa taxonGrupa : taxs) {
				maxSize = Math.max(maxSize, taxonGrupa.proteins.size());
			}
			int intialSize = taxID_TaxonGroupMap.size();
			for (TaxonGrupa taxonGrupa : taxs) {
				final int size = taxonGrupa.proteins.size();
				// makni manje od 5 hita
				if (size < 5) {
					taxID_TaxonGroupMap.remove(taxonGrupa.getTaxID());
					maknutiTaxID.add(size);
				}
			}

			log.debug(s.stop().prettyPrintMy());
			log.debug("Maknuo ih " + taxID_TaxonGroupMap.size() + " od " + intialSize);
		}

	}

	/**
	 * SPORO. Brat je ako imaju istu cepanicu, iz istog MSMS, i razliciti
	 * predznaka.
	 */
	private static UFB_ProteinWrapper pronadjiBrata(UFB_ProteinWrapper prot, HashSet<UFB_ProteinWrapper> proteins) {
		for (UFB_ProteinWrapper drugi : proteins) {
			if (drugi == prot) {
				continue;
			}
			if ((drugi.parentMSMS.positive != prot.parentMSMS.positive)
					&& (drugi.msName.equals(prot.msName) && drugi.parentMSMS.name.equals(prot.parentMSMS.name))) {
				if (drugi.protein.cepanica.equals(prot.protein.cepanica)) {
					return drugi;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param tg
	 *            countShureshoot, countPogodjenihMSmatch
	 * @return
	 */
	public static final TwoValue<Integer, Integer> countShureShoot(TaxonGrupa tg) {
		int countShureshoot = 0;
		int countPogodjenihMSmatch = 0;
		{
			final HashSet<UFB_ProteinWrapper> proteins = tg.proteins;
			for (UFB_ProteinWrapper ufb_ProteinWrapper : proteins) {
				if (ufb_ProteinWrapper.containShureShoot) {
					countShureshoot++;
				}
				if (ufb_ProteinWrapper.msCountMatch > 0) {
					countPogodjenihMSmatch += ufb_ProteinWrapper.msCountMatch;
				}
			}
			if (countShureshoot != 0) {
				countShureshoot = countShureshoot / 2;
			}
		}
		TwoValue<Integer, Integer> res = new TwoValue<Integer, Integer>(countShureshoot, countPogodjenihMSmatch);
		return res;
		// return countShureshoot;
	}

	/**
	 * Dali MS pikovi sadrze masu sa deltom
	 * 
	 * @param allMSPeakFiles
	 * @param mass
	 * @param d
	 * @return
	 */
	public static TwoValue<Peak, PeakFile> containMass(Set<PeakFile> allMSPeakFiles, double mass, double delta) {
		for (PeakFile peakFile : allMSPeakFiles) {
			final List<Peak> allPeaks = peakFile.getAllPeaks();
			for (Peak peak : allPeaks) {
				if (peak.getCentroidMass() - delta < mass && mass < delta + peak.getCentroidMass()) {
					return new TwoValue<Peak, PeakFile>(peak, peakFile);
				}
			}
		}
		return null;
	}

	/**
	 * izbaciti sve ne SS tax grupe ako ijedna ima SS
	 * 
	 * @param taxID_TaxonGroupMap
	 */
	public static void removeNonSStaxon(HashMap<Integer, TaxonGrupa> taxID_TaxonGroupMap) {
		final Set<Entry<Integer, TaxonGrupa>> entrySet = taxID_TaxonGroupMap.entrySet();

		boolean containSS = false;
		for (Entry<Integer, TaxonGrupa> entry : entrySet) {
			if (entry.getValue().containAnySS()) {
				containSS = true;
				break;
			}
		}
		if (containSS) {
			final Iterator<Entry<Integer, TaxonGrupa>> it = entrySet.iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, TaxonGrupa> entry = it.next();
				if (!entry.getValue().containAnySS()) {
					// taxID_TaxonGroupMap.remove(entry.getKey());
					it.remove();
				}
			}
		}
	}

	private static final Logger log = LoggerFactory.getLogger(UFB3TransformUtil.class);

	/**
	 * 
	 * @param result
	 * @param idMSMS_PeakFileMap
	 * @param taxon
	 *            taxoni, tj. hitovi iz ovog taxona koji se traze
	 * @param taxonAll
	 *            svi taxoni, taxon grupe iz kojih se trazi SS
	 * @param monitor
	 * @return
	 */
	public static ArrayList<SSS> findSSS(UFB_Result result, ArrayList<TaxonGrupa> taxon, ArrayList<TaxonGrupa> taxonAll) {

		ArrayList<SSS> sssList = new ArrayList<UFB3TransformUtil.SSS>();
		// monitor.beginTask("Search for unique SS in species", taxon.size());
		for (TaxonGrupa taxonGrupa : taxon) {
			final HashSet<UFB_ProteinWrapper> proteins = taxonGrupa.proteins;
			for (UFB_ProteinWrapper hit : proteins) {
				if (hit.containShureShoot) {
					final ArrayList<GI> giFromSS = getGieveTaxona(hit.protein.gis, taxonGrupa);
					// ako ovi GI-evi ne postoje u drugim taxonima onda je to to

					boolean sadrzi = false;
					for (TaxonGrupa taxonInSearch : taxonAll) {
						if (taxonInSearch != taxonGrupa) {
							if (!giFromSS.isEmpty()) {
								if (sadrziTaxonGieve(giFromSS.get(0), taxonInSearch)) {
									sadrzi = true;
									break;
								}
							}

						}
					}

					if (!sadrzi) {
						sssList.add(new SSS(taxonGrupa, giFromSS, hit));
					}

				}

			}
			// monitor.worked(1);
			// if (monitor.isCanceled()) {
			// monitor.done();
			// return sssList;
			// }

			// log.debug("{} / {} obradjeno", count, taxon.size());
		}

		return sssList;

	}

	private static boolean sadrziTaxonGieve(GI gi, TaxonGrupa tgOther) {
		final HashSet<UFB_ProteinWrapper> proteins = tgOther.proteins;
		for (UFB_ProteinWrapper p : proteins) {
			final ArrayList<GI> gis = p.protein.gis;
			for (GI giOther : gis) {
				if (giOther == gi) {
					return true;
				}
			}
		}
		return false;
	}

	public static class SSS {
		public SSS(TaxonGrupa taxonGrupa, ArrayList<GI> giFromSS, UFB_ProteinWrapper hit) {
			tg = taxonGrupa;
			gis = giFromSS;
			this.hit = hit;
		}

		public ArrayList<GI> gis;
		public UFB_ProteinWrapper hit;
		public TaxonGrupa tg;
	}

	/**
	 * Vraca gi-eve samo one koji spadaju u taxon grupu
	 * 
	 * @param gis
	 * @param taxonGrupa
	 * @return
	 */
	private static ArrayList<GI> getGieveTaxona(ArrayList<GI> gis, TaxonGrupa taxonGrupa) {
		ArrayList<GI> result = new ArrayList<GI>(gis.size());
		for (GI gi : gis) {
			if (gi.taxonID == taxonGrupa.getTaxID()) {
				result.add(gi);
			}
		}
		result.trimToSize();
		return result;

	}

	/**
	 * Task http://www.ncbi.nlm.nih.gov/protein/365733994?report=fasta
	 * 
	 * @param calculatedTaxons
	 */
	public static void findAndSetUniqueGI(ArrayList<TaxonGrupa> calculatedTaxons) {
		for (TaxonGrupa taxonGrupa : calculatedTaxons) {
			Iterator<UFB_ProteinWrapper> it = taxonGrupa.proteins.iterator();
			NEXT: while (it.hasNext()) {
				UFB_ProteinWrapper hit = it.next();
				for (GI gi : hit.protein.gis) {
					if (gi.taxonID != taxonGrupa.getTaxID()) {
						continue NEXT;
					}
				}
				// znaci da su tu samo GI-evi iz ovog taxona
				hit.uniqueProtein = true;
				taxonGrupa.countUniqueProteins++;
			}
		}

	}

	public static void findAndSetUniqueGI(UFB3result res) {
		for (UFB3TaxGroup t : res.getTaxon()) {
			Iterator<UFB3Hit> it = res.getHits().get(t.getTaxID()).iterator();
			while (it.hasNext()) {
				it.next();

			}
		}
	}

	public static void countSS(UFB3result u) {
		for (UFB3TaxGroup t : u.getTaxon()) {
			Iterator<UFB3Hit> it = u.getHits().get(t.getTaxID()).iterator();
			int countSS = 0;
			int countSSniz = 0;
			while (it.hasNext()) {
				UFB3Hit h = it.next();
				if (h.isContainShureShoot()) {
					countSS++;
					countSSniz += h.getNiz();
				}
			}
			t.setSs(countSS);
			t.setSsNiz(countSSniz);

		}
	}

	public static void removeNonSStaxon(UFB3result u) {
		Iterator<UFB3TaxGroup> it = u.getTaxon().iterator();
		while (it.hasNext()) {
			UFB3TaxGroup t = it.next();
			if (t.getSs() <= 0) {
				u.getHits().remove(t.getTaxID());
				it.remove();

			}
		}
	}
}
