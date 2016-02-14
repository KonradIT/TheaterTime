package com.chernowii.theatertime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by Konrad Iturbe on 02/14/16.
 */
public class WidgetService extends AppWidgetProvider {
    public String ON = "com.chernowii.theatertime.THEATER_ON";
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
        for(int i=0; i<appWidgetIds.length; i++){
            int currentWidgetId = appWidgetIds[i];


            Intent intentOn = new Intent();
           intentOn.setAction(ON);


            Intent intentOff = new Intent();
            intentOff.setClassName(off.class.getPackage().getName(), off.class.getName());

            PendingIntent pendingOn = PendingIntent.getActivity(context, 0,intentOn, 0);
            PendingIntent pendingOff = PendingIntent.getActivity(context, 0,intentOff, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_ui);

            views.setOnClickPendingIntent(R.id.wtheateron, pendingOn);
            views.setOnClickPendingIntent(R.id.wtheateroff, pendingOff);
            appWidgetManager.updateAppWidget(currentWidgetId,views);
            Toast.makeText(context, "widget added", Toast.LENGTH_SHORT).show();
        }
    }
}