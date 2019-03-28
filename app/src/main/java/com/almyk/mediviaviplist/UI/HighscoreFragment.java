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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.almyk.mediviaviplist.Database.Entities.HighscoreEntity;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.ViewModel.HighscoreViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HighscoreFragment extends Fragment implements Spinner.OnItemSelectedListener {
    private static final String TAG = HighscoreFragment.class.getSimpleName();

    private static HighscoreViewModel mViewModel;
    private HighscoreAdapter mAdapter;

    private static String mServer;

    private RecyclerView mRecyclerView;
    private Spinner mVocSpinner;
    private Spinner mSkillSpinner;
    private LinearLayout mEmptyHighscoreLayout;
    private ProgressBar mProgressBar;


    public HighscoreFragment() {
    }

    public static HighscoreFragment newInstance() {
        return new HighscoreFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        mEmptyHighscoreLayout = rootView.findViewById(R.id.empty_highscore);
        mProgressBar = rootView.findViewById(R.id.indeterminateBar);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.highscore_menu_refresh:
                mViewModel.refreshHighscore();
                Toast.makeText(getActivity(), "Updating all Highscores for " + mServer + "\n\tThis might take a while.", Toast.LENGTH_LONG).show();
                mEmptyHighscoreLayout.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(HighscoreViewModel.class);
        mViewModel.init(mServer.toLowerCase());

        mViewModel.getHighscores().observe(this, new Observer<List<HighscoreEntity>>() {
            @Override
            public void onChanged(@Nullable List<HighscoreEntity> highscoreEntities) {
                Log.d(TAG, "Highscore data set changed");
                mAdapter.setEntries(highscoreEntities);
                mProgressBar.setVisibility(View.GONE);
                if(highscoreEntities.isEmpty()) {
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyHighscoreLayout.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyHighscoreLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    public void setServer(String server) {
        this.mServer = server;
    }

    public static void addVip(String name) {
        mViewModel.addVip(name);
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
