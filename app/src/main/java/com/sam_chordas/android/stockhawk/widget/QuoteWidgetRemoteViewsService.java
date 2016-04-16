/*
package com.sam_chordas.android.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.ui.DetailsChartActivity;

*/
/**
 * Created by Tarun on 12/04/2016.
 *//*

public class QuoteWidgetRemoteViewsService extends RemoteViewsService {

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

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                QuoteWidgetProvider.class));

        Cursor data = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},
                null);
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_collection_item);

        if (data.moveToFirst()) {
            for (int appWidgetId : appWidgetIds) {
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


}
*/

package com.sam_chordas.android.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

public class QuoteWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewFactory(this.getApplicationContext(), intent);
    }
}

class ListViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor mCursor;
    private int mAppWidgetId;

    public ListViewFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void onCreate() {
        // Since we reload the cursor in onDataSetChanged() which gets called immediately after
        // onCreate(), we do nothing here.
    }

    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    public int getCount() {
        return mCursor.getCount();
    }

    public RemoteViews getViewAt(int position) {
        // Get the data for this position from the content provider
        String symbol = "";
        String bidPrice = "";
        String change = "";
        int isUp = 1;
        if (mCursor.moveToPosition(position)) {
            final int symbolColIndex = mCursor.getColumnIndex(QuoteColumns.SYMBOL);
            final int bidPriceColIndex = mCursor.getColumnIndex(QuoteColumns.BIDPRICE);
            final int changeColIndex = mCursor.getColumnIndex(QuoteColumns.PERCENT_CHANGE);
            final int isUpIndex = mCursor.getColumnIndex(QuoteColumns.ISUP);
            symbol = mCursor.getString(symbolColIndex);
            bidPrice = mCursor.getString(bidPriceColIndex);
            change = mCursor.getString(changeColIndex);
            isUp = mCursor.getInt(isUpIndex);
        }

        // Fill data in UI
        final int itemId = R.layout.widget_collection_item;
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), itemId);
        rv.setTextViewText(R.id.stock_symbol, symbol);
        rv.setTextViewText(R.id.bid_price, bidPrice);
        rv.setTextViewText(R.id.change, change);
        if (isUp == 1) {
            rv.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
        } else {
            rv.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
        }

        // Set the click intent
        final Intent fillInIntent = new Intent();
        final Bundle extras = new Bundle();
        extras.putString("symbol", symbol);
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
        return rv;
    }

    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
        // Refresh the cursor
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},
                null);
    }
}
