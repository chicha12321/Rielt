package com.data;

import java.math.BigDecimal;
import java.util.Date;

public class Book_oplat {
    private BigDecimal id_book_oplat = null;
    private BigDecimal id_book_uchyot = null;
    private Date date_opl = null;
    private BigDecimal sum_opl = null;

    public BigDecimal getId_book_oplat() {
        return id_book_oplat;
    }

    public void setId_book_oplat(BigDecimal id_book_oplat) {
        this.id_book_oplat = id_book_oplat;
    }

    public BigDecimal getId_book_uchyot() {
        return id_book_uchyot;
    }

    public void setId_book_uchyot(BigDecimal id_book_uchyot) {
        this.id_book_uchyot = id_book_uchyot;
    }

    public Date getDate_opl() {
        return date_opl;
    }

    public void setDate_opl(Date date_opl) {
        this.date_opl = date_opl;
    }

    public BigDecimal getSum_opl() {
        return sum_opl;
    }

    public void setSum_opl(BigDecimal sum_opl) {
        this.sum_opl = sum_opl;
    }

}
