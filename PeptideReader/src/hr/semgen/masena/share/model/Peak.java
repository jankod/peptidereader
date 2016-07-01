package hr.semgen.masena.share.model;

import hr.semgen.masena.share.util.ProteinConstants;

import java.io.Serializable;
import java.text.DecimalFormat;

public final class Peak implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Ako je umjetni peak, tj. napravljen je da masa perkursora bude tu.
	 */
	private boolean dummy = false;

	/**
	 * Ako je selektiran u tabeli
	 */
	private boolean tableSelected = false;

	private int lastNumOrderColumnValue;

	/**
	 * masa fragmenta proteina
	 */
	private float centroidMass;

	/**
	 * Potencijalni pik koji odgovara nekom drugom piku
	 */
	private boolean potentionalPeak = false;

	
	/**
	 * Potencijalna aa koja odgovara nekoj drugoj
	 */
	private String potentionalAA = "";
	
	/**
	 * visina pika, intenzitet signala
	 */
	private int height;

	private String name;

	private int order;

	private static final DecimalFormat format = new DecimalFormat("##.#####");

	@Override
	public String toString() {
		return "[m:" + format.format(centroidMass) + " h:" + format.format(height) + "]";
	}
	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(centroidMass);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + height;
		return result;
	}

	/**
	 * Dva pika su jednaka ako imaju istu masu i visinu.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Peak other = (Peak) obj;
		if (Double.doubleToLongBits(centroidMass) != Double.doubleToLongBits(other.centroidMass))
			return false;
		if (height != other.height)
			return false;
		
		
		
		
		return true;
	}

	public Peak() {
	}
	public Peak(float centroidMass, int height) {

		this.centroidMass = centroidMass;
		this.height = height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public final int getHeight() {
		return height;
	}

	public void setCentroidMass(float centroidMass) {
		this.centroidMass = centroidMass;
	}

	public float getCentroidMass() {
		return centroidMass;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Integer getOrder() {
		return order;
	}

	public void setTableSelected(boolean tableSelected) {
		this.tableSelected = tableSelected;
	}

	public boolean isTableSelected() {
		return tableSelected;
	}

	public void setLastNumOrderColumnValue(int newNum) {
		this.lastNumOrderColumnValue = newNum;
	}

	public int getLastNumOrderColumnValue() {
		return lastNumOrderColumnValue;
	}

	
	
	
	/**
	 * Postavlja na true kad moze biti potencijalni odgovarajuci peak nkom selektiranome u tabeli
	 * @param potentionalPeak
	 */
	public void setPotentionalPeak(boolean potentionalPeak) {
		this.potentionalPeak = potentionalPeak;
	}

	public boolean isPotentionalPeak() {
		return potentionalPeak;
	}

	/**
	 * Postavlja ovaj pik kao pik napravljen kao masa perkursora.
	 * 
	 * @param dummy
	 */
	public void setDummy(boolean dummy) {
		this.dummy = dummy;
	}

	/**
	 * 
	 * @return true ako je ovo masa perkusora
	 */
	public boolean isDummy() {
		return dummy;
	}

	public void setPotentionalAA(String potentionalAA) {
		this.potentionalAA = potentionalAA;
	}

	public String getPotentionalAA() {
		return potentionalAA;
	}



	public void plusMirjanaH1() {
		this.centroidMass = (float) (this.centroidMass + ProteinConstants.MIRJANA_DELTA);
	}
	public void massPlusDelta(double delta) {
		this.centroidMass = (float) (this.centroidMass + delta);
	}

}
