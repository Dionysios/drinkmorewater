package com.dionpapas.drinkyourwater.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class GenericReceiver extends BroadcastReceiver {
    public static final String NETWORK_AVAILABLE_ACTION = "com.dionpapas.drinkyourwater.NetworkAvailable";
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";
    public static final String DATE_HAS_CHANGED = "com.dionpapas.drinkyourwater.dateHasChanged";
    private static final String TAG = "GenericReceiver1";

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d(TAG, intent.getAction());
      //  MainActivity.writeFile("GeneralReceiver", intent);
        if (intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)){
            Intent networkStateIntent = new Intent(NETWORK_AVAILABLE_ACTION);
            networkStateIntent.putExtra(IS_NETWORK_AVAILABLE, isConnectedToInternet(context));
            LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent);
        } else if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
            Log.d(TAG, intent.getAction());
            Intent networkStateIntent = new Intent(NETWORK_AVAILABLE_ACTION);
            networkStateIntent.putExtra(IS_NETWORK_AVAILABLE, isConnectedToInternet(context));
            LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent);
        } else if (intent.getAction().equals("android.intent.action.DATE_CHANGED")) {
        //} else if (intent.getAction().equals("android.intent.action.TIME_SET")) {
            Intent dateIntent = new Intent(DATE_HAS_CHANGED);
            LocalBroadcastManager.getInstance(context).sendBroadcast(dateIntent);
        } else {
            Log.i("TAG", "Action can not be handle" + intent.getAction());
        }
    }

    private boolean isConnectedToInternet(Context context) {
        try {
            if (context != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
//                ConnectivityManager.NetworkCallback networkCallback = createNetworkCallback(subscriber, context);
//                final NetworkRequest networkRequest = new NetworkRequest.Builder().build();
//                connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
            return false;
        } catch (Exception e) {
            Log.e(GenericReceiver.class.getName(), e.getMessage());
            return false;
        }
    }
}