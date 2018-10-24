package com.dionpapas.drinkyourwater.fragments;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dionpapas.drinkyourwater.R;
import com.dionpapas.drinkyourwater.utilities.Utilities;

public class MainFragment extends Fragment {

    private TextView mWaterCountDisplay, mNetworkDisplay;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        mWaterCountDisplay = view.findViewById(R.id.tv_water_count);
        mNetworkDisplay = view.findViewById(R.id.tv_networkView);
        mNetworkDisplay.setVisibility(View.INVISIBLE);
    }

    private void updateWaterCount() {
        int waterCount = Utilities.getWaterCount(getContext());
        mWaterCountDisplay.setText(waterCount+"");
    }

    public void testSaving(View view) {
        updateWaterCount();
    }

}
