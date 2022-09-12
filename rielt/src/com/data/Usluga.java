package com.data;

import java.math.BigDecimal;

public class Usluga {
	 private BigDecimal kod_usl = null; // Код
	 private String name_usl = null; // Наименование
	public BigDecimal getKod_usl() {
		return kod_usl;
	}
	public void setKod_usl(BigDecimal kod_usl) {
		this.kod_usl = kod_usl;
	}
	public String getName_usl() {
		return name_usl;
	}
	public void setName_usl(String name_usl) {
		this.name_usl = name_usl;
	}
	@Override
	public String toString() {
		return name_usl;
	}
	 
	 
}
