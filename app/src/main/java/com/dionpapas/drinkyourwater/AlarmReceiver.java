package com.dionpapas.drinkyourwater;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String RESTART_COUNTER = "com.dionpapas.drinkyourwater.restart_counter";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Here is Alarm", "here is alarm");
       // Intent restartCounterIntent = new Intent(RESTART_COUNTER);
       // LocalBroadcastManager.getInstance(context).sendBroadcast(restartCounterIntent);
       Toast.makeText(context, "ALARM!! ALARM!!", Toast.LENGTH_SHORT).show();

        //Stop sound service to play sound for alarm
        //context.startService(new Intent(context, RestartCounterService.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, RestartCounterService.class));
        } else {
            context.startService(new Intent(context, RestartCounterService.class));
        }

//        //This will send a notification message and show notification in notification tray
//        ComponentName comp = new ComponentName(context.getPackageName(),
//                AlarmNotificationService.class.getName());
//        startWakefulService(context, (intent.setComponent(comp)));
    }


}
