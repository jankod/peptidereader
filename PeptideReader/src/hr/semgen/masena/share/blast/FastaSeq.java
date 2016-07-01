package hr.semgen.masena.share.blast;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FastaSeq implements Serializable {

	private static final long serialVersionUID = 1L;

	private DigestInformation digestInformation;

	/**
	 * Jedna seq ima vise GI i vise desc: np: ./blastdbcmd -entry 17943209
	 * -outfmt '%g %t'
	 */
	public Map<Integer, String> gisDesc = new HashMap<Integer, String>();

	public String header;
	public String seq;

	public FastaSeq(String header, String seq) {
		this.header = header;
		this.seq = seq;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		result = prime * result + ((seq == null) ? 0 : seq.hashCode());
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
		FastaSeq other = (FastaSeq) obj;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		if (seq == null) {
			if (other.seq != null)
				return false;
		} else if (!seq.equals(other.seq))
			return false;
		return true;
	}

	public FastaSeq() {
	}

	@Override
	public String toString() {
		if (seq != null && seq.length() > 30) {
			return header + "\n" + seq.subSequence(0, 29) + " ...";
		}
		return header + "\n" + seq;
	}

	private static final Charset charset = Charset.forName("ISO-8859-1");
	public final static Pattern ctrlAPattern = Pattern.compile("\\p{Cntrl}");

	/**
	 * Vraca parsirani header sa listom GI i descriptiona. Smatra se da je
	 * header pdvojen Ctrl + A znakom.
	 * 
	 * @return
	 */
	public GiDesc[] getGiDescriptionFromHeader() {
		final String[] split = ctrlAPattern.split(header);
		GiDesc[] gids = new GiDesc[split.length];
		for (int i = 0; i < split.length; i++) {
			GiDesc gd = getGiDesc(split[i]);
			gids[i] = gd;

		}
		return gids;

	}

	/**
	 * Vraca
	 * 
	 * @return
	 */
	private GiDesc getGiDesc(String s) {
		StringBuilder gi = new StringBuilder(10);
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if (c == '|') { // search for GI
				for (int j = i + 1; j < s.length(); j++) {
					final char g = s.charAt(j);
					if (g == '|') { // search for desc
						for (int k = j + 1; k < s.length(); k++) {
							final char desc = s.charAt(k);
							if (desc == ' ') {
								// sve dalje je desc
								GiDesc gd = new GiDesc();
								gd.gi = Integer.valueOf(gi.toString());
								gd.desc = s.substring(k);
								return gd;
							}
						}

						break;
					} else {
						gi.append(g);
					}
				}
			}

		}
		return null;
	}

	/**
	 * Na svakih 40 znakova dodaje novi red
	 * 
	 * @param seq2
	 * @return
	 */
	public static String formatSeq(String seq, String newLineChar) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < seq.length(); i++) {
			if (i % 80 == 0 && i != 0) {
				b.append(newLineChar);
			}
			b.append(seq.charAt(i));
		}
		return b.toString();
	}

	public void setDigestInformation(DigestInformation digestInformation) {
		this.digestInformation = digestInformation;
	}

	public DigestInformation getDigestInformation() {
		return digestInformation;
	}

	/**
	 * 
	 * @author ja
	 * 
	 */
	public static class DigestInformation implements Serializable{
		private static final long serialVersionUID = 1L;

		/**
		 * Peptid koji se isao razlomiti i izracunati mase.
		 */
		public String peptide;

		/**
		 * Razlomljeni dio peptida koji se fita sa masom na masu prekursora
		 */
		public String peptideThatFitsMass;

		/**
		 * Masa peptida koja pase +-
		 */
		public double massFit;
	}
}
