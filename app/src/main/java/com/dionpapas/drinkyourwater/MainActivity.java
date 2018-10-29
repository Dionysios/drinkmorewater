package com.dionpapas.drinkyourwater;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dionpapas.drinkyourwater.database.AppDatabase;
import com.dionpapas.drinkyourwater.database.WaterEntry;
import com.dionpapas.drinkyourwater.fragments.DairyFragment;
import com.dionpapas.drinkyourwater.fragments.MainFragment;
import com.dionpapas.drinkyourwater.fragments.SettingsFragment;
import com.dionpapas.drinkyourwater.utilities.GenericReceiver;
import com.dionpapas.drinkyourwater.utilities.Utilities;

import java.util.List;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static com.dionpapas.drinkyourwater.utilities.GenericReceiver.DATE_HAS_CHANGED;
import static com.dionpapas.drinkyourwater.utilities.GenericReceiver.IS_NETWORK_AVAILABLE;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, NavigationView.OnNavigationItemSelectedListener{
    private static final String WIFI_STATE_CHANGE_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
    private GenericReceiver genericReceiver;
    String networkStatus;
    private AppDatabase mDb;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //updateWaterCount();
        setupSharedPreferences();

        mDb = AppDatabase.getInstance(getApplicationContext());
        genericReceiver = new GenericReceiver();

        IntentFilter intentFilterDate = new IntentFilter();
        intentFilterDate.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilterDate.addAction(Intent.ACTION_DATE_CHANGED);
        intentFilterDate.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        //register Intents
        this.registerReceiver(genericReceiver, new IntentFilter(CONNECTIVITY_ACTION));
        this.registerReceiver(genericReceiver, new IntentFilter(WIFI_STATE_CHANGE_ACTION));
//        this.registerReceiver(genericReceiver, new IntentFilter(Intent.ACTION_TIME_CHANGED));
        this.registerReceiver(genericReceiver, new IntentFilter(Intent.ACTION_DATE_CHANGED));
//        this.registerReceiver(genericReceiver, new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED));
//        this.registerReceiver(genericReceiver, intentFilterDate);
        //Register intents to local receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GenericReceiver.DATE_HAS_CHANGED);
        intentFilter.addAction(GenericReceiver.NETWORK_AVAILABLE_ACTION);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(DATE_HAS_CHANGED)) {
                    Utilities.saveWaterEntry(context);
                    Utilities.setWaterCount(context, 0);
                } else {
                    boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                    networkStatus = isNetworkAvailable ? "connected" : "disconnected";
                    if (networkStatus.equals("disconnected")){
                        //todo setVIsibility it show be called from Fragment
                       // mNetworkDisplay.setVisibility(View.VISIBLE);
                    } else {
                      //  mNetworkDisplay.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }, intentFilter);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MainFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_main);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_main:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MainFragment()).commit();
                break;
            case R.id.nav_dairy:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DairyFragment()).commit();
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

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        initializeFireBaseJob(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater =  getMenuInflater();
//        inflater.inflate(R.menu.settings_menu ,menu);
//        return true;
//    }

    private void initializeFireBaseJob(SharedPreferences sharedPreferences){
        FireBaseJob.initiaze(this,
                sharedPreferences.getBoolean(getString(R.string.enable_notif_key), getResources().getBoolean(R.bool.pref_enable_notif)),
                sharedPreferences.getBoolean(getString(R.string.notif_on_wifi_key), getResources().getBoolean(R.bool.pref_on_wifi)),
                sharedPreferences.getBoolean(getString(R.string.notif_when_charging_key), getResources().getBoolean(R.bool.pref_when_charg)),
                sharedPreferences.getString(getString(R.string.interval_key), getString(R.string.interval_value)));
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if(id == R.id.action_settings){
//            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
//            startActivity(startSettingsActivity);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
        unregisterReceiver(genericReceiver);
    }

    private void updateWaterCount() {
        int waterCount = Utilities.getWaterCount(this);
       // mWaterCountDisplay.setText(waterCount+"");
    }
}
