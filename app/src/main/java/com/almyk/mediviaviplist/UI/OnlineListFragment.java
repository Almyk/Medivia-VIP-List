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

import com.almyk.mediviaviplist.Database.Entities.PlayerEntity;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.ViewModel.OnlineListViewModel;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineListFragment extends Fragment {
    private static final String TAG = OnlineListFragment.class.getSimpleName();

    private static OnlineListViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private OnlineListAdapter mAdapter;

    private static String mServer;

    public static OnlineListFragment newInstance() {
        return new OnlineListFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_online_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.rv_online_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), VERTICAL));
        mAdapter = new OnlineListAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

//        // Fixes bug that causes app to crash when app is newly started and user quickly changes to this view
//        List<PlayerEntity> emptyList = new ArrayList<>();
//        mAdapter.setOnlineList(emptyList);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(OnlineListViewModel.class);
        mViewModel.init(mServer.toLowerCase());

        mViewModel.getOnlineList().observe(this, new Observer<List<PlayerEntity>>() {
            @Override
            public void onChanged(@Nullable List<PlayerEntity> playerEntities) {
                mAdapter.setOnlineList(playerEntities);
                ((MainActivity) getActivity()).getSupportActionBar()
                        .setTitle(mServer + " (" + playerEntities.size() + ")");
            }
        });
    }

    public void setServer(String server) {
        this.mServer = server;
    }

    public static void addVip(String name) {
        mViewModel.addVip(name);
    }
}
