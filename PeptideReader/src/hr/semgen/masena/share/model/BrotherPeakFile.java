package hr.semgen.masena.share.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BrotherPeakFile implements Serializable {

	private static final long serialVersionUID = 1L;
	private PeakFile pos;
	private PeakFile neg;

	public void setNeg(PeakFile neg) {
		this.neg = neg;
	}

	public PeakFile getNeg() {
		return neg;
	}

	public void setPos(PeakFile pos) {
		this.pos = pos;
	}

	public PeakFile getPos() {
		return pos;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(pos).append(neg).toString();
	}
}
