package hr.semgen.masena.share.ufb3;

import java.io.Serializable;

/**
 * Za prenosenje gi i tax podataka
 * @author tag
 *
 */
public class UFB3GiTax implements Serializable{

	private static final long serialVersionUID = 1L;
	public long gi;
	public String taxDesc;
	public String protDesc;
	public int taxID;
	
	
	@Override
	public String toString() {
		return "UFB3GiTax [gi=" + gi + ", taxDesc=" + taxDesc + ", protDesc=" + protDesc + ", taxID=" + taxID + "]";
	}

}
