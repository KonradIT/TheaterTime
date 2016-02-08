package com.chernowii.theatertime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Calendar;

public class PhoneConfig extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private static final String on = "/on";
    private static final String off = "/off";
    public static final String CURRENT_START_ALARM = "PrefsFile";
    public String START_TIME = "00:00";
    public String STOP_TIME = "00:00";
    public int START_TIME_HH = 00;
    public int START_TIME_MM = 00;
    public int STOP_TIME_HH = 00;
    public int STOP_TIME_MM = 00;
    Node wearNode;
    GoogleApiClient wearGoogleApiClient;
    private boolean mResolvingError=false;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    public static final String PREFS_NAME = "Preferences";
   // SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    public static final String start = "start_string";
    public static final String stop = "stop_string";
    public int WELCOME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_config);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show start activity

            startActivity(new Intent(PhoneConfig.this, Welcome.class));

        }


        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();
        wearGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        String startTime;
        String stopTime;
        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        startTime = settings.getString(start, null); //2
        stopTime = settings.getString(stop, null); //2
        TextView currentTimer = (TextView)findViewById(R.id.currentTimer);
        currentTimer.setText(startTime + " - " + stopTime);
        if (WELCOME == 0){

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_phone_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendOn(View v) {
        if (wearNode != null && wearGoogleApiClient!=null && wearGoogleApiClient.isConnected()) {
            //
            Wearable.MessageApi.sendMessage(
                    wearGoogleApiClient, wearNode.getId(), on, null).setResultCallback(

                    new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {

                            if (!sendMessageResult.getStatus().isSuccess()) {
                                Log.e("TAG", "Failed to send message with status code: "
                                        + sendMessageResult.getStatus().getStatusCode());
                            }
                        }
                    }
            );
        }else{
            Toast.makeText(getApplicationContext(),
                    "No connection to phone", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),
                    "Connect watch to phone!", Toast.LENGTH_SHORT).show();

        }

    }
    public void sendOff(View v) {
        if (wearNode != null && wearGoogleApiClient!=null && wearGoogleApiClient.isConnected()) {
            //
            Wearable.MessageApi.sendMessage(
                    wearGoogleApiClient, wearNode.getId(), off, null).setResultCallback(

                    new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {

                            if (!sendMessageResult.getStatus().isSuccess()) {
                                Log.e("TAG", "Failed to send message with status code: "
                                        + sendMessageResult.getStatus().getStatusCode());
                            }
                        }
                    }
            );
        }else{
            Toast.makeText(getApplicationContext(),
                    "No connection to phone", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),
                    "Connect watch to phone!", Toast.LENGTH_SHORT).show();

        }

    }

    //time triggers


    public void setStartingTime(View v){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(PhoneConfig.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                START_TIME = selectedHour + ":" + selectedMinute;
                START_TIME_HH = selectedHour;
                START_TIME_MM = selectedMinute;
                TextView startTime = (TextView)findViewById(R.id.startTime);
                startTime.setText(START_TIME);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Stopping Time");
        mTimePicker.show();
    }

    public void setEndingTime(View v){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(PhoneConfig.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                STOP_TIME = selectedHour + ":" + selectedMinute;
                STOP_TIME_HH = selectedHour;
                STOP_TIME_MM = selectedMinute;
                TextView stopTime = (TextView)findViewById(R.id.stopTime);
                stopTime.setText(STOP_TIME);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Stopping Time");
        mTimePicker.show();
    }

    public void setStartTimeConfig(View v){
        Calendar calendaram = Calendar.getInstance();
        calendaram.set(Calendar.HOUR_OF_DAY, START_TIME_HH);
        calendaram.set(Calendar.MINUTE, START_TIME_MM);
        calendaram.set(Calendar.SECOND, 0);
        calendaram.set(Calendar.AM_PM, Calendar.PM);
        Intent myIntent = new Intent(PhoneConfig.this, TheaterOn.class);
        PendingIntent pendingIntentam = PendingIntent.getBroadcast(PhoneConfig.this, 0, myIntent, 0);
        AlarmManager alarmManageram = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManageram.set(AlarmManager.RTC, calendaram.getTimeInMillis(), pendingIntentam);
        Toast.makeText(getApplicationContext(),"Time configuration saved!",Toast.LENGTH_LONG).show();
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(start, START_TIME);
        editor.putString(stop, STOP_TIME);//3
        editor.commit(); //4

        String startTime;
        String stopTime;
        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        startTime = settings.getString(start, null); //2
        stopTime = settings.getString(stop, null); //2
        TextView currentTimer = (TextView)findViewById(R.id.currentTimer);
        currentTimer.setText(startTime + " - " + stopTime);
    }
    //config

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {
            wearGoogleApiClient.connect();
        }
    }

    /*
    * Resolve the node = the connected device to send the message to
    */
    private void resolveNode() {

        Wearable.NodeApi.getConnectedNodes(wearGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult nodes) {
                for (Node node : nodes.getNodes()) {
                    wearNode = node;
                }
            }
        });
    }


    @Override
    public void onConnected(Bundle bundle) {
        resolveNode();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(), "Error, not connected to phone!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),                     "Error, not connected to phone!", Toast.LENGTH_SHORT).show();
    }

}

