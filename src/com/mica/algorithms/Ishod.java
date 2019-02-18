package com.mica.algorithms;

import com.mica.main.Akcija;
import com.mica.main.Stanje;

public class Ishod {
	private Stanje staroStanje;
	private Akcija akcija;
	private double nagrada;
	private Stanje novoStanje;
	
	public Ishod() {
		
	}
	
	public Ishod(Stanje staroStanje, Akcija akcija, double nagrada, Stanje novoStanje) {
		this.staroStanje = staroStanje;
		this.akcija = akcija;
		this.nagrada = nagrada;
		this.novoStanje = novoStanje;
	}

	public Stanje getStaroStanje() {
		return staroStanje;
	}

	public void setStaroStanje(Stanje staroStanje) {
		this.staroStanje = staroStanje;
	}

	public Akcija getAkcija() {
		return akcija;
	}

	public void setAkcija(Akcija akcija) {
		this.akcija = akcija;
	}

	public double getNagrada() {
		return nagrada;
	}

	public void setNagrada(double nagrada) {
		this.nagrada = nagrada;
	}

	public Stanje getNovoStanje() {
		return novoStanje;
	}

	public void setNovoStanje(Stanje novoStanje) {
		this.novoStanje = novoStanje;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((akcija == null) ? 0 : akcija.hashCode());
		long temp;
		temp = Double.doubleToLongBits(nagrada);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((novoStanje == null) ? 0 : novoStanje.hashCode());
		result = prime * result + ((staroStanje == null) ? 0 : staroStanje.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ishod other = (Ishod) obj;
		if (akcija != other.akcija)
			return false;
		if (Double.doubleToLongBits(nagrada) != Double.doubleToLongBits(other.nagrada))
			return false;
		if (novoStanje == null) {
			if (other.novoStanje != null)
				return false;
		} else if (!novoStanje.equals(other.novoStanje))
			return false;
		if (staroStanje == null) {
			if (other.staroStanje != null)
				return false;
		} else if (!staroStanje.equals(other.staroStanje))
			return false;
		return true;
	}

	
	public String toStringForNN() {
		StringBuilder sb = new StringBuilder("");
		sb.append(staroStanje.toStringForNN());
		sb.append(":");
		sb.append(akcija.ordinal());
		sb.append(":");
		sb.append(nagrada);
		sb.append(":");
		sb.append(novoStanje.toStringForNN());
		
		return sb.toString();
	}
	
}
