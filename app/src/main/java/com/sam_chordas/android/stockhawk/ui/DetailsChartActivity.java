package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.service.RetrieveHistoryService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import model.RetrievedResponse;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailsChartActivity extends Activity {
    Intent intent;
    String query, query1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_chart);
        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        intent = getIntent();
        RestAdapter builder = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("https://query.yahooapis.com/v1/public")
                .build();

        try {
            query1 = URLEncoder.encode("select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22", "utf-8")
                    + intent.getStringExtra("symbol") +
                    URLEncoder.encode("%22%20and%20startDate%20%3D%20%222009-09-11%22%20and%20endDate%20%3D%20%222010-03-10%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=", "utf-8");

            query = URLEncoder.encode("select * from yahoo.finance.historicaldata where symbol = \"YHOO\" and startDate = \"2009-09-11\" and endDate = \"2010-03-10\"", "utf-8");
        }
        catch (UnsupportedEncodingException i) {
            i.printStackTrace();
        }

        Log.e("QUERY!!!",query);
        Log.e("QUERY1",query1);


        RetrieveHistoryService retrieveHistoryService = builder.create(RetrieveHistoryService.class);
        retrieveHistoryService.getHistory(query, new Callback<RetrievedResponse>() {
            @Override
            public void success(RetrievedResponse retrievedResponse, Response response) {
                Toast.makeText(DetailsChartActivity.this, retrievedResponse.getCount()+"!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Retrofit","Error");
            }
        });

    }

}
