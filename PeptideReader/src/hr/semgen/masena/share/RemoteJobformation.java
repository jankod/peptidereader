package hr.semgen.masena.share;

import hr.semgen.masena.share.model.PeakFile;
import hr.semgen.masena.share.mph.MphParameters;
import hr.semgen.masena.ufb.UFB_Params;

import java.io.Serializable;
import java.util.Collection;

/**
 * Informacije o procesu pokrenute, ali ne parametri.
 */
public abstract class RemoteJobformation implements Serializable {

	private static final long serialVersionUID = 1L;

	String workspaceName;
	long duration;
	Collection<PeakFile> msList;
	String jobName;
	String remoteIP;

	public String getRemoteIP() {
		return remoteIP;
	}

	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}

	public String getWorkspaceName() {
		return workspaceName;
	}

	public void setWorkspaceName(String workspaceName) {
		this.workspaceName = workspaceName;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public void setMsList(Collection<PeakFile> msList) {
		this.msList = msList;
	}

	public Collection<PeakFile> getMsList() {
		return msList;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
}

class UFB3JobInfo extends RemoteJobformation {
	private static final long serialVersionUID = 1L;
	UFB_Params params;

}

class MPHJobInfo extends RemoteJobformation {
	private static final long serialVersionUID = 1L;
	MphParameters params;

}
