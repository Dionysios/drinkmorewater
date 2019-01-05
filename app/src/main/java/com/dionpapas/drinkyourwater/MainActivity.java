package com.dionpapas.drinkyourwater;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dionpapas.drinkyourwater.database.AppDatabase;
import com.dionpapas.drinkyourwater.fragments.CupFragment;
import com.dionpapas.drinkyourwater.fragments.DairyFragment;
import com.dionpapas.drinkyourwater.fragments.MainFragment;
import com.dionpapas.drinkyourwater.fragments.SettingsFragment;
import com.dionpapas.drinkyourwater.utilities.GenericReceiver;
import com.dionpapas.drinkyourwater.utilities.Utilities;

import java.util.Calendar;

import static com.dionpapas.drinkyourwater.AlarmReceiver.RESTART_COUNTER;
import static com.dionpapas.drinkyourwater.utilities.GenericReceiver.DATE_HAS_CHANGED;
import static com.dionpapas.drinkyourwater.utilities.GenericReceiver.IS_NETWORK_AVAILABLE;
import static com.dionpapas.drinkyourwater.utilities.GenericReceiver.NETWORK_AVAILABLE_ACTION;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, NavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnImageClickListener,  CupFragment.OnImageCupClickListener {
    private static final String WIFI_STATE_CHANGE_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
    private GenericReceiver genericReceiver;
    private AppDatabase mDb;
    private DrawerLayout drawer;
    MainFragment mainFragment;
    //Pending intent instance
    private PendingIntent pendingIntent;
    //Alarm Request Code
    private static final int ALARM_REQUEST_CODE = 133;

    private static final String TAG = "GenericReceiverMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFragment = new MainFragment();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        setActionBarTitle(getString(R.string.app_name_2));
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setupSharedPreferences();
        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent backgroundService = new Intent(getApplicationContext(), DateChangedBackgroundService.class);
        startService(backgroundService);
        Log.d(DateChangedReceiver.BroacastFound, "Activity onCreate");


        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, ALARM_REQUEST_CODE, alarmIntent, 0);
       // genericReceiver = new GenericReceiver();
      //  mDateBroadcastReceiver = new DateBroadcastReceiver();
     //   mDateIntentFilter = new IntentFilter();
//        mDateIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
//        mDateIntentFilter.addAction(Intent.ACTION_DATE_CHANGED);

//        //saveDate();
//        //register Intents
       // this.registerReceiver(mDateBroadcastReceiver, new IntentFilter(CONNECTIVITY_ACTION));
       // this.registerReceiver(mDateBroadcastReceiver, new IntentFilter(WIFI_STATE_CHANGE_ACTION));
       // this.registerReceiver(mDateBroadcastReceiver, new IntentFilter(Intent.ACTION_DATE_CHANGED));
//
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intentFilter.addAction(GenericReceiver.NETWORK_AVAILABLE_ACTION);
        intentFilter.addAction(RESTART_COUNTER);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //writeFile("MainActivityReceiver", intent);
                Log.d(DateChangedReceiver.BroacastFound, "Here receiving in MainActivity " + intent.getAction());
                Log.i(TAG, intent.getAction());
                if (intent.getAction().equals(NETWORK_AVAILABLE_ACTION)) {
                    boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                    String networkStatus = isNetworkAvailable ? "connected" : "disconnected";
                    if (networkStatus.equals("disconnected")) {
                       //onInputMainFragment(View.VISIBLE);
                        //showNetworkStatusView(false);
                        mainFragment.mNetworkDisplay.setVisibility(View.VISIBLE);
                    } else {
                       // onInputMainFragment(View.INVISIBLE);
                       mainFragment.mNetworkDisplay.setVisibility(View.INVISIBLE);
                        //showNetworkStatusView(true);
                    }
                }
            }
        }, intentFilter);

        if(savedInstanceState == null) {
            updateWaterCount();
        }

        getSupportFragmentManager ().beginTransaction ().replace (R.id.fragment_container,
                mainFragment).commit ();
        navigationView.setCheckedItem (R.id.nav_main);
        triggerAlarmManager(getTimeInterval("60"));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_main:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                       mainFragment).commit();
                break;
            case R.id.nav_dairy:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DairyFragment()).commit();
                break;
            case R.id.nav_cup:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CupFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i("TAG", "onStartJob something changed" + key);
        if (Utilities.KEY_WATER_COUNT.equals(key)) {
            updateWaterCount();
        } else {
            FireBaseJob.cancelAllReminders(this);
            initializeFireBaseJob(sharedPreferences);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initializeFireBaseJob(SharedPreferences sharedPreferences) {
        FireBaseJob.initiaze(this,
                sharedPreferences.getBoolean(getString(R.string.enable_notif_key), getResources().getBoolean(R.bool.pref_enable_notif)),
                sharedPreferences.getBoolean(getString(R.string.notif_on_wifi_key), getResources().getBoolean(R.bool.pref_on_wifi)),
                sharedPreferences.getBoolean(getString(R.string.notif_when_charging_key), getResources().getBoolean(R.bool.pref_when_charg)),
                sharedPreferences.getString(getString(R.string.interval_key), getString(R.string.interval_value)));
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        initializeFireBaseJob(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).
                unregisterOnSharedPreferenceChangeListener(this);
    }

    public void updateWaterCount() {
        int waterCount = Utilities.getWaterCount(this);
        mainFragment.setWaterCount(waterCount);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onImageCupSelected(int position) {
        Toast.makeText(this, "Position clicked = " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onImageClicked() {
        Utilities.incrementWaterCount(this);
    }

    //get time interval to trigger alarm manager
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

    //Stop/Cancel alarm manager
//    public void stopAlarmManager() {
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        manager.cancel(pendingIntent);//cancel the alarm manager of the pending intent
//        //Stop the Media Player Service to stop sound
//        stopService(new Intent(MainActivity.this, RestartCounterService.class));
//        //remove the notification from notification tray
//        NotificationManager notificationManager = (NotificationManager) this
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancel(AlarmNotificationService.NOTIFICATION_ID);
//        Toast.makeText(this, "Alarm Canceled/Stop by User.", Toast.LENGTH_SHORT).show();

    @Override
    public void showNetworkStatusView(Boolean isActive) {
        mainFragment.setActive(isActive);
    }
}