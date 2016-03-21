package com.chernowii.theatertime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.twofortyfouram.locale.sdk.client.receiver.AbstractPluginSettingReceiver;

/**
 * Created by Konrad Iturbe on 03/03/16.
 */
public final class TaskerReceiver extends AbstractPluginSettingReceiver {

    @Override
    protected boolean isBundleValid(@NonNull final Bundle bundle) {
        return PluginBundleValues.isBundleValid(bundle);
    }

    @Override
    protected boolean isAsync() {
        return false;
    }

    @Override
    protected void firePluginSetting(@NonNull final Context context, @NonNull final Bundle bundle) {
        if(PluginBundleValues.getMessage(bundle).equals("Theater Time: ON")){
            Toast.makeText(context,"Theater ON",Toast.LENGTH_SHORT).show();
            Intent broadcast = new Intent();
            broadcast.setAction("com.chernowii.theatertime.THEATER_ON");
            context.sendBroadcast(broadcast);
        }
        if(PluginBundleValues.getMessage(bundle).equals("Theater Time: OFF")){
            Toast.makeText(context,"Theater OFF",Toast.LENGTH_SHORT).show();
            Intent broadcast = new Intent();
            broadcast.setAction("com.chernowii.theatertime.THEATER_OFF");
            context.sendBroadcast(broadcast);
        }
    }
}
