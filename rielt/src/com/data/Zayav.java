package com.data;

import java.math.BigDecimal;
import java.util.Date;

public class Zayav {
		private BigDecimal id_zayav = null;
		private BigDecimal num_zayav = null;
	 private Date date_zayav = null;
	 private String vid = null;
	 private BigDecimal kod_klient = null;
	 private BigDecimal kod_sotrud = null;
	 private String prefer = null;
	 private Klient klient = null;
	 private Sotrud sotrud = null;
	 
	 public BigDecimal getKod_sotrud() {
		return kod_sotrud;
	}
	public void setKod_sotrud(BigDecimal kod_sotrud) {
		this.kod_sotrud = kod_sotrud;
	}
	public Sotrud getSotrud() {
		return sotrud;
	}
	public void setSotrud(Sotrud sotrud) {
		this.sotrud = sotrud;
	}
	
	 
	public BigDecimal getId_zayav() {
		return id_zayav;
	}
	public void setId_zayav(BigDecimal id_zayav) {
		this.id_zayav = id_zayav;
	}
	public BigDecimal getNum_zayav() {
		return num_zayav;
	}
	public void setNum_zayav(BigDecimal num_zayav) {
		this.num_zayav = num_zayav;
	}
	public Date getDate_zayav() {
		return date_zayav;
	}
	public void setDate_zayav(Date date_zayav) {
		this.date_zayav = date_zayav;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public BigDecimal getKod_klient() {
		return kod_klient;
	}
	public void setKod_klient(BigDecimal kod_klient) {
		this.kod_klient = kod_klient;
	}
	public String getPrefer() {
		return prefer;
	}
	public void setPrefer(String prefer) {
		this.prefer = prefer;
	}
	public Klient getKlient() {
		return klient;
	}
	public void setKlient(Klient klient) {
		this.klient = klient;
	}
	 
}
