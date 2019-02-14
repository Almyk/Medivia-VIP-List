package com.example.almyk.mediviaviplist.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.almyk.mediviaviplist.AppExecutors;
import com.example.almyk.mediviaviplist.MediviaVipListApp;
import com.example.almyk.mediviaviplist.Database.PlayerEntity;
import com.example.almyk.mediviaviplist.Repository.DataRepository;

import java.util.HashMap;
import java.util.List;


public class VipListViewModel extends AndroidViewModel {
    private static final String TAG = VipListViewModel.class.getSimpleName();

    private static DataRepository mRepository;
    private static AppExecutors mExecutors;

    private LiveData<List<PlayerEntity>> mVipList;
    private HashMap<String, PlayerEntity> mOnlineList;

    public VipListViewModel(@NonNull Application application) {
        super(application);

        mRepository = ((MediviaVipListApp) application).getRepository();
        mExecutors = AppExecutors.getInstance();
    }

    public void init() {
        setupVipList();
        getOnlinePlayers();
    }

    private void setupVipList() {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mVipList = mRepository.getVipList();
            }
        });
    }

    public void getOnlinePlayers() {
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mOnlineList = mRepository.getOnline("prophecy"); // TODO should not hard code server here
                updateVipList();
            }
        });
    }

    private void updateVipList() {
        for(PlayerEntity player : mVipList.getValue()) {
            String name = player.getName();
            if(mOnlineList.containsKey(name)) {
                mRepository.updatePlayer(mOnlineList.get(name));
            } else {
                player.setOnline(false);
                mRepository.updatePlayer(player);
            }
        }
    }

    public LiveData<List<PlayerEntity>> getVipList() {
        return mVipList;
    }

    public void addPlayer(final String name) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                PlayerEntity player = new PlayerEntity(name);
                mRepository.addPlayer(player);
            }
        });

    }
}
