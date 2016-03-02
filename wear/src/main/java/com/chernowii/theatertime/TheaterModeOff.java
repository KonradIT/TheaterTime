package com.chernowii.theatertime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;

public class TheaterModeOff extends Activity {

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
        }
        finish();
    }
}
