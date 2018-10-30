package com.dionpapas.drinkyourwater.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dionpapas.drinkyourwater.MainActivity;
import com.dionpapas.drinkyourwater.R;
import com.dionpapas.drinkyourwater.utilities.Utilities;

public class MainFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{

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
        setupSharedPreferences();
        updateWaterCount();
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ADD", "Here 0");
                Utilities.incrementWaterCount(getContext());
            }
        });
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void updateWaterCount() {
        int waterCount = Utilities.getWaterCount(getContext());
        mWaterCountDisplay.setText(waterCount+"");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i("ADD", "Here 4" + key);
        if(Utilities.KEY_WATER_COUNT.equals(key)) {
            updateWaterCount();
        } else {
            //FireBaseJob.cancelAllReminders(this);
            //initializeFireBaseJob(sharedPreferences);
        }
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.app_name_2));
    }
}
