package hr.semgen.ms.algor;

public enum AminoAcid {

	G(57.02146F),
	A(71.03711F),
	S(87.03203F),
	P(97.05276F),
	V(99.06841F),
	T(101.0476F),
	C(103.0092F),
	I(113.0841F),
	L(113.0841F),
	N(114.0429F),
	D(115.0269F),
	Q(128.0585F),
	K(128.0949F),
	E(129.0425F),
	M(131.0404F),
	H(137.0589F),
	F(147.0684F),
	R(156.1011F),
	Y(163.0633F),
	W(186.0793F);
	
	private final float mass;

	private AminoAcid(float mass) {
		this.mass = mass;
	}

	public float mass() {
		return mass;
	}
	
}
