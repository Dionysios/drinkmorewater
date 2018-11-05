package com.dionpapas.drinkyourwater.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dionpapas.drinkyourwater.MainActivity;
import com.dionpapas.drinkyourwater.R;
import com.dionpapas.drinkyourwater.utilities.GenericReceiver;
import com.dionpapas.drinkyourwater.utilities.Utilities;

import static com.dionpapas.drinkyourwater.utilities.GenericReceiver.DATE_HAS_CHANGED;
import static com.dionpapas.drinkyourwater.utilities.GenericReceiver.IS_NETWORK_AVAILABLE;
import static com.dionpapas.drinkyourwater.utilities.GenericReceiver.NETWORK_AVAILABLE_ACTION;

public class MainFragment extends Fragment {

    private TextView mWaterCountDisplay, mNetworkDisplay;
    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.app_name_2));
        mWaterCountDisplay = view.findViewById(R.id.tv_water_count);
        mNetworkDisplay = view.findViewById(R.id.tv_networkView);
        mImageView = view.findViewById(R.id.ib_water_increment);
        mNetworkDisplay.setVisibility(View.INVISIBLE);
        updateWaterCount();
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ADD", "Here 0");
                Utilities.incrementWaterCount(getContext());
                updateWaterCount();
            }
        });
        //Local Broadcast Manager to receive events inside the app
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GenericReceiver.DATE_HAS_CHANGED);
        intentFilter.addAction(GenericReceiver.NETWORK_AVAILABLE_ACTION);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                 if(intent.getAction().equals(NETWORK_AVAILABLE_ACTION)){
                    boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                    String networkStatus = isNetworkAvailable ? "connected" : "disconnected";
                    if (networkStatus.equals("disconnected")){
                        mNetworkDisplay.setVisibility(View.VISIBLE);
                    } else {
                        mNetworkDisplay.setVisibility(View.INVISIBLE);
                    }
                } else if (intent.getAction().equals(DATE_HAS_CHANGED)){
                     updateWaterCount();
                 }
            }
        }, intentFilter);
    }

    private void updateWaterCount() {
        int waterCount = Utilities.getWaterCount(getContext());
        mWaterCountDisplay.setText(waterCount+"");
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateWaterCount();
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.app_name_2));
    }
}
