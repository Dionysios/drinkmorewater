package com.dionpapas.drinkyourwater;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dionpapas.drinkyourwater.fragments.MainFragment;
import com.dionpapas.drinkyourwater.utilities.Utilities;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class FireBaseJob {
   // private static final int REMINDER_INTERVAL_MINUTES = 1;
    //private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = 150 ;
    public static final String FIREBASE_REMINDER_TAG = "my-unique-tag";


    synchronized public static void initiaze(@NonNull final Context context, boolean active, boolean connectedWifi, boolean isCharging, String REMINDER_INTERVAL , String todaysdate) {
        int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(Integer.parseInt(REMINDER_INTERVAL)));
        Log.i("TAG", "onStartJob the time incoming" + REMINDER_INTERVAL_SECONDS );
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);
        if (!active) {
            cancelAllReminders(context);
        } else {
            if (checkIfDateHasChanged(todaysdate)){
                Utilities.saveWaterEntry(context);
                Utilities.setWaterCount(context, 0);
            }
            Log.i("TAG", "onStartJob initialize with" + active);
            Log.i("TAG", "onStartJob getting values here" + connectedWifi + "I am charging" + isCharging);
            Job constraintReminderJob = firebaseJobDispatcher.newJobBuilder()
                    .setService(ReminderService.class)
                    .setTag(FIREBASE_REMINDER_TAG)
                    .setConstraints(connectedWifi ? Constraint.ON_UNMETERED_NETWORK : Constraint.ON_ANY_NETWORK,
                            isCharging ? Constraint.DEVICE_CHARGING : 0)
                    .setLifetime(Lifetime.FOREVER)
                    .setRecurring(true)
                    .setTrigger(Trigger.executionWindow(REMINDER_INTERVAL_SECONDS,REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                    .setReplaceCurrent(true)
                    .build();
            firebaseJobDispatcher.schedule(constraintReminderJob);
        }
    }

    public static void cancelAllReminders(Context context){
        Log.i("TAG", "onStartJob cancel");
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);
        firebaseJobDispatcher.cancel(FIREBASE_REMINDER_TAG);
    }

    public static boolean checkIfDateHasChanged(String todaysDate){
        return (Utilities.getTodaysDate().equals(todaysDate)) ? true : false;
    }
}
