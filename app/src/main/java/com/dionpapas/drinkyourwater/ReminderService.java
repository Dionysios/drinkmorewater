package com.dionpapas.drinkyourwater;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class ReminderService extends JobService {
    /**
     * This asynctask will run a job once conditions are met with the constraints
     * As soon as user device gets connected with the power supply. it will generate
     * a notification showing that condition is met.
     */
    private AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {



                Context context = ReminderService.this;
                ReminderTasks.executeTask(context, ReminderTasks.SEND_NOTIFICATION);
                Log.i("TAG", "onStartJob");
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                /* false means, that job is done. we don't want to reschedule it*/
                jobFinished(jobParameters, false);
                Log.i("TAG", "onStartJob- OnPost");
            }
        };

        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mBackgroundTask != null) {
            mBackgroundTask.cancel(true);
        }
        Log.i("TAG", "onStopJob");
        /* true means, we're not done, please reschedule */
        return true;
    }

    @Override
    public void onDestroy() {
        super.onCreate();

    }


}
