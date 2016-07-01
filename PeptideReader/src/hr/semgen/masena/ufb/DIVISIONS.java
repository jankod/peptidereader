package hr.semgen.masena.ufb;

import java.io.Serializable;

public enum DIVISIONS implements Serializable {
	
	
	Bacteria(0), Invertebrates(1), Mammals(2), Phages(3), Plants(4), Primates(5), Rodents(6), Synthetic(7), Unassigned(
			8), Viruses(9), Vertebrates(10), Environmental_samples(11), Resistance(27), Target(28);

	public final short num;

	DIVISIONS(int num) {
		this.num = (short) num;

	}

}
