package com.chernowii.theatertime;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Konrad Iturbe on 02/13/16.
 */
public class off extends BroadcastReceiver {
    Context context;
    public static final String TAP_ACTION_PATH = "/off";
    private static final String TAG = "NotificationReceiver";
    GoogleApiClient mGoogleApiClient;
    public void onReceive(Context context, Intent paramIntent) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        sendMessage();
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {

                    }
                }).build();
        mGoogleApiClient.connect();
    }

    private void getNodes() {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(
                new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                        HashSet<String> results = new HashSet<String>();
                        for (Node node : getConnectedNodesResult.getNodes()) {
                            results.add(node.getId());
                            Log.d(TAG, node.getId().toString());
                        }
                        Log.d(TAG,results.toString());
                        sendMessageApi(results);
                    }
                }
        );
    }

    private void sendMessageApi(Collection<String> nodes) {
        for (String node : nodes) {
            Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, node, TAP_ACTION_PATH, null).setResultCallback(
                    new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            if (!sendMessageResult.getStatus().isSuccess()) {

                                Toast.makeText(context, "FAILED TO SEND THEATER COMMAND!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Theater ON!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
            );
        }
    }

    private void sendMessage() {
        getNodes();
    }

}
