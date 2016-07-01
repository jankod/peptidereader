package hr.semgen.masena.share.mph;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MphHit {
	public MphBlastHit blastHit;
	public long msmsID;
	public long gi;

	public int taxID;
	public int seqID;

	/**
	 * SS brat
	 */
	private MphHit ss;

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE, false, false).toString();
	}

	public String createProteinPart(String seq) {
		int from = blastHit.subjectStart - 1;
		int to = blastHit.subjectEnd;

		int leftPos = from;
		for (int i = from; i > 0; i--) {
			final char aa = seq.charAt(i);
			if (aa == 'K' || aa == 'R') {
				leftPos = i;
				break;
			}
			leftPos = i;
		}

		int uljevo = from - leftPos;
		uljevo = Math.min(uljevo, 2 - 1);

		int rightPos = to;
		for (int i = to; i < seq.length(); i++) {
			final char aa = seq.charAt(i);
			if (aa == 'K' || aa == 'R') {
				rightPos = i;
				// dodati jos plus 1
				if (rightPos + 1 <= seq.length()) {
					rightPos++;
				}

				break;
			}

			rightPos = i;
		}
		rightPos = Math.min(rightPos + 1, seq.length());

		String subseq = seq.substring(leftPos, rightPos);
		return subseq;
	}

	/*
	 * public float calcIdentitiesPercent() { final float ide =
	 * blastHit.identical; final float lf = blastHit.subjectSeq.length(); float
	 * res = ide / lf; System.out.println(res); res = (float) (Math.round(res *
	 * 100F / 100F)); return res; }
	 */

	public MphBlastHit getBlastHit() {
		return blastHit;
	}

	public void setBlastHit(MphBlastHit blastHit) {
		this.blastHit = blastHit;
	}

	public long getMsmsID() {
		return msmsID;
	}

	public MphHit getSs() {
		return ss;
	}

	public void setSs(MphHit ss) {
		this.ss = ss;
	}

	public void setMsmsID(long msmsID) {
		this.msmsID = msmsID;
	}

	public long getGi() {
		return gi;
	}

	public void setGi(long gi) {
		this.gi = gi;
	}

	public int getTaxID() {
		return taxID;
	}

	public void setTaxID(int taxID) {
		this.taxID = taxID;
	}

	public int getSeqID() {
		return seqID;
	}

	public void setSeqID(int seqID) {
		this.seqID = seqID;
	}

}
