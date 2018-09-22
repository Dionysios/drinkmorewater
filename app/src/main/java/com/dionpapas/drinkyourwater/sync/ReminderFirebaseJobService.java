package com.dionpapas.drinkyourwater.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;

public class ReminderFirebaseJobService extends JobService{

    private AsyncTask mTask;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
