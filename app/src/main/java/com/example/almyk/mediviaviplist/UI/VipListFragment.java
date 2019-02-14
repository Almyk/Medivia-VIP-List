package com.example.almyk.mediviaviplist.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class VipListFragment extends Fragment {
    private static final String TAG = VipListFragment.class.getSimpleName();

    private VipListViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private VipListAdapter mAdapter;

    private Button mUpdateButton;
    private Button mAddPlayerButton;
    private EditText mNewPlayerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vip_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.rv_vip);
        if (mRecyclerView == null) {
            Log.d(TAG, "mRecyclerView is null");
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new VipListAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mUpdateButton = rootView.findViewById(R.id.bt_update);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getOnlinePlayers();
            }
        });

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
}
