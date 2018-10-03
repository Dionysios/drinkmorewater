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

class FireBaseJob {
    public static final String FIREBASE_REMINDER_TAG = "my-unique-tag";
    private static boolean sInitialized = false;

    synchronized public static void initiaze(@NonNull final Context context) {
        if (sInitialized) return;
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);
        Log.i("TAG", "onStartJob initialize");
        Job constraintReminderJob = firebaseJobDispatcher.newJobBuilder()
                .setService(ReminderService.class)
                .setTag(FIREBASE_REMINDER_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(0, 60))
                .setReplaceCurrent(true)
                .build();

        firebaseJobDispatcher.schedule(constraintReminderJob);
        sInitialized = true;
    }
}
