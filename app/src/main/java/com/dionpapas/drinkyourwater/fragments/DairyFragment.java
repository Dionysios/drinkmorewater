package com.dionpapas.drinkyourwater.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dionpapas.drinkyourwater.MainActivity;
import com.dionpapas.drinkyourwater.R;
import com.dionpapas.drinkyourwater.adapters.DiaryAdapter;
import com.dionpapas.drinkyourwater.database.AppDatabase;
import com.dionpapas.drinkyourwater.database.WaterEntry;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class DairyFragment extends Fragment {

    private DiaryAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private static AppDatabase mDb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDb = AppDatabase.getInstance(getActivity());
        return inflater.inflate(R.layout.fragment_diary, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set the RecyclerView to its corresponding view
        // Set title bar
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.progress));
        //Toolbar toolbar = view.findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mRecyclerView = view.findViewById(R.id.recyclerViewWaterEntries);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new DiaryAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);
        getAllEntries();
    }

    public void getAllEntries(){
        final LiveData<List<WaterEntry>> counting = mDb.taskDao().getAllWaterEntries();
        counting.observe(this, new Observer<List<WaterEntry>>() {
            @Override
            public void onChanged(@Nullable List<WaterEntry> waterEntries) {
                mAdapter.setwaterEntries(waterEntries);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.progress));
    }
}
