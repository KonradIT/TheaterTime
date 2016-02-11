package com.chernowii.theatertime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by Konrad Iturbe on 02/07/16.
 */
public class SendMessageToWatch extends Thread {
    private static final String on = "/on";

    // Constructor to send a message to the data layer



    // Wearable.MessageApi.sendMessage(wearGoogleApiClient, wearNode.getId(), off, null).setResultCallback(

}