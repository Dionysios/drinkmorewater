package com.dionpapas.drinkyourwater.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dionpapas.drinkyourwater.MainActivity;
import com.dionpapas.drinkyourwater.R;

public class CupFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set the RecyclerView to its corresponding view
        // Set title bar
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.cup));
        //Toolbar toolbar = view.findViewById(R.id.toolbar);
    }
}
