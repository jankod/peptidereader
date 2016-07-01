package hr.semgen.masena.share;

import hr.semgen.masena.share.blast.BlastInput;
import hr.semgen.masena.share.blast.BlastOutput;
import hr.semgen.masena.share.blast.BlastOutputGlobal;
import hr.semgen.masena.share.blast.MSSearchBlastParamModel;
import hr.semgen.masena.share.csh.CheckSingleHitParams;
import hr.semgen.masena.share.csh.CheckSingleHitResults;
import hr.semgen.masena.share.diff.TripticMassOrganismResult;
import hr.semgen.masena.share.model.PeakFile;
import hr.semgen.masena.share.mph.MphParameters;
import hr.semgen.masena.share.mph.MphResult;
import hr.semgen.masena.share.prediction.GambResult;
import hr.semgen.masena.share.ufb3.UFB3GiTax;
import hr.semgen.masena.share.ufb3.UFB3result;
import hr.semgen.masena.share.ufb3.UserStatistic;
import hr.semgen.masena.ufb.UFB_MSmatchResult;
import hr.semgen.masena.ufb.UFB_Params;
import hr.semgen.masena.ufb.UFB_Result;
import hr.semgen.ms.ufb2.MS;
import hr.semgen.ms.ufb2.SearchResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RemoteAPI {

	/**
	 * Vraca sve cepanice pojedinog organizma.
	 * 
	 * @return
	 */
	public TripticMassOrganismResult getOrganisamsTripticMass(String processID);

	/**
	 * Starta trazenje triptickog organizma.
	 * 
	 * @param taxID
	 * @return
	 */
	public String startSearchTripticMassOrganisms(int taxID);

	/**
	 * Vraca zadnji cache blast result
	 * 
	 * @return
	 */
	public BlastOutput lastBlast();

	/**
	 * Salje info o svojim pluginovima i server vraca koje pluginove da si skine
	 * 
	 * @param clientPligin
	 * @return
	 */
	public ArrayList<Plugin> whichPluginDownload(List<Plugin> clientPligin);

	/**
	 * Skida plugin sa servera u bynarnom obliku.
	 * 
	 * @param p
	 * @return
	 */
	public byte[] downloadPlugin(Plugin p);

	public BlastOutput blast(BlastInput in);

	/**
	 * Pokrece long running blast
	 * 
	 * @param in
	 * @return ID procesa
	 */
	public String blastLong(BlastInput in);

	/**
	 * Vraca true ako je process gotov
	 * 
	 * @param processID
	 * @return
	 */
	public boolean isFinishProcess(String processID);

	public BlastOutput getBlastResult(String processID);

	/**
	 * Ubija process ID.
	 * 
	 * @param processID
	 */
	public void killProcess(String processID);

	public List<Taxon> searchTaxon(String query);

	/**
	 * Pokrece global job blast
	 * 
	 * @return process ID
	 */
	public String blastGlobal(BlastInput in, MSSearchBlastParamModel params, List<PeakFile> msList);

	public BlastOutputGlobal getBlastGlobalResult(String processID);

	public String startUltraBlast(UFB_Params params, List<PeakFile> msList);

	/**
	 * Starta UFB2
	 * 
	 * @param params
	 * @param msList
	 *            Lista svih MS-a u workspace!
	 * @return ID od procesa
	 */
	public String startUltraBlast2(UFB_Params params, ArrayList<MS> msList);

	public UFB_Result getUltraFastBlastResult(String processID);

	/**
	 * Matcha sve MS pikove iz workspace sa cepanicam GI-eva. Pretpostavka je da
	 * je cijeli workspace jedna vrsta!
	 * 
	 * @param allWorkspaceMSpeaks
	 *            , ili samo pikovi jednog MS-a ako je gel based
	 * @param gis
	 *            givei jedne vrste koji su pogodjeni.
	 * @return processID
	 */
	public String startMSmatch(double[] allWorkspaceMSpeaks, long[] gis, double plusMinus);

	/**
	 * Vraca rezultat MS matcha
	 * 
	 * @param processID
	 * @return
	 */
	public UFB_MSmatchResult getMSmatchResult(String processID);

	/**
	 * Vraca rezultat UFB2 ako je process gotov.
	 * 
	 * @param procID
	 * @return
	 */
	public SearchResult getUFB2Result(String procID);

	/**
	 * Vraca mapu da GI => description iz baze
	 * 
	 * @param allGis
	 * @return
	 */
	public Map<Long, String> getDescription(HashSet<Long> allGis);

	/**
	 * 
	 * @param params
	 * @param msList
	 * @return ID procesa
	 */
	public String startUBF3(UFB_Params params, Set<PeakFile> msList);

	/**
	 * Starta MPH
	 * 
	 * @param params
	 * @param msList
	 * @return
	 */
	// public String startMPH(MphParameters params, PeakFile ms) ;

	/**
	 * Starta ali sve ms-ove
	 * 
	 * @param params
	 * @param msList
	 * @return process ID
	 */
	public String startMultiMsMph2(MphParameters params);

	public UFB3result getUBF3result(String id);

	public void addStatistic(UserStatistic dataStat);

	/**
	 * Vraca sve proteine koji imaju ovu cepanicu u sebi.
	 * 
	 * @param cepanica
	 * @return
	 */
	public ArrayList<UFB3GiTax> getOtherGiTax(String cepanica);

	public MphResult getMphResult(String id);

	public Map<Long, Boolean> gambergerThis(Map<Long, String> gambergerQuery);
	
	/**
	 * Drugi program od gambergera koji vraca i signifikantnost.
	 * @param gambergerQuery ID => CSV
	 * @return
	 */
	public Map<String, GambResult> gambergerPrediction2(Map<String, String> gambergerQuery);
	
	/**
	 * Treci program od gambergera koji vraca i signifikantnost i optimiziran za sslanje podatakas.
	 * @param gambergerQuery ID => CSV
	 * @return
	 */
//	public Map<String, GambResult> gambergerPrediction3(ArrayList<GambTaxCSV> gambTaxs);
	
	
	public CheckSingleHitResults checkSingleHit(CheckSingleHitParams param);
	
	
	public String getProteinSequence(long gi);
	
	

}
