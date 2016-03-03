package com.chernowii.theatertime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private static final String charge_on = "/charge_on";
    private static final String charge_off = "/charge_off";
    public static final String CURRENT_START_ALARM = "PrefsFile";
    public String START_TIME = "00:00";
    public String STOP_TIME = "00:00";

    public String alarm = "";
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
    private static final String wifiOn = "/wifion";
    private static final String wifiOff = "/wifiOff";
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
        CheckBox reactToCharge = (CheckBox)findViewById(R.id.chargingEvent);
        reactToCharge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                     @Override
                                                     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                         if (buttonView.isChecked()) {
                                                            Toast.makeText(getApplicationContext(),"React to charge",Toast.LENGTH_SHORT).show();
                                                            sendWatch(charge_on,charge_on);
                                                         } else {
                                                             Toast.makeText(getApplicationContext(),"Not reacting to charge",Toast.LENGTH_SHORT).show();
                                                             sendWatch(charge_off, charge_off);

                                                         }
                                                }
                                            }
        );

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
        switch (item.getItemId()) {
            case R.id.action_about:
                new AlertDialog.Builder(this)
                        .setTitle("About Theater Time")
                        .setMessage("Developed by Konrad Iturbe\n/u/konrad-iturbe | @KonradIT in XDA and GitHub\nHope you like the app!\nVersion: 1.0")
                        .setPositiveButton(R.string.issues, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.github.com/KonradIT/theatertime/issues"));
                                startActivity(githubIntent);
                            }
                        })
                        .setNeutralButton(R.string.webpage, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://chernowii.com"));
                                startActivity(webIntent);
                            }
                        })

                        .setNegativeButton(R.string.mail, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent mailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto", "mail@chernowii.com", null));
                                startActivity(mailIntent);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();

                return true;
            case R.id.action_root:
                new AlertDialog.Builder(this)
                        .setTitle("Root Your Android Wear watch")
                        .setMessage("Android Wear is no different from Android when it comes to hacking and rooting, you will need to first free your Wear watch (unlock it), flash a custom recovery such as TWRP, and flash the wear SuperSU zip file. \nChoose LP (Lollipop, 5.X) or MM (Marshmallow, 6.X) tutorials, you will need a computer and the ADB tools.\n Remember, Google Search is your friend!")

                        .setNeutralButton("Tutorial for LP", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.julianevansblog.com/2015/02/how-to-root-your-android-wear-to-modify-your-device.html"));
                                startActivity(webIntent);
                            }
                        })

                        .setNegativeButton("Tutorial for MM", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://forum.xda-developers.com/apps/supersu/android-wear-6-0-1-root-squashfs-t3319097/post65428079#post65428079"));
                                startActivity(webIntent);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();

                return true;
            case R.id.action_more_apps:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Konrad Iturbe- Chernowii")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:Konrad Iturbe - Chernowii")));
                }

                return true;
            case R.id.action_donate:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.chernowii.donate")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.chernowii.donate")));
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
public void copyIntentOn(View v){
    ClipboardManager clipboard = (ClipboardManager)
            getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = ClipData.newPlainText("com.chernowii.theatertime.THEATER_ON", "com.chernowii.theatertime.THEATER_ON");
    clipboard.setPrimaryClip(clip);
}

    public void copyIntentOff(View v){
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("com.chernowii.theatertime.THEATER_OFF", "com.chernowii.theatertime.THEATER_OFF");
        clipboard.setPrimaryClip(clip);
    }
    public void sendOn(View v) {
        boolean isChecked = ((CheckBox) findViewById(R.id.wifiBox)).isChecked();
        if (isChecked){
            sendWiFi("wifiOff");
        }
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
    public void sendWatch(String cause, String status){
        if (wearNode != null && wearGoogleApiClient!=null && wearGoogleApiClient.isConnected()) {
            //
            Wearable.MessageApi.sendMessage(
                    wearGoogleApiClient, wearNode.getId(), cause, status.getBytes()).setResultCallback(

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
        boolean isChecked = ((CheckBox) findViewById(R.id.wifiBox)).isChecked();
        if (isChecked){
            sendWiFi("wifiOn");
        }
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

    public void sendWiFi(String state) {
        if (wearNode != null && wearGoogleApiClient!=null && wearGoogleApiClient.isConnected()) {
            //
            Wearable.MessageApi.sendMessage(
                    wearGoogleApiClient, wearNode.getId(), state, null).setResultCallback(

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
        sendWatch("/start_time_hh", String.valueOf(START_TIME_HH));
        sendWatch("/start_time_mm", String.valueOf(START_TIME_MM));
        sendWatch("/stop_time_hh", String.valueOf(STOP_TIME_HH));
        sendWatch("/stop_time_mm", String.valueOf(STOP_TIME_MM));
        sendWatch("/setalarm", "nothing");
        Toast.makeText(getApplicationContext(),"Time configuration saved!",Toast.LENGTH_LONG).show();
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2

        editor.putString(alarm, on);
        editor.commit();
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

