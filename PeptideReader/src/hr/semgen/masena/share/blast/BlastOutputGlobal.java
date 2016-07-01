package hr.semgen.masena.share.blast;




import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlastOutputGlobal implements Serializable {

	private static final long serialVersionUID = 1L;

	private String workspaceName;

	private Date dateCreated;

	private List<BlastOutputMS> outputMSs = new ArrayList<BlastOutputMS>();

	private final List<MSMSpeptid> msmsPeptideList;
	
	public List<MSMSpeptid> getMsmsPeptideList() {
		return msmsPeptideList;
	}
	
	public BlastOutputGlobal(List<MSMSpeptid> msmsPeptideList) {
		this.msmsPeptideList = msmsPeptideList;
	}

	public void setWorkspaceName(String workspaceName) {
		this.workspaceName = workspaceName;
	}

	public String getWorkspaceName() {
		return workspaceName;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setOutputMSs(List<BlastOutputMS> outputMSs) {
		this.outputMSs = outputMSs;
	}

	public List<BlastOutputMS> getOutputMSs() {
		return outputMSs;
	}

}
