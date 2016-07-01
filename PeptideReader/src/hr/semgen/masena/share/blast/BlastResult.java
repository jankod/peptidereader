package hr.semgen.masena.share.blast;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BlastResult implements Serializable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alignmentLength == null) ? 0 : alignmentLength.hashCode());
		result = prime * result + Float.floatToIntBits(bitScore);
		result = prime * result + Float.floatToIntBits(eValue);
		result = prime * result + ((gapOpens == null) ? 0 : gapOpens.hashCode());
		result = prime * result + ((mismatches == null) ? 0 : mismatches.hashCode());
		result = prime * result + Float.floatToIntBits(percentIdentity);
		result = prime * result + queryEnd;
		result = prime * result + ((querySeq == null) ? 0 : querySeq.hashCode());
		result = prime * result + queryStart;
		result = prime * result + subjectEnd;
		result = prime * result + subjectGI;
		result = prime * result + ((subjectID == null) ? 0 : subjectID.hashCode());
		result = prime * result + ((subjectSeq == null) ? 0 : subjectSeq.hashCode());
		result = prime * result + subjectStart;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlastResult other = (BlastResult) obj;
		if (alignmentLength == null) {
			if (other.alignmentLength != null)
				return false;
		} else if (!alignmentLength.equals(other.alignmentLength))
			return false;
		if (Float.floatToIntBits(bitScore) != Float.floatToIntBits(other.bitScore))
			return false;
		if (eValue != other.eValue)
			return false;
		if (gapOpens == null) {
			if (other.gapOpens != null)
				return false;
		} else if (!gapOpens.equals(other.gapOpens))
			return false;
		if (mismatches == null) {
			if (other.mismatches != null)
				return false;
		} else if (!mismatches.equals(other.mismatches))
			return false;
		if (Float.floatToIntBits(percentIdentity) != Float.floatToIntBits(other.percentIdentity))
			return false;
		if (queryEnd != other.queryEnd)
			return false;
		if (querySeq == null) {
			if (other.querySeq != null)
				return false;
		} else if (!querySeq.equals(other.querySeq))
			return false;
		if (queryStart != other.queryStart)
			return false;
		if (subjectEnd != other.subjectEnd)
			return false;
		if (subjectGI != other.subjectGI)
			return false;
		if (subjectID == null) {
			if (other.subjectID != null)
				return false;
		} else if (!subjectID.equals(other.subjectID))
			return false;
		if (subjectSeq == null) {
			if (other.subjectSeq != null)
				return false;
		} else if (!subjectSeq.equals(other.subjectSeq))
			return false;
		if (subjectStart != other.subjectStart)
			return false;
		return true;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this).toString();
	}

	/**
	 * gi|324114589|gb|EGC08557.1|
	 */
	public String subjectID;

	/**
	 * % identity
	 */
	public float percentIdentity;

	public String alignmentLength;

	public String mismatches;

	public String gapOpens;

	public int queryStart;
	public int queryEnd;

	public int subjectStart;
	public int subjectEnd;

	public float eValue;

	public float bitScore;

	/**
	 * Query koji je kao blast napravio
	 */
	public String querySeq;

	public String subjectSeq;

	public int subjectGI;

	public FastaSeq fastaSeq;

	/**
	 * Originalna query pept sequenca
	 */
	public String originalQuerySeq;

	/**
	 * Total number of gaps
	 */
	public int gaps;

	/**
	 * Percentage of positive-scoring matches
	 */
	public double percentageOfpositiveMatches;

	/**
	 * Ovo je samo za blast report tablicu prljavo rjesenje
	 */
	public Double cacheBionMatch;

	/**
	 * Ovo je prjavo rjesenje za blast resport
	 */
	public Double cacheMsMatch;

	/**
	 * Number za tablicu u blast reportu, pocinje brojat kao sto sortira po
	 * b-ionima. Prvo je null, da se zna da nije postavljeno.
	 */
	public Integer cacheNum = null;

	/**
	 * nident means Number of identical matches
	 */
	public int identical;
}
