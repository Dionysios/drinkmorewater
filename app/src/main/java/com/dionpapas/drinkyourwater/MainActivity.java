package com.dionpapas.drinkyourwater;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dionpapas.drinkyourwater.utilities.Utilities;
import com.firebase.jobdispatcher.Constraint;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSharedPreferences();
    }

        private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        FireBaseJob.initiaze(this, sharedPreferences.getBoolean(getString(R.string.enable_notif_key),
                getResources().getBoolean(R.bool.pref_enable_notif)));
            boolean isCharging = sharedPreferences.getBoolean(
                    getString(R.string.notif_when_charging_key), getResources().getBoolean(R.bool.pref_enable_when_charg));
            if(isCharging){
                Log.i("TAG", "onStartJob adding on initiliaze contrain");
            } else {
                FireBaseJob.addConstrain(Constraint.DEVICE_CHARGING);
                Log.i("TAG", "onStartJob removing contrain");
                FireBaseJob.removeConstrain(Constraint.DEVICE_CHARGING);
            }
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =  getMenuInflater();
        inflater.inflate(R.menu.settings_menu ,menu);
        return true;
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
            // updateWaterCount();
        } else if(key.equals(getString(R.string.enable_notif_key))) {
            boolean active = sharedPreferences.getBoolean(
                    getString(R.string.enable_notif_key), getResources().getBoolean(R.bool.pref_enable_notif));
            if (active) {
                Log.i("TAG", "onStartJob starting notifications" + active);
                FireBaseJob.initiaze(this, active);
            } else {
                Log.i("TAG", "onStartJob canceling notifications" + active);
                FireBaseJob.cancelAllReminders();
            }
        } else if(key.equals(getString(R.string.notif_when_charging_key))){
            boolean isCharging = sharedPreferences.getBoolean(
                    getString(R.string.notif_when_charging_key), getResources().getBoolean(R.bool.pref_enable_when_charg));
            if(isCharging){
                Log.i("TAG", "onStartJob adding contrain");
                FireBaseJob.addConstrain(Constraint.DEVICE_CHARGING);
            } else {
                Log.i("TAG", "onStartJob removing contrain");
                FireBaseJob.removeConstrain(Constraint.DEVICE_CHARGING);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).
                unregisterOnSharedPreferenceChangeListener(this);

    }
}
