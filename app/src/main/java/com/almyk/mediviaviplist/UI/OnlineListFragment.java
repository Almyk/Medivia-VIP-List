package com.almyk.mediviaviplist.UI;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.ViewModel.OnlineListViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnlineListFragment extends Fragment {
    private OnlineListViewModel mViewModel;

    private String mServer;

    public static OnlineListFragment newInstance() {
        return new OnlineListFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_online_list, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(OnlineListViewModel.class);
        mViewModel.init(mServer);

        mViewModel.getOnlineList().observe(this, new Observer<List<PlayerEntity>>() {
            @Override
            public void onChanged(@Nullable List<PlayerEntity> playerEntities) {
                // TODO update recycle view adapter here
            }
        });
    }

    public void setServer(String server) {
        this.mServer = server;
    }
}
