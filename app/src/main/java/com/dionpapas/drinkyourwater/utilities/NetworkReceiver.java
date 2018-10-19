package com.dionpapas.drinkyourwater.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkReceiver extends BroadcastReceiver {
    public static final String NETWORK_AVAILABLE_ACTION = "com.dionpapas.drinkyourwater.NetworkAvailable";
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";
    public static final String DATE_HAS_CHANGED = "dateHasChanged";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TAG", "Getting intent 3" + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {
            Intent dateIntent = new Intent(DATE_HAS_CHANGED);
            LocalBroadcastManager.getInstance(context).sendBroadcast(dateIntent);
        } else {
            Intent networkStateIntent = new Intent(NETWORK_AVAILABLE_ACTION);
            networkStateIntent.putExtra(IS_NETWORK_AVAILABLE, isConnectedToInternet(context));
            LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent);
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
            Log.e(NetworkReceiver.class.getName(), e.getMessage());
            return false;
        }
    }
}