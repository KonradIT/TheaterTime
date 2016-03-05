package com.chernowii.theatertime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by Konrad Iturbe on 02/14/16.
 */
public class WidgetService extends AppWidgetProvider {
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
        for(int i=0; i<appWidgetIds.length; i++){
            int currentWidgetId = appWidgetIds[i];
            Intent intent_on = new Intent();
            intent_on.setAction("com.chernowii.theatertime.THEATER_ON");

            Intent intent_off = new Intent();
            intent_off.setAction("com.chernowii.theatertime.THEATER_OFF");

            PendingIntent theater_on = PendingIntent.getActivity(context, 0, intent_on, 0);
            PendingIntent theater_off = PendingIntent.getActivity(context, 0, intent_off, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_ui);

            views.setOnClickPendingIntent(R.id.wtheateron, theater_on);
            views.setOnClickPendingIntent(R.id.wtheateroff, theater_off);

            appWidgetManager.updateAppWidget(currentWidgetId, views);
        }
    }
}