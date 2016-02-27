package com.chernowii.theatertime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Konrad Iturbe on 02/04/16.
 */
public class ListenerForPhone extends WearableListenerService {
    private static final String on = "/on";
    private static final String off = "/off";
    private static final String START_TIME_HH = "/start_time_hh";
    private static final String START_TIME_MM = "/start_time_mm";
    private static final String STOP_TIME_HH = "/stop_time_hh";
    private static final String STOP_TIME_MM = "/stop_time_mm";
    private static final String setAlarm = "/setalarm";
    private static final String wifiOn = "/wifion";
    private static final String wifiOff = "/wifiOff";

    public int SettingStartHH = 0;
    public int SettingStartMM = 0;
    public int SettingStopHH = 0;
    public int SettingStopMM = 0;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(on)) {
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
            Toast.makeText(getApplicationContext(),"ON",Toast.LENGTH_SHORT).show();
        }

        if (messageEvent.getPath().equals(off)) {
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
            Toast.makeText(getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();

        }
        if (messageEvent.getPath().equals(START_TIME_HH)){
            SettingStartHH = Integer.parseInt(new String(messageEvent.getData()));
            //Toast.makeText(getApplicationContext(), "Start HH" + SettingStartHH, Toast.LENGTH_LONG).show();
        }
        if (messageEvent.getPath().equals(START_TIME_MM)){
            SettingStartMM = Integer.parseInt(new String(messageEvent.getData()));
            //Toast.makeText(getApplicationContext(), "WORKS", Toast.LENGTH_LONG).show();
        }
        if (messageEvent.getPath().equals(STOP_TIME_HH)){
            SettingStopHH = Integer.parseInt(new String(messageEvent.getData()));
            //Toast.makeText(getApplicationContext(), "WORKS", Toast.LENGTH_LONG).show();
        }
        if (messageEvent.getPath().equals(STOP_TIME_MM)){
            SettingStopMM = Integer.parseInt(new String(messageEvent.getData()));
        }
        if (messageEvent.getPath().equals(setAlarm)){
            Calendar calendaram = Calendar.getInstance();
            calendaram.set(Calendar.HOUR_OF_DAY, SettingStartHH);
            calendaram.set(Calendar.MINUTE, SettingStartMM);
            calendaram.set(Calendar.SECOND, 0);
            calendaram.set(Calendar.AM_PM, Calendar.PM);
            Intent myIntent = new Intent(this, TheaterModeOn.class);
            PendingIntent pendingIntentam = PendingIntent.getActivity(ListenerForPhone.this, 0, myIntent, 0);
            AlarmManager alarmManageram = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManageram.set(AlarmManager.RTC, calendaram.getTimeInMillis(), pendingIntentam);

            Calendar calendarTwo = Calendar.getInstance();
            calendarTwo.set(Calendar.HOUR_OF_DAY, SettingStopHH);
            calendarTwo.set(Calendar.MINUTE, SettingStopMM);
            calendarTwo.set(Calendar.SECOND, 0);
            calendarTwo.set(Calendar.AM_PM, Calendar.PM);
            Intent StopTheater = new Intent(this, TheaterModeOff.class);
            PendingIntent PendingIntentTwo = PendingIntent.getActivity(ListenerForPhone.this, 0, StopTheater, 0);
            AlarmManager alarmManagerTwo = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManagerTwo.set(AlarmManager.RTC, calendarTwo.getTimeInMillis(), PendingIntentTwo);

        }
        if (messageEvent.getPath().equals(wifiOn)){
            WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            wifi.setWifiEnabled(true);
        }
        if (messageEvent.getPath().equals(wifiOff)){
            WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            wifi.setWifiEnabled(false);
        }


    }

}
