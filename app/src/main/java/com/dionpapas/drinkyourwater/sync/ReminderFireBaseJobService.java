package com.dionpapas.drinkyourwater.sync;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ReminderFireBaseJobService extends JobService{

    private AsyncTask mTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        Log.i("TAG", "Sending 8");
        mTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Log.i("TAG", "Sending 9");
                Context context = ReminderFireBaseJobService.this;
                Log.i("TAG", "Sending 10");
                ReminderTasks.executeTask(context, ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
                jobFinished(jobParameters, false);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                Log.i("TAG", "Sending 11");
                Log.i("TAG", "Finished job " + jobParameters.toString());
                jobFinished(jobParameters,false );
            }
        };

        mTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i("TAG", "Sending 12");
        if(mTask != null) mTask.cancel(true);
        return true;
    }
}
