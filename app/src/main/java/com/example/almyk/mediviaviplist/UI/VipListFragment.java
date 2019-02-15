package com.example.almyk.mediviaviplist.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.almyk.mediviaviplist.Database.PlayerEntity;
import com.example.almyk.mediviaviplist.R;
import com.example.almyk.mediviaviplist.ViewModel.VipListViewModel;

import java.util.List;

public class VipListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = VipListFragment.class.getSimpleName();

    private VipListViewModel mViewModel;

    private SwipeRefreshLayout mSwipeContainer;
    private RecyclerView mRecyclerView;
    private VipListAdapter mAdapter;

    private Button mUpdateButton;
    private Button mAddPlayerButton;
    private EditText mNewPlayerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.vip_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.rv_vip);
        if (mRecyclerView == null) {
            Log.e(TAG, "mRecyclerView is null");
        }
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new VipListAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mSwipeContainer = rootView.findViewById(R.id.swipe_container);
        mSwipeContainer.setOnRefreshListener(this);

        mNewPlayerView = rootView.findViewById(R.id.et_player_name);
        mAddPlayerButton = rootView.findViewById(R.id.bt_add_player);
        mAddPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.addPlayer(mNewPlayerView.getText().toString());
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(VipListViewModel.class);
        mViewModel.init();

        mViewModel.getVipList().observe(this, new Observer<List<PlayerEntity>>() {
            @Override
            public void onChanged(@Nullable List<PlayerEntity> playerEntities) {
                mAdapter.setPlayers(playerEntities);
            }
        });
    }

    public static VipListFragment newInstance() {
        return new VipListFragment();
    }

    @Override
    public void onRefresh() {
        mViewModel.getOnlinePlayers();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeContainer.setRefreshing(false);
            }
        }, 2000);
    }
}
