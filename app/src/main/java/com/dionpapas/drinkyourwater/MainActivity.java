package com.dionpapas.drinkyourwater;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dionpapas.drinkyourwater.database.AppDatabase;
import com.dionpapas.drinkyourwater.database.WaterEntry;
import com.dionpapas.drinkyourwater.utilities.NetworkReceiver;
import com.dionpapas.drinkyourwater.utilities.Utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static com.dionpapas.drinkyourwater.utilities.NetworkReceiver.DATE_HAS_CHANGED;
import static com.dionpapas.drinkyourwater.utilities.NetworkReceiver.IS_NETWORK_AVAILABLE;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    private TextView mWaterCountDisplay, mNetworkDisplay;
    private static final String WIFI_STATE_CHANGE_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
    private NetworkReceiver networkStateChangeReceiver;
    String networkStatus;
    private AppDatabase mDb;

    private BroadcastReceiver dateReceiver;

    //Pending intent instance
    private PendingIntent pendingIntent;
    //Alarm Request Code
    private static final int ALARM_REQUEST_CODE = 133;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWaterCountDisplay = findViewById(R.id.tv_water_count);
        mNetworkDisplay = findViewById(R.id.tv_networkView);
        updateWaterCount();
        setupSharedPreferences();

        mDb = AppDatabase.getInstance(getApplicationContext());
        networkStateChangeReceiver = new NetworkReceiver();

        //register Intents
        registerReceiver(networkStateChangeReceiver, new IntentFilter(CONNECTIVITY_ACTION));
        registerReceiver(networkStateChangeReceiver, new IntentFilter(WIFI_STATE_CHANGE_ACTION));
        registerReceiver(networkStateChangeReceiver, new IntentFilter(Intent.ACTION_DATE_CHANGED));
        IntentFilter intentFilter = new IntentFilter(NetworkReceiver.NETWORK_AVAILABLE_ACTION);



        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, ALARM_REQUEST_CODE, alarmIntent, 0);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("TAG", "Getting intent " + intent.getAction());
                if (intent.getAction() == DATE_HAS_CHANGED ) {
                    Log.i("TAG", "Getting intent 1 " + intent.getAction());
                    Utilities.setWaterCount(context, 0);
                } else {
                    boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                    networkStatus = isNetworkAvailable ? "connected" : "disconnected";
                    if (networkStatus.equals("disconnected")){
                        mNetworkDisplay.setVisibility(View.VISIBLE);
                    } else {
                        mNetworkDisplay.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }, intentFilter);

        dateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO: Awesome things
                Log.i("TAG", "I got something here " + intent.getAction());
                Utilities.setWaterCount(context, 0);
            }
        };

        IntentFilter intentFilter2  = new IntentFilter();
        //intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        //intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

        this.registerReceiver(
                dateReceiver,
                new IntentFilter(Intent.ACTION_TIME_CHANGED)
        );

        triggerAlarmManager(getTimeInterval("120"));
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        initializeFireBaseJob(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =  getMenuInflater();
        inflater.inflate(R.menu.settings_menu ,menu);
        return true;
    }

    private void initializeFireBaseJob(SharedPreferences sharedPreferences){
        FireBaseJob.initiaze(this,
                sharedPreferences.getBoolean(getString(R.string.enable_notif_key), getResources().getBoolean(R.bool.pref_enable_notif)),
                sharedPreferences.getBoolean(getString(R.string.notif_on_wifi_key), getResources().getBoolean(R.bool.pref_on_wifi)),
                sharedPreferences.getBoolean(getString(R.string.notif_when_charging_key), getResources().getBoolean(R.bool.pref_when_charg)),
                sharedPreferences.getString(getString(R.string.interval_key), getString(R.string.interval_value)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i("TAG", "onStartJob something changed" + key);
        if(Utilities.KEY_WATER_COUNT.equals(key)) {
            updateWaterCount();
        } else {
            FireBaseJob.cancelAllReminders(this);
            initializeFireBaseJob(sharedPreferences);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final LiveData<List<WaterEntry>> counting = mDb.taskDao().getAllWaterEntries();
        counting.observe(this, new Observer<List<WaterEntry>>() {
            @Override
            public void onChanged(@Nullable List<WaterEntry> waterEntries) {
                Log.i("ADD", "on Resume this is the size" + waterEntries.size());
                if(waterEntries.size() > 0) Log.i("ADD", "on Resume this is the size" + waterEntries.get(0).getCounter() + "and" + waterEntries.get(0).getUpdatedAt());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).
                unregisterOnSharedPreferenceChangeListener(this);
        unregisterReceiver(networkStateChangeReceiver);
        unregisterReceiver(dateReceiver);
    }

    private void updateWaterCount() {
        int waterCount = Utilities.getWaterCount(this);
        mWaterCountDisplay.setText(waterCount+"");
    }

    public void testSaving(View view) {
        Utilities.saveWaterEntry(this);
    }


    private int getTimeInterval(String getInterval) {
        int interval = Integer.parseInt(getInterval);//convert string interval into integer
//        //Return interval on basis of radio button selection
//        if (secondsRadioButton.isChecked())
//            return interval;
//        if (minutesRadioButton.isChecked())
//            return interval * 60;//convert minute into seconds
//        if (hoursRadioButton.isChecked()) return interval * 60 * 60;//convert hours into seconds
        //else return 0
        return interval;
    }

    //Trigger alarm manager with entered time interval
    public void triggerAlarmManager(int alarmTriggerTime) {
        // get a Calendar object with current time
        Calendar cal = Calendar.getInstance();
        // add alarmTriggerTime seconds to the calendar object
        cal.add(Calendar.SECOND, alarmTriggerTime);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);//set alarm manager with entered timer by converting into milliseconds

        Toast.makeText(this, "Alarm Set for " + alarmTriggerTime + " seconds.", Toast.LENGTH_SHORT).show();
    }
}
