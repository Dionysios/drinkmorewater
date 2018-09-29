package com.dionpapas.drinkyourwater.sync;

import android.content.Context;
import android.content.Intent;
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

public class FirebaseJob {

    public static final int REMINDER_INTERVAL_MINUTES = 1;
    public static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    public static final int REMINDER_WINDOW_TIME = REMINDER_INTERVAL_SECONDS;
    public static final String FIREBASE_REMINDER_TAG = "reminder_sync";
    private static boolean sInitialized;

    private static final int SYNC_INTERVAL_HOURS = 1;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    synchronized public static void initiaze(@NonNull final Context context) {
        if (sInitialized) return;
        sInitialized = true;
        Log.i("TAG", "Sending 3");
        scheduleFirebaseJobDispatcherSync(context);
        startImmediateSync(context);
//        Driver driver = new GooglePlayDriver(context);
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
//
//        Job myJob = dispatcher.newJobBuilder()
//                // the JobService that will be called
//                .setService(ReminderFireBaseJobService.class)
//                // uniquely identifies the job
//                .setTag(FIREBASE_REMINDER_TAG)
//                // one-off job
//                .setRecurring(true)
//                // don't persist past a device reboot
//                .setLifetime(Lifetime.FOREVER)
//                // start between 0 and 15 minutes (900 seconds)
//                .setTrigger(Trigger.executionWindow(0, REMINDER_INTERVAL_SECONDS))
//                // overwrite an existing job with the same tag
//                .setReplaceCurrent(true)
//                .setConstraints(Constraint.ON_ANY_NETWORK)
//                // retry with exponential backoff
//               // .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
//                // constraints that need to be satisfied for the job to run
//                .build();
//        dispatcher.schedule(myJob);

    }

    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {
        Log.i("TAG", "Sending 4");
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        /* Create the Job to periodically sync Sunshine */
        Job syncSunshineJob = dispatcher.newJobBuilder()
                /* The Service that will be used to sync Sunshine's data */
                .setService(SunshineFirebaseJobService.class)
                /* Set the UNIQUE tag used to identify this Job */
                .setTag(FIREBASE_REMINDER_TAG)
                /*
                 * Network constraints on which this Job should run. We choose to run on any
                 * network, but you can also choose to run only on un-metered networks or when the
                 * device is charging. It might be a good idea to include a preference for this,
                 * as some users may not want to download any data on their mobile plan. ($$$)
                 */
                .setConstraints(Constraint.ON_ANY_NETWORK)
                /*
                 * setLifetime sets how long this job should persist. The options are to keep the
                 * Job "forever" or to have it die the next time the device boots up.
                 */
                .setLifetime(Lifetime.FOREVER)
                /*
                 * We want Sunshine's weather data to stay up to date, so we tell this Job to recur.
                 */
                .setRecurring(true)
                /*
                 * We want the weather data to be synced every 3 to 4 hours. The first argument for
                 * Trigger's static executionWindow method is the start of the time frame when the
                 * sync should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 */
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                /*
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.
                 */
                .setReplaceCurrent(true)
                /* Once the Job is ready, call the builder's build method to return the Job */
                .build();

        /* Schedule the Job with the dispatcher */
        dispatcher.schedule(syncSunshineJob);
    }

    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, ReminderIntent.class);
        context.startService(intentToSyncImmediately);
    }
}
