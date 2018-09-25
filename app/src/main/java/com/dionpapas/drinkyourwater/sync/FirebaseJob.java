package com.dionpapas.drinkyourwater.sync;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class FirebaseJob {

    public static final int REMINDER_INTERVAL_MINUTES = 1;
    public static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    public static final int REMINDER_WINDOW_TIME = REMINDER_INTERVAL_SECONDS;
    public static final String FIREBASE_REMINDER_TAG = "reminder_tag";
    public static boolean sInitialized;

    synchronized public static void scheduleReminder(@NonNull final Context context) {
        if(sInitialized) return;
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(ReminderFireBaseJobService.class)
                // uniquely identifies the job
                .setTag(FIREBASE_REMINDER_TAG)
                // one-off job
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 0 and 15 minutes (900 seconds)
                .setTrigger(Trigger.executionWindow(0, REMINDER_INTERVAL_SECONDS))
                // overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                // constraints that need to be satisfied for the job to run
                .build();
        dispatcher.schedule(myJob);
        sInitialized = true;
    }
}
