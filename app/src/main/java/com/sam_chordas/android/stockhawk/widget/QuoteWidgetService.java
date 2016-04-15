package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Tarun on 15/04/2016.
 */
public class QuoteWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new QuoteWidgetDataProvider(this, intent);
    }
}
