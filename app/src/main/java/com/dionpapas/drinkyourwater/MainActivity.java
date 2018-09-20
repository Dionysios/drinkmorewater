package com.dionpapas.drinkyourwater;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dionpapas.drinkyourwater.sync.ReminderIntent;
import com.dionpapas.drinkyourwater.sync.ReminderTasks;
import com.dionpapas.drinkyourwater.utilities.NotificationBuilder;
import com.dionpapas.drinkyourwater.utilities.Utilities;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    private TextView mWaterCountDisplay;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWaterCountDisplay = (TextView) findViewById(R.id.tv_water_count);
        setupSharedPreferences();
        updateWaterCount();

    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.getBoolean(getString(R.string.enable_notif_key),
                getResources().getBoolean(R.bool.pref_enable_notif));
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
        if(Utilities.KEY_WATER_COUNT.equals(key)){
            updateWaterCount();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).
                unregisterOnSharedPreferenceChangeListener(this);

    }

    public void incrementWater(View view) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, R.string.water_text_toast, Toast.LENGTH_SHORT);
        mToast.show();

        NotificationBuilder.createNotification(this);
        Intent incrementWaterCountIntent =  new Intent(this, ReminderIntent.class);
        incrementWaterCountIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
        startService(incrementWaterCountIntent);

    }

    private void updateWaterCount() {
        int waterCount = Utilities.getWaterCount(this);
        mWaterCountDisplay.setText(waterCount+"");
    }
}
