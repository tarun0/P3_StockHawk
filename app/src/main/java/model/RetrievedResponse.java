package model;

import java.util.ArrayList;

/**
 * Created by Tarun on 10/04/2016.
 */
public class RetrievedResponse {
    private int count;
    private ArrayList<RetrievedQuoteHistory> results;

    public int getCount() {
        return count;
    }
    public ArrayList<RetrievedQuoteHistory> getResults() {
        return results;
    }
}
