package com.dionpapas.drinkyourwater;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dionpapas.drinkyourwater.utilities.Utilities;

import java.util.Calendar;

import static com.dionpapas.drinkyourwater.MainActivity.ALARM_REQUEST_CODE;

public class RestartCounterService extends Service {



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //PendingIntent pendingIntent = null;
        /* Retrieve a PendingIntent that will perform a broadcast */
        //Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        //pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, alarmIntent, 0);
        Utilities.saveWaterEntry (this);
        Utilities.setWaterCount(this,0 );
        Log.i("Here is Alarm", "here is alarm1");
//        Calendar cal = Calendar.getInstance();
//        // add alarmTriggerTime seconds to the calendar object
//        //cal.add(Calendar.SECOND, alarmTriggerTime);
//        cal.set(Calendar.HOUR_OF_DAY, 21);
//        cal.set(Calendar.MINUTE, 00);
//        cal.set(Calendar.SECOND, 00);
//
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
//        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),  AlarmManager.INTERVAL_DAY, pendingIntent);//set alarm manager with entered timer by converting into milliseconds
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
