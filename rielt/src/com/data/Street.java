package com.data;

import java.math.BigDecimal;

public class Street {
	 private BigDecimal kod_street = null; // Код
	 private String name_street = null; // Код
	 private BigDecimal kod_gorod = null; 
	 private Gorod gorod = null;
	public BigDecimal getKod_street() {
		return kod_street;
	}
	public void setKod_street(BigDecimal kod_street) {
		this.kod_street = kod_street;
	}
	public String getName_street() {
		return name_street;
	}
	public void setName_street(String name_street) {
		this.name_street = name_street;
	}
	public BigDecimal getKod_gorod() {
		return kod_gorod;
	}
	public void setKod_gorod(BigDecimal kod_gorod) {
		this.kod_gorod = kod_gorod;
	}
	public Gorod getGorod() {
		return gorod;
	}
	public void setGorod(Gorod gorod) {
		this.gorod = gorod;
	}
	@Override
	public String toString() {
		return  getGorod().toString() + ", " +getName_street(); 
	} 
}
