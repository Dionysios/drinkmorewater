package com.dionpapas.drinkyourwater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dionpapas.drinkyourwater.utilities.Utilities;

public class DateChangedReceiver extends BroadcastReceiver {
    final static String SCREEN_TOGGLE_TAG = "SCREEN_TOGGLE_TAG";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(Intent.ACTION_DATE_CHANGED.equals(action)) {
            Utilities.saveWaterEntry(context);
            Utilities.setWaterCount(context, 0);
            Log.d(SCREEN_TOGGLE_TAG, "Screen is turn off.");
        }
    }
}
