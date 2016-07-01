package hr.semgen.masena.share.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class PeptidDTO implements Serializable {

	private static final long serialVersionUID = -5077021753851608054L;
	private NadjenaAA[] nadjene;

	public PeptidDTO(NadjenaAA[] nadjene) {
		this.nadjene = nadjene;
	}

	public static PeptidDTO toPeptideDTO(Peptid p) {
		NadjenaAA[] arr = p.getAsListOriginal().toArray(new NadjenaAA[p.getAsListOriginal().size()]);
		return new PeptidDTO(arr);
	}

	public static Peptid fromPeptideDTO(PeptidDTO dto) {
		Peptid p = new Peptid();
		for (NadjenaAA n : dto.nadjene) {
			p.addNew(n);
		}
		return p;
	}
	
	@Override
	public String toString() {
		String s  = "";
		if(nadjene != null) {
			for(NadjenaAA a: nadjene) {
				s+= a.aa;
			}
		}
		
		return s;
	}

	public String getQuery() {
		String s  = "";
		if(nadjene != null) {
			for(NadjenaAA a: nadjene) {
				s+= a.aa;
			}
		}
		return s;		
	}
	
	public String getRewerseQuery() {
		return StringUtils.reverse(getQuery());
	}
}
