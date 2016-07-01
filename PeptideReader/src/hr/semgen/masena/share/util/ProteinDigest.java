package hr.semgen.masena.share.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ProteinDigest {

	public static void main(String[] args) {
		String prot = "RICNFM";
		System.out.println(prot + "\n---------------------");

		final List<String> res = tripsyn(prot, 6, 30);
		for (String string : res) {
			System.out.println(string);
		}
		// System.out.println("============================");
		// final List<String> r = ProtDigetsUtil.digestProtein(prot);
		// for (String string : r) {
		// System.out.println(string);
		// }

	}

	/**
	 * Super brza metoda za cepanje sa 1 miss clevage. Cepa iza R ili K ako
	 * poslje nije P. Moraju biti velika slova!
	 * 
	 * @param prot
	 * @param min
	 * @param max
	 * @return
	 */
	public static List<String> tripsyn(String prot, int min, int max) {
		final int numberOfRandK = StringUtils.countMatches(prot, "R") + StringUtils.countMatches(prot, "K");
		if (numberOfRandK == 0) {
			List<String> result = new ArrayList<String>(1);
			if (prot.length() >= min && prot.length() <= max)
				result.add(prot);
			return result;
		}
		List<String> result = new ArrayList<String>(numberOfRandK * 3 + 2);

		if (prot.length() < min) {
			// result.add(prot);
			return result;
		}

		String lastChunk = null;
		int lastPos = 0;

		for (int i = 0; i < prot.length(); i++) {
			final char charAt = prot.charAt(i);

			if (charAt == 'R' || charAt == 'K') {
				if (prot.length() > i + 1) {
					final char nextChar = prot.charAt(i + 1);
					if (nextChar == 'P') {
						continue;
					}
				}

				final String chunk = prot.substring(lastPos, i + 1);
				if (chunk.length() >= min && chunk.length() <= max) {
					result.add(chunk);
				}
				lastPos = i + 1;
				if (lastChunk != null) {
					int size = lastChunk.length() + chunk.length();
					if (size >= min && size <= max)
						result.add(lastChunk + chunk);
				}

				lastChunk = chunk;
			}

		}

		// dodati jos zadnji chuk
		if (lastPos < prot.length()) {
			final String ostatak = prot.substring(lastPos);
			if (lastChunk != null) {
				String n = lastChunk + ostatak;
				if (n.length() >= min && n.length() <= max) {
					result.add(n);
				}
				// i sami komad zadnji staviti
				if (ostatak.length() >= min && ostatak.length() <= max) {
					result.add(ostatak);
				}

			} else {
				String n = ostatak;
				if (n.length() >= min && n.length() <= max) {
					result.add(n);
				}
			}
		}

		// dodati miss clevage

		return result;
	}
}
