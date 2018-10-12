package com.dionpapas.drinkyourwater;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.dionpapas.drinkyourwater.utilities.Utilities;
import com.firebase.jobdispatcher.Constraint;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    private TextView mWaterCountDisplay;
    ListPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWaterCountDisplay = findViewById(R.id.tv_water_count);
        updateWaterCount();
        setupSharedPreferences();
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //myFrequencyList = (Preference) findPreference("frequency_key");
        initializeFirebaseJob(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =  getMenuInflater();
        inflater.inflate(R.menu.settings_menu ,menu);
        return true;
    }

    private void initializeFirebaseJob(SharedPreferences sharedPreferences){
        FireBaseJob.initiaze(this,
                sharedPreferences.getBoolean(getString(R.string.enable_notif_key), getResources().getBoolean(R.bool.pref_enable_notif)),
                sharedPreferences.getBoolean(getString(R.string.notif_on_wifi_key), getResources().getBoolean(R.bool.pref_on_wifi)),
                sharedPreferences.getBoolean(getString(R.string.notif_when_charging_key), getResources().getBoolean(R.bool.pref_when_charg)),
                sharedPreferences.getString(getString(R.string.interval_key), getString(R.string.interval_value)));
       // pref.findIndexOfValue(getString(R.string.interval_key));
//        ListPreference themePref = (ListPreference) sharedPreference;
//        pref.setSummary(R.string.interval_value);
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
            initializeFirebaseJob(sharedPreferences);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).
                unregisterOnSharedPreferenceChangeListener(this);

    }

    private void updateWaterCount() {
        int waterCount = Utilities.getWaterCount(this);
        mWaterCountDisplay.setText(waterCount+"");
    }
}
