package com.dionpapas.drinkyourwater.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.dionpapas.drinkyourwater.database.AppDatabase;
import com.dionpapas.drinkyourwater.database.WaterEntry;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utilities {

    public static final String KEY_WATER_COUNT = "water-count";
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
        Log.i("ADD", "Here 1");
        Utilities.setWaterCount(context, ++waterCount);
        Log.i("ADD", "Here 2");
    }

    synchronized public static int returnWaterCount(Context context) {
        int waterCount = Utilities.getWaterCount(context);
        Log.i("ADD", "Here 1");
        Utilities.setWaterCount(context, ++waterCount);
        Log.i("ADD", "Here 2");
        return waterCount;
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

    public static String getTodaysDate(){
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        return currentDate;
    }
}
