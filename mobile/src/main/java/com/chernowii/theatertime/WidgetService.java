package com.chernowii.theatertime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by Konrad Iturbe on 02/14/16.
 */
public class WidgetService extends AppWidgetProvider {
    public String THEATER_ON = "THEATER_ON";
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
        for(int i=0; i<appWidgetIds.length; i++){
            int currentWidgetId = appWidgetIds[i];


            Intent intent = new Intent(context, on.class);

            PendingIntent pending = PendingIntent.getActivity(context, 0,intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_ui);

            views.setOnClickPendingIntent(R.id.wtheateron, pending);
            appWidgetManager.updateAppWidget(currentWidgetId,views);
            Toast.makeText(context, "widget added", Toast.LENGTH_SHORT).show();
        }
    }
}