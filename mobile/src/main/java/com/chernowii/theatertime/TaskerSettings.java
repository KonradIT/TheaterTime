package com.chernowii.theatertime;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.twofortyfouram.locale.sdk.client.ui.activity.AbstractAppCompatPluginActivity;
import com.twofortyfouram.locale.sdk.client.receiver.AbstractPluginSettingReceiver;
import com.twofortyfouram.locale.sdk.client.ui.activity.AbstractPluginActivity;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class TaskerSettings extends AbstractPluginActivity {
    public String message2 = "";
    public String status = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasker_settings);
    }


    @Override
    public boolean isBundleValid(Bundle bundle) {
        return PluginBundleValues.isBundleValid(bundle);
    }

    @Override
    public void onPostCreateWithPreviousResult(Bundle bundle, String s) {

    }


    @Override



 public Bundle getResultBundle() {
        Bundle result = null;
        if (((CheckBox) findViewById(R.id.taskerStatus)).isChecked()) {
            status = "Theater Mode: ON";
            result = PluginBundleValues.generateBundle(getApplicationContext(), status);
        }
        else{
            result = PluginBundleValues.generateBundle(getApplicationContext(), "Theater Time: OFF");

        }
        return result;
    }


    @NonNull
    @Override
    public String getResultBlurb(Bundle bundle) {
        final String message = PluginBundleValues.getMessage(bundle);

        final int maxBlurbLength = getResources().getInteger(
                R.integer.com_twofortyfouram_locale_sdk_client_maximum_blurb_length);

        if (message.length() > maxBlurbLength) {
            return message.substring(0, maxBlurbLength);
        }

        return message;
    }
}


