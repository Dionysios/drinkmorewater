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
    private static final int REMINDER_INTERVAL_MINUTES = 5;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS ;
    public static final String FIREBASE_REMINDER_TAG = "my-unique-tag";

    private static FirebaseJobDispatcher firebaseJobDispatcher;

    synchronized public static void initiaze(@NonNull final Context context, boolean active) {
        if (!active) return;
        Driver driver = new GooglePlayDriver(context);
        firebaseJobDispatcher = new FirebaseJobDispatcher(driver);
        Log.i("TAG", "onStartJob initialize with" + active);
        Job constraintReminderJob = firebaseJobDispatcher.newJobBuilder()
                .setService(ReminderService.class)
                .setTag(FIREBASE_REMINDER_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(0, 900 ))
                .setReplaceCurrent(true)
                .build();
        firebaseJobDispatcher.schedule(constraintReminderJob);
    }

    public static void cancelAllReminders(){
        Log.i("TAG", "onStartJob cancel");
        firebaseJobDispatcher.cancel(FIREBASE_REMINDER_TAG);
    }
}
