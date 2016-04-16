package com.sam_chordas.android.stockhawk.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.model.Quote;
import com.sam_chordas.android.stockhawk.model.RequestModel;
import com.sam_chordas.android.stockhawk.service.RetrieveHistoryService;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailsChartActivity extends Activity {
    Intent intent;
    String query;
    List<Float> prices;
    List<String> dates;
    BarChart chart;
    BarDataSet barDataSet;
    BarData barData;
    ArrayList<BarEntry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_chart);
        chart = (BarChart) findViewById(R.id.barchart);
        intent = getIntent();
        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setTitle(intent.getStringExtra("symbol"));
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.progress_dialog_message));
        dialog.show();
        RestAdapter builder = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("https://query.yahooapis.com/v1/public")
                .build();

        query = "select * from yahoo.finance.historicaldata where symbol = \'" +
                intent.getStringExtra("symbol") +
                "\' and startDate = \'2009-09-11\' and endDate = \'2010-03-10\'";

        RetrieveHistoryService retrieveHistoryService = builder.create(RetrieveHistoryService.class);
        retrieveHistoryService.getHistory(query, new Callback<RequestModel>() {
            @Override
            public void success(RequestModel retrievedResponse, Response response) {
                dialog.dismiss();
                if (retrievedResponse.getQuery().getCount() > 0) {

                    Toast.makeText(getApplicationContext(), R.string.pinch_to_zoom, Toast.LENGTH_SHORT).show();
                    int total = retrievedResponse.getQuery().getCount();
                    prices = new ArrayList<>();
                    dates = new ArrayList<String>();

                    ArrayList<Quote> quoteList = retrievedResponse.getQuery().getResults().getQuote();
                    entries = new ArrayList<BarEntry>();

                    for (int i = 0; i < total; i++) {
                        prices.add(quoteList.get(i).getClose());
                        dates.add(quoteList.get(i).getDate());
                        entries.add(new BarEntry(prices.get(i), i));
                    }

                    barDataSet = new BarDataSet(entries, "Close Price");
                    barData = new BarData(dates, barDataSet);

                    chart.setData(barData);
                    chart.setDescription("Close price vs Date");
                    chart.animateY(1500);
                    chart.invalidate();
                }
                else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailsChartActivity.this);
                    alertDialog.setTitle(R.string.message).setMessage(R.string.no_history_data_available).setCancelable(false)
                           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {
                                   Intent intent = new Intent(getApplicationContext(), MyStocksActivity.class);
                                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                   startActivity(intent);
                               }
                           }).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(DetailsChartActivity.this);
                alert.setTitle(R.string.message).setMessage(R.string.retrieve_chart_network_issue);
                alert.setCancelable(false).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), MyStocksActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }).show();
            }
        });
    }
}
