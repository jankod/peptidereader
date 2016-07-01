package hr.semgen.ms.algor;

import hr.semgen.masena.share.util.ProteinConstants;
import hr.semgen.masena.share.util.ProteinsUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Algoritmi za kalkulator mase peptida.
 * 
 * @author tag
 * 
 */
public class CalculatorAlgo {
	private static final Logger log = LoggerFactory.getLogger(CalculatorAlgo.class);

	public static enum DERIVATIZATION {
		CAF(ProteinConstants.CAFF_NEW), H3(ProteinConstants.H3_NEW), H4 (ProteinConstants.H4);

		private double mass;

		DERIVATIZATION(double mass) {
			this.mass = mass;
		}

		public double getMass() {
			return mass;
		}

	}

	public static enum PeptideIon {
		DERIV_NEG, DERIV_POS, NEG, POS
	}

	private String peptide;

	private DERIVATIZATION derivatization = DERIVATIZATION.CAF;
	private PeptideIon ionType;

	public CalculatorAlgo(String peptide, PeptideIon ionType) {
		this.peptide = peptide;
		this.ionType = ionType;
	}

	public double calc0charge() {
		double m0 = ProteinsUtils.get().calcPeptideMassZeroo(peptide);
		switch (ionType) {
		case POS:
			return m0;
		case NEG:
			return m0;
		case DERIV_NEG:
			// log.debug("deriv neg");
			return m0 + derivatization.getMass();// ProteinConstants.CAFF_NEW;
		case DERIV_POS:
			//log.debug("deriv pos");
			return m0 + derivatization.getMass(); // ProteinConstants.CAFF_NEW;
		default:
			log.error("wrong ion type {}", ionType);
			throw new RuntimeException("wron ion type " + ionType);
		}
	}

	public double calc1charge() {
		double m0 = ProteinsUtils.get().calcPeptideMassZeroo(peptide);
		switch (ionType) {
		case POS:
			return m0 + ProteinConstants.PROTON_MASS;
		case NEG:
			return m0 - ProteinConstants.PROTON_MASS;
		case DERIV_NEG:
			return m0 + derivatization.getMass() - ProteinConstants.PROTON_MASS;
		case DERIV_POS:
			return m0 + derivatization.getMass() + ProteinConstants.PROTON_MASS;
		default:
			log.error("wrong ion type {}", ionType);
			throw new RuntimeException("wron ion type " + ionType);
		}
	}

	public double calc2charge() {
		double m0 = ProteinsUtils.get().calcPeptideMassZeroo(peptide);
		switch (ionType) {
		case POS:
			return m0 / 2d + ProteinConstants.PROTON_MASS;
		case NEG:
			return m0 / 2d - ProteinConstants.PROTON_MASS;
		case DERIV_NEG:
			return (m0 + derivatization.getMass()) / 2d - ProteinConstants.PROTON_MASS;
		case DERIV_POS:
			return (m0 + derivatization.getMass()) / 2d + ProteinConstants.PROTON_MASS;
		default:
			log.error("wrong ion type {}", ionType);
			throw new RuntimeException("wron ion type " + ionType);
		}
	}

	public double calc3charge() {
		double m0 = ProteinsUtils.get().calcPeptideMassZeroo(peptide);
		switch (ionType) {
		case POS:
			return m0 / 3d + ProteinConstants.PROTON_MASS;
		case NEG:
			return m0 / 3d - ProteinConstants.PROTON_MASS;
		case DERIV_NEG:
			return (m0 + derivatization.getMass()) / 3d - ProteinConstants.PROTON_MASS;
		case DERIV_POS:
			return (m0 + derivatization.getMass()) / 3d + ProteinConstants.PROTON_MASS;
		default:
			log.error("wrong ion type {}", ionType);
			throw new RuntimeException("wron ion type " + ionType);
		}
	}

	public double calc4charge() {
		double m0 = ProteinsUtils.get().calcPeptideMassZeroo(peptide);
		switch (ionType) {
		case POS:
			return m0 / 4d + ProteinConstants.PROTON_MASS;
		case NEG:
			return m0 / 4d - ProteinConstants.PROTON_MASS;
		case DERIV_NEG:
			return (m0 + derivatization.getMass()) / 4d - ProteinConstants.PROTON_MASS;
		case DERIV_POS:
			return (m0 + derivatization.getMass()) / 4d + ProteinConstants.PROTON_MASS;
		default:
			log.error("wrong ion type {}", ionType);
			throw new RuntimeException("wron ion type " + ionType);
		}
	}

	public void setIonType(PeptideIon ionType) {
		this.ionType = ionType;
	}

	public PeptideIon getIonType() {
		return ionType;
	}

	public void setPeptide(String peptide) {
		this.peptide = peptide;
	}

	public String getPeptide() {
		return peptide;
	}

	/**
	 * Vraca text + ili -
	 * 
	 * @return
	 */
	public String getTextCharge() {
		switch (ionType) {
		case POS:
			return "+";
		case NEG:
			return "-";
		case DERIV_NEG:
			return "-";
		case DERIV_POS:
			return "+";
		default:
			log.error("wrong ion type {}", ionType);
			throw new RuntimeException("wron ion type " + ionType);
		}
	}

	public void setDerivatization(DERIVATIZATION derivatization) {
		this.derivatization = derivatization;
	}

	public DERIVATIZATION getDerivatization() {
		return derivatization;
	}
}
