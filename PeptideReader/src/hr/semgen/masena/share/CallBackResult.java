package hr.semgen.masena.share;

/**
 * Povratni rezultat
 * 
 * @param <T>
 */
public abstract class CallBackResult<T> {

	public abstract void finish(T result);

	public void error(Throwable t, String msg) {
	}
}
