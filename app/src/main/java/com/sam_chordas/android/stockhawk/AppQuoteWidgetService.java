package com.sam_chordas.android.stockhawk;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Tarun on 13/04/2016.
 */
public class AppQuoteWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AppQuoteWidgetProvider(this, intent);
    }
}
