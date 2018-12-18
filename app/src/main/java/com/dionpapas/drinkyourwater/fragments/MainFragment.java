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

import com.dionpapas.drinkyourwater.R;

public class MainFragment extends Fragment {

    public TextView mWaterCountDisplay, mNetworkDisplay;
    public static final String LIST_INDEX = "list_index";
    private int mCount;
    private boolean isActive;
    //private ImageView mImageView;
    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnImageClickListener mCallbackClick;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnImageClickListener {
        void onImageClicked();

        void showNetworkStatusView(Boolean isActive);
    }
    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallbackClick = (OnImageClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     //  return inflater.inflate(R.layout.fragment_main, container, false);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ImageView mImageView = (ImageView) rootView.findViewById(R.id.ib_water_increment);
      //  ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.app_name_2));
      //  ((MainActivity) getActivity()).updateWaterCount();
        mWaterCountDisplay = rootView.findViewById(R.id.tv_water_count);
        mNetworkDisplay = rootView.findViewById(R.id.tv_networkView);

        if(savedInstanceState != null) {
            mCount = savedInstanceState.getInt(LIST_INDEX);
            //mWaterCountDisplay.setText(mCount+"");
        }

        mWaterCountDisplay.setText(mCount+"");
        if (isActive) {
            mNetworkDisplay.setVisibility(View.VISIBLE);
        } else {
            mNetworkDisplay.setVisibility(View.INVISIBLE);
        }

        //mImageView = view.findViewById(R.id.ib_water_increment);
        //updateWaterCount( Utilities.getWaterCount(getContext()));
        //updateNetworkDisplay(View.INVISIBLE);
        //todo NetworkDisplay currently not working
        mImageView.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                // Trigger the callback method and pass in the position that was clicked
                Log.i("ADD", "Here 0");
                mCount++;
                mCallbackClick.onImageClicked();
                mWaterCountDisplay.setText(mCount+"");
            }
        });

        return rootView;
    }

    public void setWaterCount(int count) {
        mCount = count;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putInt(LIST_INDEX, mCount);
    }
//    public void updateNetworkDisplay(int status) {
//        Log.i("ADD", "Sending visibility" + status);
//        if (mNetworkDisplay != null) {
//            mNetworkDisplay.setVisibility(status);
//        } else {
//            Log.i("ADD", "Sending visibility" + status + "everything is null");
//        }
//    }

//    public void updateWaterCount(int waterCount) {
//        if (mWaterCountDisplay != null) {
//            mWaterCountDisplay.setText(waterCount+"");
//            //mNetworkDisplay.setVisibility(status);
//        } else {
//            Log.i("ADD", "Sending water count " + waterCount + "everything is null");
//        }
//    }
}
