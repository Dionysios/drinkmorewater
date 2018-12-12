package com.dionpapas.drinkyourwater;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class DateChangedBackgroundService extends Service {
    private DateChangedReceiver dateChangedReceiver = null;
    private static final String WIFI_STATE_CHANGE_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Create an IntentFilter instance.
        IntentFilter intentFilter = new IntentFilter();

        // Add network connectivity change action.
        intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intentFilter.addAction(CONNECTIVITY_ACTION);
        intentFilter.addAction(WIFI_STATE_CHANGE_ACTION);

        // Set broadcast receiver priority.
        intentFilter.setPriority(100);
        // Create a network change broadcast receiver.
        dateChangedReceiver = new DateChangedReceiver();
        // Register the broadcast receiver with the intent filter object.
        registerReceiver(dateChangedReceiver, intentFilter);
        Log.d(DateChangedReceiver.BroacastFound, "Service onCreate: dateChangedReceiver is registered.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister dateChangedReceiver when destroy.
        if(dateChangedReceiver !=null) {
            unregisterReceiver(dateChangedReceiver);
            Log.d(DateChangedReceiver.BroacastFound, "Service onDestroy: dateChangedReceiver is unregistered.");
        }
    }
}

