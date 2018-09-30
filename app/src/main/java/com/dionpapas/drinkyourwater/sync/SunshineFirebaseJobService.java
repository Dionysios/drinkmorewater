package com.dionpapas.drinkyourwater.sync;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;


public class SunshineFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchWeatherTask;

    /**
     * The entry point to your Job. Implementations should offload work to another thread of
     * execution as soon as possible.
     *
     * This is called by the Job Dispatcher to tell us we should start our job. Keep in mind this
     * method is run on the application's main thread, so we need to offload work to a background
     * thread.
     *
     * @return whether there is more work remaining.
     */
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Log.d("TAG", "Sending 5");
        mFetchWeatherTask = new AsyncTask() {
            @Override
            protected Void doInBackground(Object[] params) {
                Log.d("TAG", "Sending 6");
                Context context = getApplicationContext();
                //ReminderTasks.sendNotification(context);
                ReminderTasks.executeTask(context, ReminderTasks.SEND_NOTIFICATION);
                jobFinished(jobParameters, false);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                Log.d("TAG", "Sending 11");
                Log.d("TAG", "Finished job " + jobParameters.toString());
                jobFinished(jobParameters,false );
            }
        };

        mFetchWeatherTask.execute();
        return true;
    }

    /**
     * Called when the scheduling engine has decided to interrupt the execution of a running job,
     * most likely because the runtime constraints associated with the job are no longer satisfied.
     *
     * @return whether the job should be retried
     * @see Job.Builder#setRetryStrategy(RetryStrategy)
     * @see RetryStrategy
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mFetchWeatherTask != null) {
            mFetchWeatherTask.cancel(true);
        }
        return true;
    }
}
