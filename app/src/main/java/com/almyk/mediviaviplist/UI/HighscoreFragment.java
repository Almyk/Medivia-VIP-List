package com.almyk.mediviaviplist.UI;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almyk.mediviaviplist.Database.HighscoreEntity;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.ViewModel.HighscoreViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HighscoreFragment extends Fragment {
    private HighscoreViewModel mViewModel;
    private HighscoreAdapter mAdapter;

    private String mServer;

    private RecyclerView mRecyclerView;


    public HighscoreFragment() {
    }

    public static HighscoreFragment newInstance() {
        return new HighscoreFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_highscore, container, false);

        mRecyclerView = rootView.findViewById(R.id.rv_highscore);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new HighscoreAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        List<HighscoreEntity> dummyList = new ArrayList<>();
        mAdapter.setEntries(dummyList);

        setupViewModel();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(HighscoreViewModel.class);
        mViewModel.init(mServer);

        mViewModel.getHighscores().observe(this, new Observer<List<HighscoreEntity>>() {
            @Override
            public void onChanged(@Nullable List<HighscoreEntity> highscoreEntities) {
                mAdapter.setEntries(highscoreEntities);
            }
        });
    }

    public void setServer(String server) {
        this.mServer = server.toLowerCase();
    }
}
