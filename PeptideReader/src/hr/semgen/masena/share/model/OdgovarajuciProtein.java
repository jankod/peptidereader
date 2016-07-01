package hr.semgen.masena.share.model;

import java.io.Serializable;

public class OdgovarajuciProtein implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private double mass;

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getMass() {
		return mass;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
