package hr.semgen.masena.share.model;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class NasaoProteina implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Peak b;
	public String proteini = "";

	@Override
	public String toString() {
		return proteini;
	}

	public List<NadjenaAA> proteiniList = new LinkedList<NadjenaAA>();

	public void addProtein(NadjenaAA p) {
		proteiniList.add(p);
	}
}
