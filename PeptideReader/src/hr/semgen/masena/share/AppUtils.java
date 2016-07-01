package hr.semgen.masena.share;

public class AppUtils {
	/**
	 * Zaokruzuje na dvije decimale
	 * @param d
	 * @return
	 */
	public static double round(double d) {
		return  (double)Math.round(d * 100) / 100;
	}
	
	public static void main(String[] args) {
		System.err.println(round(23.456783339));
	}

	
}
