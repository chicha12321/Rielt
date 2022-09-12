package com.data;

import java.math.BigDecimal;

public class Gorod {
    private BigDecimal kod_gorod = null; // Код
    private String name = null; // Наим-е
    private BigDecimal popul = null; // Престижность

    public BigDecimal getKod_gorod() {
        return kod_gorod;
    }

    public void setKod_gorod(BigDecimal kod_gorod) {
        this.kod_gorod = kod_gorod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPopul() {
        return popul;
    }

    public void setPopul(BigDecimal popul) {
        this.popul = popul;
    }

    @Override
    public String toString() {
        return getName();
    }

}
