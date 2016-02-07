package com.chernowii.theatertime;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by Konrad Iturbe on 02/06/16.
 */
public class TheaterOn extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent scheduledIntent = new Intent(context, SendMessageToWatch.class);
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(scheduledIntent);
        Toast.makeText(context, "Theater ON (Started)", Toast.LENGTH_LONG).show();
    }


}

