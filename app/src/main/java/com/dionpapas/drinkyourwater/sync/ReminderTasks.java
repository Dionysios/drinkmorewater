package com.dionpapas.drinkyourwater.sync;

import android.content.Context;

import com.dionpapas.drinkyourwater.utilities.Utilities;

/*All background tasks that run in the app */
public class ReminderTasks {
    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";

    public static void executeTask(Context context, String action) {
        if (ACTION_INCREMENT_WATER_COUNT.equals(action)) {
            incrementWaterCount(context);
        }
    }

    private static void incrementWaterCount(Context context) {
        Utilities.incrementWaterCount(context);
    }
}

