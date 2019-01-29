package com.mica.main;

public class StanjeAkcija {
	private Stanje stanje;
	private Akcija akcija;
	
	public StanjeAkcija() {
		
	}
	
	public StanjeAkcija(Stanje stanje, Akcija akcija) {
		this.stanje = stanje;
		this.akcija = akcija;
	}

	public Stanje getStanje() {
		return stanje;
	}

	public void setStanje(Stanje stanje) {
		this.stanje = stanje;
	}

	public Akcija getAkcija() {
		return akcija;
	}

	public void setAkcija(Akcija akcija) {
		this.akcija = akcija;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((akcija == null) ? 0 : akcija.hashCode());
		result = prime * result + ((stanje == null) ? 0 : stanje.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof StanjeAkcija) {
			StanjeAkcija sa = (StanjeAkcija) obj;
			
			return stanje.equals(sa.stanje) && akcija == sa.akcija;
		}
		
		return false;
	}

	@Override
	public String toString() {
		return stanje + "|" + akcija.name();
	}
}
