package com.data;

import java.math.BigDecimal;
import java.util.Date;

public class Book_uchyot {

    private BigDecimal id_book_uchyot = null;
    private BigDecimal id_zayav = null;
    private BigDecimal kod_obj = null;
    private BigDecimal kod_usl = null;
    private Date uch_date = null;
    private BigDecimal kod_sotrud = null;
    private BigDecimal stoim = null;
    private BigDecimal opl_stoim = null;
    private Usluga usluga = null;
    private Sotrud sotrud = null;
    private Obj_nedvij obj_nedvij = null;

    public BigDecimal getId_book_uchyot() {
        return id_book_uchyot;
    }

    public void setId_book_uchyot(BigDecimal id_book_uchyot) {
        this.id_book_uchyot = id_book_uchyot;
    }

    public BigDecimal getId_zayav() {
        return id_zayav;
    }

    public void setId_zayav(BigDecimal id_zayav) {
        this.id_zayav = id_zayav;
    }

    public BigDecimal getKod_obj() {
        return kod_obj;
    }

    public void setKod_obj(BigDecimal kod_obj) {
        this.kod_obj = kod_obj;
    }

    public BigDecimal getKod_usl() {
        return kod_usl;
    }

    public void setKod_usl(BigDecimal kod_usl) {
        this.kod_usl = kod_usl;
    }

    public Date getUch_date() {
        return uch_date;
    }

    public void setUch_date(Date uch_date) {
        this.uch_date = uch_date;
    }

    public BigDecimal getKod_sotrud() {
        return kod_sotrud;
    }

    public void setKod_sotrud(BigDecimal kod_sotrud) {
        this.kod_sotrud = kod_sotrud;
    }

    public BigDecimal getStoim() {
        return stoim;
    }

    public void setStoim(BigDecimal stoim) {
        this.stoim = stoim;
    }

    public BigDecimal getOpl_stoim() {
        return opl_stoim;
    }

    public void setOpl_stoim(BigDecimal opl_stoim) {
        this.opl_stoim = opl_stoim;
    }

    public Usluga getUsluga() {
        return usluga;
    }

    public void setUsluga(Usluga usluga) {
        this.usluga = usluga;
    }

    public Sotrud getSotrud() {
        return sotrud;
    }

    public void setSotrud(Sotrud sotrud) {
        this.sotrud = sotrud;
    }

    public Obj_nedvij getObj_nedvij() {
        return obj_nedvij;
    }

    public void setObj_nedvij(Obj_nedvij obj_nedvij) {
        this.obj_nedvij = obj_nedvij;
    }


}
