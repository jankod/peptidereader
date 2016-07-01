package hr.semgen.masena.share;

import java.io.Serializable;


/**
 * Wrapper za dvije vrjednosti.
 * @param <T1>
 * @param <T2>
 */
public final class TwoValue<T1, T2> implements Serializable {

	private static final long serialVersionUID = -8475961153790066365L;
	private T1 t1;
	private T2 t2;

	public TwoValue(T1 t1, T2 t2) {
		this.setT1(t1);
		this.setT2(t2);
	}

	
	public void setT2(T2 t2) {
		this.t2 = t2;
	}

	public T2 getT2() {
		return t2;
	}

	public void setT1(T1 t1) {
		this.t1 = t1;
	}

	public T1 getT1() {
		return t1;
	}

}
