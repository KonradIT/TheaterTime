package com.chernowii.theatertime;

import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Konrad Iturbe on 02/04/16.
 */
public class ListenerForPhone extends WearableListenerService {
    private static final String on = "/on";
    private static final String off = "/off";



    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(on)) {
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

        if (messageEvent.getPath().equals(off)) {
            try {
                Process su = Runtime.getRuntime().exec("su");
                DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
                outputStream.writeBytes("input tap 100 100;");
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
                outputStream.writeBytes("input tap 100 100;");
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
                outputStream.writeBytes("input tap 100 100;");
                outputStream.flush();
                outputStream.writeBytes("exit\n");
                outputStream.flush();
                su.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
