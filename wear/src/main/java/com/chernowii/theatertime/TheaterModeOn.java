package com.chernowii.theatertime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;

public class TheaterModeOn extends Activity {

    private TextView mTextView;
    public boolean isWatchRooted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(RootUtils.isDeviceRooted()){
            isWatchRooted=true;
        }
        else{
            Intent noroot = new Intent(this, NoRootAvailable.class);
            this.startActivity(noroot);
        }
        if(isWatchRooted) {
            try {
                Process su = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                outputStream.writeBytes("input keyevent 26\n");
                outputStream.flush();
                outputStream.writeBytes("exit\n");
                outputStream.flush();
                su.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Process su = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                String sqliteON = "sqlite3 /data/data/com.android.providers.settings/databases/settings.db \"update global set value='1' where name='theater_mode_on';\" ";
                outputStream.writeBytes("settings put global theater_mode_on 1\n" + sqliteON + "\n");
                outputStream.flush();

                outputStream.writeBytes("exit\n");
                outputStream.flush();
                su.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        finish();
    }
}
