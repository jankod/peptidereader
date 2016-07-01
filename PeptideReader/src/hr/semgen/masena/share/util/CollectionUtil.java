package hr.semgen.masena.share.util;

import java.util.ArrayList;
import java.util.List;


public class CollectionUtil {

	
	/**
	 * Brise sve do forRemove u listi, ukljucujuci i njega.
	 * @param list lista iz koje se mice sve.
	 * @param forRemove ako je null, metoda nis ne radi, vec vraca 0.
	 * @return broj maknutih iz liste
	 */
	public static <T> int  removeFirstTo(List<T> list, T forRemove) {
		if(forRemove == null) {
			return 0;
		}
		
		List<T> removed = new ArrayList<T>();
		
		for (int i = 0; i < list.size(); i++) {
			T value = list.get(i);
			
			
			removed.add(value);
			if(value.equals(forRemove)) {
				break;
			}
		}
		list.removeAll(removed);
		return removed.size();
	}

	
	
}
