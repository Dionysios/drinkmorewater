package com.dionpapas.drinkyourwater;

import android.annotation.SuppressLint;
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
    private static final int REMINDER_INTERVAL_MINUTES = 15;
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
                .setTrigger(Trigger.executionWindow(0, REMINDER_INTERVAL_SECONDS ))
                .setReplaceCurrent(true)
                .build();
        firebaseJobDispatcher.schedule(constraintReminderJob);
    }

    synchronized public static void initiazeCharging(@NonNull final Context context) {
        Driver driver = new GooglePlayDriver(context);
        firebaseJobDispatcher = new FirebaseJobDispatcher(driver);
        Log.i("TAG", "onStartJob initialize with changing option" );
        Job constraintReminderJob = firebaseJobDispatcher.newJobBuilder()
                .setService(ReminderService.class)
                .setTag(FIREBASE_REMINDER_TAG)
                .setConstraints(Constraint.DEVICE_CHARGING)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(0, REMINDER_INTERVAL_SECONDS ))
                .setReplaceCurrent(true)
                .build();
        firebaseJobDispatcher.schedule(constraintReminderJob);
    }

    public static void cancelAllReminders(){
        Log.i("TAG", "onStartJob cancel");
        firebaseJobDispatcher.cancel(FIREBASE_REMINDER_TAG);
    }

    public static void addConstrain(int constraint) {
        cancelAllReminders();
        Job constraintReminderJob = firebaseJobDispatcher.newJobBuilder()
                .setService(ReminderService.class)
                .setTag(FIREBASE_REMINDER_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK, constraint)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(0, REMINDER_INTERVAL_SECONDS ))
                .setReplaceCurrent(true)
                .build();
        firebaseJobDispatcher.schedule(constraintReminderJob);
       // firebaseJobDispatcher.newJobBuilder().addConstraint(constraint);
        Log.i("TAG", "onStartJob contraint added"+firebaseJobDispatcher.newJobBuilder().getConstraints().toString());
    }

    //todo this needs to rewriten
    public static void removeConstrain(int constraint) {
        int [] newContraints = null;
        int[] contraints = firebaseJobDispatcher.newJobBuilder().getConstraints();
        for(int i=0;i<contraints.length;i++) {
            if( contraints[i] == constraint){

            }else {
                contraints[i] = newContraints[i];
            }
        }
        cancelAllReminders();
        Job constraintReminderJob = firebaseJobDispatcher.newJobBuilder()
                .setService(ReminderService.class)
                .setTag(FIREBASE_REMINDER_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(0, REMINDER_INTERVAL_SECONDS ))
                .setReplaceCurrent(true)
                .build();
        firebaseJobDispatcher.schedule(constraintReminderJob);
        Log.i("TAG", "onStartJob contraint removed"+firebaseJobDispatcher.newJobBuilder().getConstraints().toString());

    }
}
