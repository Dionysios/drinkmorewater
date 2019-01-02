package com.dionpapas.drinkyourwater;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

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

        //
        Utilities.setWaterCount(this,0 );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        //On destory stop and release the media player
//        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//            mediaPlayer.stop();
//            mediaPlayer.reset();
//            mediaPlayer.release();
//        }
    }
}
