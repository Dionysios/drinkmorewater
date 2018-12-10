package com.dionpapas.drinkyourwater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static com.dionpapas.drinkyourwater.utilities.GenericReceiver.DATE_HAS_CHANGED;
import static com.dionpapas.drinkyourwater.utilities.GenericReceiver.IS_NETWORK_AVAILABLE;
import static com.dionpapas.drinkyourwater.utilities.GenericReceiver.NETWORK_AVAILABLE_ACTION;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, NavigationView.OnNavigationItemSelectedListener, MainFragment.FragmentMainListener {
    private static final String WIFI_STATE_CHANGE_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
    private GenericReceiver genericReceiver;
    private AppDatabase mDb;
    private DrawerLayout drawer;
    private MainFragment mainFragment;
    DateBroadcastReceiver mDateBroadcastReceiver;
    IntentFilter mDateIntentFilter;

    private static final String TAG = "GenericReceiverMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        mainFragment = new MainFragment();
        setupSharedPreferences();
        mDb = AppDatabase.getInstance(getApplicationContext());
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
        intentFilter.addAction(DATE_HAS_CHANGED);
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intentFilter.addAction(GenericReceiver.NETWORK_AVAILABLE_ACTION);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //writeFile("MainActivityReceiver", intent);
                Log.i(TAG, intent.getAction());
                if (intent.getAction().equals(DATE_HAS_CHANGED)) {
                 //   Utilities.saveWaterEntry(context);
                 //   Utilities.setWaterCount(context, 0);
                    updateWaterCount();
                } else if (intent.getAction().equals(NETWORK_AVAILABLE_ACTION)) {
                    boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                    String networkStatus = isNetworkAvailable ? "connected" : "disconnected";
                    if (networkStatus.equals("disconnected")) {
                        onInputMainFragment(View.VISIBLE);
                    } else {
                        onInputMainFragment(View.INVISIBLE);
                    }
                }
            }
        }, intentFilter);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mainFragment).commit();
            navigationView.setCheckedItem(R.id.nav_main);
        }
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
        //IntentFilter ifilter = new IntentFilter(Intent.ACTION_DATE_CHANGED);
        //Intent currentBatteryStatusIntent = registerReceiver(null, ifilter);
       // Log.i("Intent1", "Getting one intent 22" + currentBatteryStatusIntent);



        //IntentFilter ifilter2 = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        // COMPLETED (6) Set a new Intent object equal to what is returned by registerReceiver, passing in null
        // for the receiver. Pass in your intent filter as well. Passing in null means that you're
        // getting the current state of a sticky broadcast - the intent returned will contain the
        // battery information you need.
        //Intent currentBatteryStatusIntent2 = registerReceiver(null, ifilter2);
        // COMPLETED (7) Get the integer extra BatteryManager.EXTRA_STATUS. Check if it matches
        // BatteryManager.BATTERY_STATUS_CHARGING or BatteryManager.BATTERY_STATUS_FULL. This means
        // the battery is currently charging.
       // int batteryStatus = currentBatteryStatusIntent2.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
       // boolean isCharging = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING ||
         //       batteryStatus == BatteryManager.BATTERY_STATUS_FULL;

       // Log.i("Intent1", "Getting one intent 33" + isCharging);
      //  int batteryStatus = currentBatteryStatusIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
      //  Log.i("Intent1", "Getting one battery 22" + batteryStatus);
        // COMPLETED (8) Update the UI using your showCharging method
       // registerReceiver(mDateBroadcastReceiver, mDateIntentFilter);
    }

    private void initializeFireBaseJob(SharedPreferences sharedPreferences) {
        FireBaseJob.initiaze(this,
                sharedPreferences.getBoolean(getString(R.string.enable_notif_key), getResources().getBoolean(R.bool.pref_enable_notif)),
                sharedPreferences.getBoolean(getString(R.string.notif_on_wifi_key), getResources().getBoolean(R.bool.pref_on_wifi)),
                sharedPreferences.getBoolean(getString(R.string.notif_when_charging_key), getResources().getBoolean(R.bool.pref_when_charg)),
                sharedPreferences.getString(getString(R.string.interval_key), getString(R.string.interval_value)),
                sharedPreferences.getString("DATE", ""));
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
       // unregisterReceiver(genericReceiver);
    }

    public void updateWaterCount() {
        int waterCount = Utilities.getWaterCount(this);
        mainFragment.updateWaterCount(waterCount);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onInputMainFragment(int input) {
        mainFragment.updateNetworkDisplay(input);
    }


    private class DateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("Intent1", "Getting one intent 11" + action);
            //   Utilities.saveWaterEntry(context);
//                 //   Utilities.setWaterCount(context, 0);
           // showCharging(isCharging);
        }
    }

//    public void saveDate() {
//        String todaysDate = Utilities.getTodaysDate();
//        Log.i("ADD", "This is the date" + todaysDate);
//        SharedPreferences sharedPreferences = getSharedPreferences(SharedPref, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("DATE", todaysDate);
//    }

//    private File createFile(String filename) throws IOException {
//        final File sharedFolder = new File(getFilesDir(), SHARED_FOLDER);
//        sharedFolder.mkdirs();
//        File sharedFile = new File(getExternalFilesDir(SHARED_FOLDER), filename);
//        mFileExists = sharedFile.exists();
//        if (!mFileExists) {
//            sharedFile.createNewFile();
//            String columnString = "\"Intent\",\"Action\",\"Date\"" + "\n";
//            FileOutputStream fos = new FileOutputStream(sharedFile);
//            fos.write(columnString.getBytes());
//            fos.close();
//            Log.d(LOG_TAG, "Creating file with colums");
//        } else {
//            Log.d(LOG_TAG, "File already exists");
//        }
//        return sharedFile;
//    }

//    public void writeFile(String function, Intent intent) {
//        FileOutputStream fos;
//        Long tsLong = System.currentTimeMillis()/1000;
//        String ts = tsLong.toString();
////        try {
////            fos = new FileOutputStream(sharedFile, true);
//            String action = intent.getAction() + ",";
//            String timestamp =  ts + "\n";
//            String combinedString = action + function + "," + timestamp;
////            fos.write(combinedString.getBytes());
////            fos.close();
////            outputStream = openFileOutput(destination.getName(), Context.MODE_PRIVATE);
////           // Toast.makeText(, "Writing intent to file, cleaning db", Toast.LENGTH_SHORT).show();
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//        //String filename = "myfile";
//        //String fileContents = "Hello world!";
//        FileOutputStream outputStream;
//
//        try {
//            outputStream = openFileOutput(String.valueOf(sharedFile), Context.MODE_APPEND);
//            outputStream.write(combinedString.getBytes());
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}