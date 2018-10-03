package com.dionpapas.drinkyourwater;

import android.content.Context;
import android.util.Log;

public class ReminderTasks {
    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    public static final String SEND_NOTIFICATION = "send_notification";

    public static void executeTask(Context context, String action) {
        Log.d("Action", "this is the action" + action);
        if (ACTION_INCREMENT_WATER_COUNT.equals(action)) {
            incrementWaterCount(context);
            //NotificationBuilder.createNotification(context);
        } else if (ACTION_DISMISS_NOTIFICATION.equals(action)){
            NotificationBuilder.clearNotification(context);
        } else if (SEND_NOTIFICATION.equals(action)){
            sendNotification(context);
        }

    }

    private static void incrementWaterCount(Context context) {
       // Utilities.incrementWaterCount(context);
        NotificationBuilder.clearNotification(context);
    }

    synchronized public static void sendNotification(Context context) {
        try {
            NotificationBuilder.createNotification(context);
        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }
    }

}