package com.dionpapas.drinkyourwater.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkReceiverTAG";

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conn =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        Log.d(TAG, "onReceive: " + networkInfo);
//     if (ANY.equals(sPref) && networkInfo != null) {
//            refreshDisplay = true;
//        } else {
//            refreshDisplay = false;
//            // todo show not connected message
//            //Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show();
//        }
    }
}