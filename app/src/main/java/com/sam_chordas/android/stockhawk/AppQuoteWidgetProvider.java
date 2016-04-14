package com.sam_chordas.android.stockhawk;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tarun on 13/04/2016.
 */
public class AppQuoteWidgetProvider implements RemoteViewsService.RemoteViewsFactory {

    Context context;
    Intent intent;
    List<String> list = new ArrayList<>();

    public AppQuoteWidgetProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    private void initData() {
        list.clear();

        for (int i = 0; i<10; i++) {
            list.add(""+i);
        }
    }
    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_item_quote);
        views.setTextViewText(R.id.change, list.get(i));
        return views;
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
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
