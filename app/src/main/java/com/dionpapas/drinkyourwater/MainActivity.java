package com.dionpapas.drinkyourwater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dionpapas.drinkyourwater.utilities.NetworkReceiver;
import com.dionpapas.drinkyourwater.utilities.Utilities;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static com.dionpapas.drinkyourwater.utilities.NetworkReceiver.IS_NETWORK_AVAILABLE;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    private TextView mWaterCountDisplay, mNetworkDisplay;
    private static final String WIFI_STATE_CHANGE_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
    private NetworkReceiver networkStateChangeReceiver;
    String networkStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWaterCountDisplay = findViewById(R.id.tv_water_count);
        mNetworkDisplay = findViewById(R.id.tv_networkView);
        updateWaterCount();
        setupSharedPreferences();
        networkStateChangeReceiver = new NetworkReceiver();
        registerReceiver(networkStateChangeReceiver, new IntentFilter(CONNECTIVITY_ACTION));
        registerReceiver(networkStateChangeReceiver, new IntentFilter(WIFI_STATE_CHANGE_ACTION));
        IntentFilter intentFilter = new IntentFilter(NetworkReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                networkStatus = isNetworkAvailable ? "connected" : "disconnected";
                if (networkStatus.equals("disconnected")){
                   mNetworkDisplay.setVisibility(View.VISIBLE);
                } else {
                   mNetworkDisplay.setVisibility(View.INVISIBLE);
                }
            }
        }, intentFilter);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).
                unregisterOnSharedPreferenceChangeListener(this);
        unregisterReceiver(networkStateChangeReceiver);
    }

    private void updateWaterCount() {
        int waterCount = Utilities.getWaterCount(this);
        mWaterCountDisplay.setText(waterCount+"");
    }
}
