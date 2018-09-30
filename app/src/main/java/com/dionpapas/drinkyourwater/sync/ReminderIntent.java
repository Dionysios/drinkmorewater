package com.dionpapas.drinkyourwater.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class ReminderIntent extends IntentService {

    public ReminderIntent() {
        super("ReminderIntent");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("Action", "this is the action" + intent.getAction());
        String action = intent.getAction();
        ReminderTasks.executeTask(this,action);
        //ReminderTasks.sendNotification(this);
    }

}
