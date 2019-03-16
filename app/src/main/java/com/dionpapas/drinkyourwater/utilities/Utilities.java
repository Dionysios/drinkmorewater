package com.dionpapas.drinkyourwater.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dionpapas.drinkyourwater.R;
import com.dionpapas.drinkyourwater.database.AppDatabase;
import com.dionpapas.drinkyourwater.database.WaterEntry;

import java.util.Date;

public class Utilities {

    public static final String KEY_WATER_COUNT = "water-count";
    public static final String KEY_ALARM_ACTIVATED = "alarm";
    private static final int DEFAULT_COUNT = 0;
    private static AppDatabase mDb;

    synchronized public static void setWaterCount(Context context, int glassesOfWater) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_WATER_COUNT, glassesOfWater);
        editor.apply();
    }

    public static int getWaterCount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int glassesOfWater = prefs.getInt(KEY_WATER_COUNT, DEFAULT_COUNT);
        return glassesOfWater;
    }

    synchronized public static void incrementWaterCount(Context context) {
        int waterCount = Utilities.getWaterCount(context);
        Utilities.setWaterCount(context, ++waterCount);
    }

    synchronized public static void setAlarmActive(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(context.getString(R.string.is_alarm_enabled), true);
        editor.apply();
    }

    public static void saveWaterEntry(Context context) {
        mDb = AppDatabase.getInstance(context);
        int counter = getWaterCount(context);
        Date date = new Date();
        final WaterEntry waterEntry = new WaterEntry(counter, date);
        //mDb.taskDao().insertWaterEntry(waterEntry);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.taskDao().insertWaterEntry(waterEntry);
            }
        });
    }
}
