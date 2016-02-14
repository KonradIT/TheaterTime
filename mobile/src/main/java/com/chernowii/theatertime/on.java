package com.chernowii.theatertime;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.widget.Toast;

/**
 * Created by Konrad Iturbe on 02/13/16.
 */
public class on extends BroadcastReceiver {

    @Override
       public void onReceive(Context _context, Intent _intent) {
        Toast.makeText(_context, "Theater ON", Toast.LENGTH_LONG).show();
         }
}
