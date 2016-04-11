package com.sam_chordas.android.stockhawk.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.ui.DetailsChartActivity;

/**
 * Created by Tarun on 12/04/2016.
 */
public class QuoteWidgetRemoteViewsService extends IntentService {

    public QuoteWidgetRemoteViewsService() {
        super("QuoteWidgetViewsService");
    }

    private static final String[] STOCK_COLUMNS = {
            QuoteColumns._ID,
            QuoteColumns.SYMBOL,
            QuoteColumns.BIDPRICE,
            QuoteColumns.PERCENT_CHANGE,
            QuoteColumns.CHANGE,
            QuoteColumns.ISUP
    };

    static final int INDEX_QUOTE_ID = 0;
    static final int INDEX_QUOTE_SYMBOL = 1;
    static final int INDEX_QUOTE_BIDPRICE = 2;
    static final int INDEX_QUOTE_PERCENT_CHANGE = 3;
    static final int INDEX_QUOTE_CHANGE = 4;
    static final int INDEX_QUOTE_ISUP = 5;


    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                QuoteWidgetProvider.class));

        Cursor data = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},
                null);
        if (data == null) {
            return;
        }

        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_collection_item);
            views.setTextViewText(R.id.stock_symbol, data.getString(INDEX_QUOTE_SYMBOL));
            views.setTextViewText(R.id.bid_price, data.getString(INDEX_QUOTE_BIDPRICE));
            views.setTextViewText(R.id.change, data.getString(INDEX_QUOTE_PERCENT_CHANGE));

            Intent chartIntent = new Intent(this, DetailsChartActivity.class);
            intent.putExtra("symbol", data.getString(INDEX_QUOTE_SYMBOL));
            views.setOnClickFillInIntent(R.id.widget_list_item, chartIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
