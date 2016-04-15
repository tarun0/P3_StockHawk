package com.sam_chordas.android.stockhawk.model;

/**
 * Created by Tarun on 10/04/2016.
 */
public class Quote {
    private String Symbol;
    private double High;
    private double Low;
    private float Close;
    private long Volume;
    private String Date;

    public String getSymbol() {
        return Symbol;
    }

    public float getClose() {
        return Close;
    }

    public String getDate() {
        return Date;
    }
}
