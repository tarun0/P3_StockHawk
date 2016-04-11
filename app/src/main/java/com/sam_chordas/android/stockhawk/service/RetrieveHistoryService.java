package com.sam_chordas.android.stockhawk.service;

import model.RequestModel;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Tarun on 10/04/2016.
 */
public interface RetrieveHistoryService {
    @GET("/yql?&format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")
    void getHistory(@retrofit.http.Query("q") String query, Callback<RequestModel> callback );
}
