package com.chernowii.theatertime;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Konrad Iturbe on 02/28/16.
 */
public class SendWear extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStart(intent, startId);
        startActivity(new Intent(this, on.class));
        Toast.makeText(getApplicationContext(),"TEST 1",Toast.LENGTH_SHORT).show();
        stopSelf(startId);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(),"TEST 2",Toast.LENGTH_SHORT).show();
        return null;
    }

}
