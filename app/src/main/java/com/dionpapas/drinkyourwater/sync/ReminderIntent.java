package com.dionpapas.drinkyourwater.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class ReminderIntent extends IntentService {

    public ReminderIntent() {
        super("ReminderIntent");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        ReminderTasks.executeTask(this,action);
    }
}
