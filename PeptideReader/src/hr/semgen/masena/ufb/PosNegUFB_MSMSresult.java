package hr.semgen.masena.ufb;

import java.io.Serializable;

public class PosNegUFB_MSMSresult implements Serializable {

	private static final long serialVersionUID = 1L;
	public UFB_MSMSresult positive;
	public UFB_MSMSresult negative;
	public long durration;
	
	public String msID;

}
