package com.chernowii.theatertime;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.widget.Toast;


/**
 * Created by Konrad Iturbe on 02/06/16.
 */
public class TheaterOn extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent();
        i.setClassName("com.chernowii.theatertime", "com.chernowii.theatertime.ActivityOn");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        Toast.makeText(context, "Theater ON (Started)", Toast.LENGTH_LONG).show();
    }


}

