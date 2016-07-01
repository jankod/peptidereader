package hr.semgen.masena.share.util;


public class AssertUtil {

	public static void notNull(Object o) {
		if(o == null) {
			throw new NullPointerException("Null je");
		}
	}
	
	public static void notNull(Object o, String msg) {
		if(o == null) {
			throw new NullPointerException(msg);
		}
	}

}
