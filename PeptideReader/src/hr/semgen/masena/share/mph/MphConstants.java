package hr.semgen.masena.share.mph;


public final class MphConstants {

	public static final float PROTON_MASS = 1.0072F; // 1.007825

	public static final float H2O = 18.010565F; // 18.010565

	public static final float CAFF = 247.9445F;

	public static final String ALL_AA = "ARNDCQEGHILKMFPSTWYV";

	// @formatter:off
	public static final float 
	G = 57.02146F, 
	A = 71.03711F,
	S = 87.03203F, 
	P = 97.05276F, 
	V = 99.06841F, 
	T = 101.0476F, 
	C = 103.0092F, 
	I = 113.0841F, 
	L = 113.0841F,
	N = 114.0429F, 
	D = 115.0269F, 
	Q = 128.0585F, 
	K = 128.0949F, 
	E = 129.0425F, 
	M = 131.0404F, 
	H = 137.0589F,
	F = 147.0684F, 
	R = 156.1011F, 
	Y = 163.0633F, 
	W = 186.0793F;
	// @formatter:on

	public final float getMassFromAAfast(char aa) {
		switch (aa) {
		case 'G':
			return G;
		case 'A':
			return A;
		case 'S':
			return S;
		case 'P':
			return P;
		case 'V':
			return V;
		case 'T':
			return T;
		case 'C':
			return C;
		case 'I':
			return I;
		case 'L':
			return L;
		case 'N':
			return N;
		case 'D':
			return D;
		case 'Q':
			return Q;
		case 'K':
			return K;
		case 'E':
			return E;
		case 'M':
			return M;
		case 'H':
			return H;
		case 'F':
			return F;
		case 'R':
			return R;
		case 'Y':
			return Y;
		case 'W':
			return W;
		case 'U':
			return 150.95364F;
		case 'O':
			return 114.07931F;
		case 'J':
			return 113.08406F;
		default:
			throw new RuntimeException("Koja je ovo a.a.: "+ aa);
		}
	}
}
