package hr.semgen.masena.share.ufb3;

import hr.semgen.masena.share.model.PeakFile;

import java.io.Serializable;
import java.util.Set;

public class UserStatistic implements Serializable {

	private static final long serialVersionUID = 1L;

	
	
	public String userCompName;
	public long time_analyse_start;
	public long time_analyse_stop;
	public String analyseName ;
	public String userIP;
	
	/**
	 * Workspace podaci
	 */
	public Set<PeakFile> workspaceMs;
}
