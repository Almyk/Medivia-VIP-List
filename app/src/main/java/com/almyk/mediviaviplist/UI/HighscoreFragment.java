package com.almyk.mediviaviplist.UI;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.almyk.mediviaviplist.Database.HighscoreEntity;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.ViewModel.HighscoreViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HighscoreFragment extends Fragment implements Spinner.OnItemSelectedListener {
    private static final String TAG = HighscoreFragment.class.getSimpleName();

    private HighscoreViewModel mViewModel;
    private HighscoreAdapter mAdapter;

    private static String mServer;

    private RecyclerView mRecyclerView;
    private Spinner mVocSpinner;
    private Spinner mSkillSpinner;


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

        mVocSpinner = rootView.findViewById(R.id.spinner_voc);
        mSkillSpinner = rootView.findViewById(R.id.spinner_skill);
        mVocSpinner.setOnItemSelectedListener(this);
        mSkillSpinner.setOnItemSelectedListener(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(mServer + " Highscores");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_highscore, menu);
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(HighscoreViewModel.class);
        mViewModel.init(mServer.toLowerCase());

        mViewModel.getHighscores().observe(this, new Observer<List<HighscoreEntity>>() {
            @Override
            public void onChanged(@Nullable List<HighscoreEntity> highscoreEntities) {
                Log.d(TAG, "Highscore data set changed");
                mAdapter.setEntries(highscoreEntities);
            }
        });
    }

    public void setServer(String server) {
        this.mServer = server;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String value = (String) parent.getItemAtPosition(position);

        List<String> skills = Arrays.asList(getActivity().getResources().getStringArray(R.array.skill_array));
        List<String> vocations = Arrays.asList(getActivity().getResources().getStringArray(R.array.vocation_array));

        if(skills.contains(value)) {
            mViewModel.setSkill(value);
            Log.d(TAG, "Set skill to: "+value);
        } else if(vocations.contains(value)) {
            mViewModel.setVocation(value);
            Log.d(TAG, "Set vocation to: "+value);
        }
        mViewModel.changeHighscoreCategory();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
