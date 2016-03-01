package com.chernowii.theatertime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Konrad Iturbe on 02/29/16.
 */
public class ChargeReceiver extends BroadcastReceiver {
    public static final String CHARGE_PREFS = "ChargePreferences";
    public static final String prefs_charge = "charge";
    public String chargingprefs = "";
    private static final String charge_on = "/charge_on";
    private static final String charge_off = "/charge_off";
    public void onReceive(Context context , Intent intent) {
        String charge_state = intent.getAction();
        SharedPreferences settings;
        settings = context.getApplicationContext().getSharedPreferences(CHARGE_PREFS, Context.MODE_PRIVATE); //1
        chargingprefs = settings.getString(prefs_charge, null); //2
        if(charge_state.equals(Intent.ACTION_POWER_CONNECTED)) {
            if (chargingprefs != null) {
                if(chargingprefs.equals(charge_on)) {
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
            }
        }
        else if(charge_state.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            if (chargingprefs != null) {
                if (chargingprefs.equals(charge_on)) {

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
            }
        }
    }
}
