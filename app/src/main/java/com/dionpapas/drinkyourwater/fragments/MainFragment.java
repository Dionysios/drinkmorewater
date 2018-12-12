package com.dionpapas.drinkyourwater.fragments;

import android.content.Context;
import android.os.Bundle;
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

public class MainFragment extends Fragment {

    private TextView mWaterCountDisplay, mNetworkDisplay;
    private ImageView mImageView;
    private FragmentMainListener listener;

    public interface FragmentMainListener {
        void onInputMainFragment(int input);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     //  return inflater.inflate(R.layout.fragment_main, container, false);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.app_name_2));
        ((MainActivity) getActivity()).updateWaterCount();
        mWaterCountDisplay = view.findViewById(R.id.tv_water_count);
        mNetworkDisplay = view.findViewById(R.id.tv_networkView);
        mImageView = view.findViewById(R.id.ib_water_increment);
        updateWaterCount( Utilities.getWaterCount(getContext()));
        updateNetworkDisplay(View.INVISIBLE);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ADD", "Here 0");
                Utilities.incrementWaterCount(getContext());
            }
        });

        return view;
    }

    public void updateNetworkDisplay(int status) {
        Log.i("ADD", "Sending visibility" + status);
        if (mNetworkDisplay != null) {
            mNetworkDisplay.setVisibility(status);
        } else {
            Log.i("ADD", "Sending visibility" + status + "everything is null");
        }
    }

    public void updateWaterCount(int waterCount) {
        if (mWaterCountDisplay != null) {
            mWaterCountDisplay.setText(waterCount+"");
            //mNetworkDisplay.setVisibility(status);
        } else {
            Log.i("ADD", "Sending water count " + waterCount + "everything is null");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentMainListener) {
            listener = (FragmentMainListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
