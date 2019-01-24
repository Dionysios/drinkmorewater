package com.dionpapas.drinkyourwater;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dionpapas.drinkyourwater.utilities.Utilities;

public class RestartCounterService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utilities.saveWaterEntry (this);
        Utilities.setWaterCount(this,0 );
        Log.i("Here is Alarm", "here is alarm1");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
