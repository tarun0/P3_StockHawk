package com.sam_chordas.android.stockhawk.service;

import model.RetrievedResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Tarun on 10/04/2016.
 */
public interface RetrieveHistoryService {
    @GET("/yql")
    void getHistory(@Query("q") String query, Callback<RetrievedResponse> callback );
}
