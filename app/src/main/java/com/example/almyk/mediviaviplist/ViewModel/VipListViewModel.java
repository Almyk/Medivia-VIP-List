package com.example.almyk.mediviaviplist.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.almyk.mediviaviplist.AppExecutors;
import com.example.almyk.mediviaviplist.Database.AppDatabase;
import com.example.almyk.mediviaviplist.Database.PlayerEntity;
import com.example.almyk.mediviaviplist.Repository.DataRepository;

import java.util.HashMap;
import java.util.List;

public class VipListViewModel extends AndroidViewModel {
    private DataRepository mRepository;
    private AppExecutors mExecutors;

    private LiveData<List<PlayerEntity>> mVipList;
    private HashMap<String, PlayerEntity> mOnlineList;

    public VipListViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        mRepository = DataRepository.getInstance(database);
        mExecutors = AppExecutors.getInstance();

        init();
    }

    private void init() {
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

    private void getOnlinePlayers() {
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
            if(mOnlineList.containsKey(player.getName())) {
                mRepository.updatePlayer(mOnlineList.get(player.getName()));
            } else {
                player.setOnline(false);
                mRepository.updatePlayer(player);
            }
        }
    }

    public LiveData<List<PlayerEntity>> getVipList() {
        return mVipList;
    }
}
