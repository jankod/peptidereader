package hr.semgen.masena.share.blast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Konbinatorika I i L a.a.
 * @author ja
 *
 */
public class IL_PeptideCalc {
	
	
	/**
	 * NOVO: vraca sam peptid od sada!
	 * Trazi kombinacije sa I i L jer imaju istu masu. Vraca i originalni pep u rezultatima.
	 * 
	 * @param pep
	 * @return
	 */
	public static Set<String> ILcombination(String pep) {
//		final HashSet<String> hashSet = new HashSet<String>();
//		hashSet.add(pep);
//		return hashSet;
		
		Set<String> result = new HashSet<String>();
		result.add(pep);
		for (int i = 0; i < pep.length(); i++) {
			final char c = pep.charAt(i);
			if (c == 'I') {
				replace("L", result, i);

			}
			if (c == 'L') {
				replace("I", result, i);
			}
		}

		return result;
	}

	private static void replace(String replacment, Set<String> result, int i) {
		List<String> newList = new ArrayList<String>();

		for (String s : result) {
			StringBuilder b = new StringBuilder(s);
			b.replace(i, i + 1, replacment);
			newList.add(b.toString());
		}
		result.addAll(newList);
	}
}
