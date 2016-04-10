package model;

/**
 * Created by Tarun on 10/04/2016.
 */
public class RetrievedQuoteHistory {
    private String Symbol;
    private String Date;
    private double High;
    private double Low;
    private double Close;
    private long Volume;

    public String getSymbol() {
        return Symbol;
    }

    public String getDate() {
        return this.Date;
    }
}
