package hr.semgen.masena.share.mph;

import hr.semgen.masena.share.model.Peak;
import hr.semgen.masena.share.model.PeakFile;

import java.util.TreeMap;

public class MphMsms {

	private float precursorMass;
	private boolean isPositive = false;
	
	
	private long id;
	
	private TreeMap<Float, MphPeak> peaks;
	
	public MphMsms() {
	}
	public MphMsms(PeakFile msms) {
		peaks = new TreeMap<Float, MphPeak>();
		if(msms.isMS()) {
			throw new RuntimeException("This is not MSMS!");
		}
		id = msms.getId();
		this.isPositive = msms.isPositive();
		this.precursorMass = msms.getMassPrecursor();
		
		for (Peak peak : msms.getAllPeaks()) {
			MphPeak p = new MphPeak(peak);
			this.peaks.put(p.mass, p);
		}
		
		if(!this.peaks.containsKey(precursorMass)) {
			throw new RuntimeException("Ne sadrzi masu prekursora!");
		}
	}
	
	
	public TreeMap<Float, MphPeak> getPeaks() {
		return peaks;
	}
	
	public long getId() {
		return id;
	}
	public boolean isPositive() {
		return isPositive;
	}
	public boolean isNegative() {
		return !isPositive();
	}
	
	public float getPrecursorMass() {
		return precursorMass;
	}
}
