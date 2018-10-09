package com.dionpapas.drinkyourwater;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

class FireBaseJob {
    private static final int REMINDER_INTERVAL_MINUTES = 1;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS ;
    public static final String FIREBASE_REMINDER_TAG = "my-unique-tag";

    synchronized public static void initiaze(@NonNull final Context context, boolean active, boolean connectedWifi, boolean isCharging) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);
        if (!active) {
            firebaseJobDispatcher.cancel(FIREBASE_REMINDER_TAG);
        } else {
            Log.i("TAG", "onStartJob initialize with" + active);
            Log.i("TAG", "onStartJob getting values here" + connectedWifi + "I am charging" + isCharging);
            Job constraintReminderJob = firebaseJobDispatcher.newJobBuilder()
                    .setService(ReminderService.class)
                    .setTag(FIREBASE_REMINDER_TAG)
                    .setConstraints(connectedWifi ? Constraint.ON_UNMETERED_NETWORK : Constraint.ON_ANY_NETWORK,
                            isCharging ? Constraint.DEVICE_CHARGING : 0)
                    .setLifetime(Lifetime.FOREVER)
                    .setRecurring(true)
                    .setTrigger(Trigger.executionWindow(0, REMINDER_INTERVAL_SECONDS))
                    .setReplaceCurrent(true)
                    .build();
            firebaseJobDispatcher.schedule(constraintReminderJob);
        }
    }

}
