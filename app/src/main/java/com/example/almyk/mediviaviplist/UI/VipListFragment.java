package com.example.almyk.mediviaviplist.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.almyk.mediviaviplist.Database.PlayerEntity;
import com.example.almyk.mediviaviplist.R;
import com.example.almyk.mediviaviplist.ViewModel.VipListViewModel;

import java.util.List;

public class VipListFragment extends Fragment {
    private static final String TAG = VipListFragment.class.getSimpleName();

    private VipListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vip_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(VipListViewModel.class);
        viewModel.init();

        viewModel.getVipList().observe(this, new Observer<List<PlayerEntity>>() {
            @Override
            public void onChanged(@Nullable List<PlayerEntity> playerEntities) {
                // TODO : update adapter that fills a RecyclerView
            }
        });
    }

    public static VipListFragment newInstance() {
        return new VipListFragment();
    }
}
