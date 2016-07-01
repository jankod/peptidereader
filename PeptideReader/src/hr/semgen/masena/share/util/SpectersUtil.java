package hr.semgen.masena.share.util;

import java.util.UUID;

public class SpectersUtil {

	
	/**
	 * Stvara novi ID spektra. Valjda se nece ponoviti.
	 * @return
	 */
	public static long createNewSpectarID() {
		return UUID.randomUUID().getLeastSignificantBits() + System.currentTimeMillis();
	}

}
