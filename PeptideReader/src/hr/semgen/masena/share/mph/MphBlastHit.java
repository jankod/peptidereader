package hr.semgen.masena.share.mph;

import hr.semgen.masena.share.TwoValue;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class MphBlastHit implements Serializable {

	
	private static final long serialVersionUID = 1L;

	/**
	 * Parsira nesto vakvog DKPNHPESPPR_6765148476772120206
	 * 
	 * @return seq i MSMS_ID
	 */
	public static TwoValue<String, Long> extractQuerySeqAndMsmsIdFromQueryID(String queryID) {
		if(queryID == null) {
			throw new NullPointerException("queryID je null");
		}
		
		final String[] split = StringUtils.split(queryID, "_");
		return new TwoValue<String, Long>(split[0].trim(), Long.parseLong(split[1].trim()));
	}
	
	
	/**
	 * DKPNHPESPPR_6765148476772120206
	 * SEQ_MsmsId
	 */
	public String queryID;

	/**
	 * Ekstrahirano iz queryID
	 */
	public String querySequenceFromHeader ;

	/**
	 * Ekstrahirano iz queryID
	 */
	public long msmsIDFromHeader;

	/**
	 * 
	 * seqID zapravo
	 */
	public String subjectID;

	
	/**
	 * percentageOfIdenticalMatches
	 */
	public float percentIdentity;

	public int alignmentLength;

	public int mismatches;

	public int gapOpens;

	/**
	 * q. start
	 */
	public int queryStart;

	/**
	 * q. end
	 */
	public int queryEnd;

	/**
	 * q. start
	 */
	public int subjectStart;

	/**
	 * s. end
	 */
	public int subjectEnd;

	public float eValue;

	public float bitScore;

	/**
	 * Aligned part of query sequence
	 */
	public String querySeqAligned;

	/**
	 *  Aligned part of subject sequence
	 */
	public String subjectSeqAligned;

	/**
	 * Total number of gaps
	 */
	public int totalGaps;

	/**
	 * Percentage of positive-scoring matches
	 */
	public float percentageOfpositiveMatches;

	public int identical;

	public int positive;


	
	@Override
	public String toString() {
		return queryID + " subjSeq: "+ subjectSeqAligned + " ident: "+identical + " % posi: "+ percentageOfpositiveMatches + " %identity "+ percentIdentity;
	}


	public String getQueryID() {
		return queryID;
	}


	public void setQueryID(String queryID) {
		this.queryID = queryID;
	}


	public String getQuerySequenceFromHeader() {
		return querySequenceFromHeader;
	}


	public void setQuerySequenceFromHeader(String querySequenceFromHeader) {
		this.querySequenceFromHeader = querySequenceFromHeader;
	}


	public long getMsmsIDFromHeader() {
		return msmsIDFromHeader;
	}


	public void setMsmsIDFromHeader(long msmsIDFromHeader) {
		this.msmsIDFromHeader = msmsIDFromHeader;
	}


	public String getSubjectID() {
		return subjectID;
	}


	public void setSubjectID(String subjectID) {
		this.subjectID = subjectID;
	}


	public float getPercentIdentity() {
		return percentIdentity;
	}


	public void setPercentIdentity(float percentIdentity) {
		this.percentIdentity = percentIdentity;
	}


	public int getAlignmentLength() {
		return alignmentLength;
	}


	public void setAlignmentLength(int alignmentLength) {
		this.alignmentLength = alignmentLength;
	}


	public int getMismatches() {
		return mismatches;
	}


	public void setMismatches(int mismatches) {
		this.mismatches = mismatches;
	}


	public int getGapOpens() {
		return gapOpens;
	}


	public void setGapOpens(int gapOpens) {
		this.gapOpens = gapOpens;
	}


	public int getQueryStart() {
		return queryStart;
	}


	public void setQueryStart(int queryStart) {
		this.queryStart = queryStart;
	}


	public int getQueryEnd() {
		return queryEnd;
	}


	public void setQueryEnd(int queryEnd) {
		this.queryEnd = queryEnd;
	}


	public int getSubjectStart() {
		return subjectStart;
	}


	public void setSubjectStart(int subjectStart) {
		this.subjectStart = subjectStart;
	}


	public int getSubjectEnd() {
		return subjectEnd;
	}


	public void setSubjectEnd(int subjectEnd) {
		this.subjectEnd = subjectEnd;
	}


	public float geteValue() {
		return eValue;
	}


	public void seteValue(float eValue) {
		this.eValue = eValue;
	}


	public float getBitScore() {
		return bitScore;
	}


	public void setBitScore(float bitScore) {
		this.bitScore = bitScore;
	}


	public String getQuerySeqAligned() {
		return querySeqAligned;
	}


	public void setQuerySeqAligned(String querySeqAligned) {
		this.querySeqAligned = querySeqAligned;
	}


	public String getSubjectSeqAligned() {
		return subjectSeqAligned;
	}


	public void setSubjectSeqAligned(String subjectSeqAligned) {
		this.subjectSeqAligned = subjectSeqAligned;
	}


	public int getTotalGaps() {
		return totalGaps;
	}


	public void setTotalGaps(int totalGaps) {
		this.totalGaps = totalGaps;
	}


	public float getPercentageOfpositiveMatches() {
		return percentageOfpositiveMatches;
	}


	public void setPercentageOfpositiveMatches(float percentageOfpositiveMatches) {
		this.percentageOfpositiveMatches = percentageOfpositiveMatches;
	}


	public int getIdentical() {
		return identical;
	}


	public void setIdentical(int identical) {
		this.identical = identical;
	}


	public int getPositive() {
		return positive;
	}


	public void setPositive(int positive) {
		this.positive = positive;
	}



	/**
	 * Ovo je zapravo subjectID
	 * @return
	 */
	public String getSeqID() {
		return subjectID;
	}
	
	

}
