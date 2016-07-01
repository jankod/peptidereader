package hr.semgen.masena.share.prediction;

import java.io.Serializable;

public class GambResult implements Serializable {

	public GambResult(boolean good, int signif) {
		this.good = good;
		significance = signif;
	}

	private static final long serialVersionUID = 1L;

	public boolean good;
	public int significance;

}
