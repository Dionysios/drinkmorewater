package com.dionpapas.drinkyourwater;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

class NotificationBuilder {

    private static final int WATER_REMINDER_INTENT_ID = 1111;
    private static final int WATER_REMINDER_PENDING_INTENT_ID = 3417;
    private static final String WATER_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";
    private static final int ACTION_DRINK_WATER_INTENT_ID = 111;
    private static final int ACTION_IGNORE_WATER_INTENT_ID = 141;

    public static void createNotification(Context context){
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    WATER_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,WATER_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_water_glass)
                .setLargeIcon(notificationIcon(context))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                .setContentText(context.getString(R.string.charging_reminder_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.charging_reminder_notification_body)))
                .setDefaults(android.app.Notification.DEFAULT_VIBRATE)
                .setContentIntent(launchAppIntent(context))
                .addAction(ignoreDrinkWaterReminder(context))
                .addAction(drinkWaterReminder(context))
                .setPriority(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(WATER_REMINDER_INTENT_ID, notificationBuilder.build());
    }

    private static PendingIntent launchAppIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                WATER_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap notificationIcon(Context context) {
        Resources res = context.getResources();
        Bitmap notificationIcon = BitmapFactory.decodeResource(res, R.drawable.ic_water_glass);
        return notificationIcon;
    }

    public static void clearNotification(Context context ){
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static NotificationCompat.Action ignoreDrinkWaterReminder (Context context){
        Intent ignoreDrinkWaterReminderIntent = new Intent(context, ReminderIntent.class);
        ignoreDrinkWaterReminderIntent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);
        PendingIntent ignoreDrinkWaterReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_WATER_INTENT_ID,
                ignoreDrinkWaterReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action ignoreReminderAction =
                new NotificationCompat.Action(R.drawable.ic_water_glass,"Not now",
                        ignoreDrinkWaterReminderPendingIntent);

        return ignoreReminderAction;
    }

    public static NotificationCompat.Action drinkWaterReminder (Context context){
        Intent drinkWaterReminderIntent = new Intent(context, ReminderIntent.class);
        drinkWaterReminderIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
        PendingIntent drinkWaterReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_DRINK_WATER_INTENT_ID,
                drinkWaterReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action drinkingReminderAction =
                new NotificationCompat.Action(R.drawable.ic_water_glass,"Yes!",
                        drinkWaterReminderPendingIntent);

        return drinkingReminderAction;
    }

}