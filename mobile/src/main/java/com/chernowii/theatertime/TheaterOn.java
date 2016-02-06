package com.chernowii.theatertime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Konrad Iturbe on 02/06/16.
 */
public class TheaterOn extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Theater ON", Toast.LENGTH_LONG).show();
    }


}


