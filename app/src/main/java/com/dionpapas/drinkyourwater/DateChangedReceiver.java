package com.dionpapas.drinkyourwater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dionpapas.drinkyourwater.utilities.GenericReceiver;
import com.dionpapas.drinkyourwater.utilities.Utilities;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class DateChangedReceiver extends BroadcastReceiver {
    public final static String BroacastFound = "Broacast";
    public static final String NETWORK_AVAILABLE_ACTION = "com.dionpapas.drinkyourwater.NetworkAvailable";
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(Intent.ACTION_DATE_CHANGED.equals(action)) {
            Log.d(BroacastFound, intent.getAction());
            Utilities.saveWaterEntry(context);
            Utilities.setWaterCount(context, 0);
        } else if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)){
            Log.d(BroacastFound, intent.getAction());
            Intent networkStateIntent = new Intent(NETWORK_AVAILABLE_ACTION);
            networkStateIntent.putExtra(IS_NETWORK_AVAILABLE, isConnectedToInternet(context));
            LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent);
            Utilities.saveWaterEntry(context);
            Utilities.setWaterCount(context, 0);
        } else if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
            Log.d(BroacastFound, intent.getAction());
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
            Log.e(GenericReceiver.class.getName(), e.getMessage());
            return false;
        }
    }
}
