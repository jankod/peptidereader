package hr.semgen.masena.ufb;

import java.io.Serializable;
import java.util.ArrayList;

public final class UFB_MSResult implements Serializable {

	private static final long serialVersionUID = 1L;

	public String name;
	public String id;

	public ArrayList<UFB_MSMSresult> msmsList = new ArrayList<UFB_MSMSresult>();

	@Override
	public String toString() {
		return name + " " + id;
	}
}
