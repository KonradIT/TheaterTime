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
    public static final String BROADCAST_ON = "com.chernowii.theatertime.THEATER_ON";
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
        for(int i=0; i<appWidgetIds.length; i++){
            int currentWidgetId = appWidgetIds[i];


            Intent intent = new Intent(BROADCAST_ON);
            Bundle extras = new Bundle();

            intent.putExtras(extras);
            context.sendBroadcast(intent);

            PendingIntent pending = PendingIntent.getActivity(context, 0,intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_ui);

            views.setOnClickPendingIntent(R.id.wtheateron, pending);
            appWidgetManager.updateAppWidget(currentWidgetId,views);
            Toast.makeText(context, "widget added", Toast.LENGTH_SHORT).show();
        }
    }
}