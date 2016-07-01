package hr.semgen.masena.share.model;

import java.io.Serializable;

/**
 * Lite peak koji sluzi za b y ione, da ne prenosi se puno po mrezi
 * @author tag
 *
 */
public final class PeakLite implements Serializable {
	private static final long serialVersionUID = 5341111705925180897L;

	private int height;
	private float centroidMass;
	
	
	
	public PeakLite(int h, float mass) {
		setHeight(h);
		setCentroidMass(mass);
	}

	
	public PeakLite() {
	}
	

	
	@Override
	public String toString() {
		return "PeakLite [height=" + height + ", mass=" + centroidMass + "]";
	}


	public PeakLite(Peak peak) {
		height = peak.getHeight();
		centroidMass = peak.getCentroidMass();
	}

	public void setCentroidMass(float centroidMass) {
		this.centroidMass = centroidMass;
	}



	public float getCentroidMass() {
		return centroidMass;
	}



	public void setHeight(int height) {
		this.height = height;
	}



	public int getHeight() {
		return height;
	}
}
