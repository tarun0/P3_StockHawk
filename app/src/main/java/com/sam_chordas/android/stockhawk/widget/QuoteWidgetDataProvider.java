package com.sam_chordas.android.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by Tarun on 15/04/2016.
 */
public class QuoteWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    Intent mIntent;
    Context mContext;

    Cursor mCursor;
    public QuoteWidgetDataProvider(Context context, Intent intent) {
        this.mIntent = intent;
        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        mCursor =  mContext.getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},
                null);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_item_quote);

        if (mCursor.moveToPosition(i)) {
            int symbolIndex = mCursor.getColumnIndex(QuoteColumns.SYMBOL);
            int bidPriceIndex = mCursor.getColumnIndex(QuoteColumns.BIDPRICE);
            int changeIndex = mCursor.getColumnIndex(QuoteColumns.PERCENT_CHANGE);
            int isUpIndex = mCursor.getColumnIndex(QuoteColumns.ISUP);

            remoteViews.setTextViewText(R.id.stock_symbol, mCursor.getString(symbolIndex));
            remoteViews.setTextViewText(R.id.bid_price, mCursor.getString(bidPriceIndex));
            remoteViews.setTextViewText(R.id.change, mCursor.getString(changeIndex));

            if (mCursor.getString(isUpIndex).equals("1")) {
                remoteViews.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
            }
            else {
                remoteViews.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
            }

            Intent newIntent = new Intent();
            newIntent.putExtra("symbol", mCursor.getString(symbolIndex));
            remoteViews.setOnClickFillInIntent(R.id.widget_list_item, newIntent);
        }

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
