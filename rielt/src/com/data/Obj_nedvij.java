package com.data;

import java.math.BigDecimal;

public class Obj_nedvij {
	private BigDecimal kod_obj = null; // Код
	 private BigDecimal kod_zdan = null; // 
	 private BigDecimal area = null; // 
	 private BigDecimal price = null; // 
	 private BigDecimal floor = null; 
	 private String vid_ned = null;
	 private Zdan zdan = null;
	public Zdan getZdan() {
		return zdan;
	}
	public void setZdan(Zdan zdan) {
		this.zdan = zdan;
	}
	public BigDecimal getKod_obj() {
		return kod_obj;
	}
	public void setKod_obj(BigDecimal kod_obj) {
		this.kod_obj = kod_obj;
	}
	public BigDecimal getKod_zdan() {
		return kod_zdan;
	}
	public void setKod_zdan(BigDecimal kod_zdan) {
		this.kod_zdan = kod_zdan;
	}
	public BigDecimal getArea() {
		return area;
	}
	public void setArea(BigDecimal area) {
		this.area = area;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getFloor() {
		return floor;
	}
	public void setFloor(BigDecimal floor) {
		this.floor = floor;
	}
	public String getVid_ned() {
		return vid_ned;
	}
	public void setVid_ned(String vid_ned) {
		this.vid_ned = vid_ned;
	}
	@Override
	public String toString() {
		return getZdan().toString()+", "+getVid_ned();
	} 
	 
	 
}
