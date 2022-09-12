package com.data;

import java.math.BigDecimal;

public class Zdan {

	 private BigDecimal kod_zdan = null; // Код
	 private BigDecimal kod_street = null; 
	
	 private String nomer_dom = null; 
	 private BigDecimal etagej = null;
	 private Street street= null;
	public BigDecimal getKod_zdan() {
		return kod_zdan;
	}
	public void setKod_zdan(BigDecimal kod_zdan) {
		this.kod_zdan = kod_zdan;
	}
	
	public String getNomer_dom() {
		return nomer_dom;
	}
	public void setNomer_dom(String nomer_dom) {
		this.nomer_dom = nomer_dom;
	}
	public BigDecimal getEtagej() {
		return etagej;
	}
	public void setEtagej(BigDecimal etagej) {
		this.etagej = etagej;
	}
	public BigDecimal getKod_street() {
		return kod_street;
	}
	public void setKod_street(BigDecimal kod_street) {
		this.kod_street = kod_street;
	}
	public Street getStreet() {
		return street;
	}
	public void setStreet(Street street) {
		this.street = street;
	}
	@Override
	public String toString() {
		//return getStreet().getGorod().getName()+", "+getStreet().getName_street()+", "+getNomer_dom();
		return getStreet().toString()+", "+getNomer_dom();
	} 
	 
}
