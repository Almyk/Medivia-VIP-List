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
    private HashMap<String, PlayerEntity> mOnlineListProphecy;
    private HashMap<String, PlayerEntity> mOnlineListLegacy;
    private HashMap<String, PlayerEntity> mOnlineListPendulum;
    private HashMap<String, PlayerEntity> mOnlineListDestiny;

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
                mOnlineListProphecy = mRepository.getOnline("prophecy");
                Log.d(TAG, "prophecy scraped");
                updateVipList(mOnlineListProphecy, "prophecy");
            }
        });
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mOnlineListLegacy = mRepository.getOnline("legacy");
                Log.d(TAG, "legacy scraped");
                updateVipList(mOnlineListLegacy, "legacy");
            }
        });
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mOnlineListPendulum = mRepository.getOnline("pendulum");
                Log.d(TAG, "pendulum scraped");
                updateVipList(mOnlineListPendulum, "pendulum");
            }
        });
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mOnlineListDestiny = mRepository.getOnline("destiny");
                Log.d(TAG, "destiny scraped");
                updateVipList(mOnlineListDestiny, "destiny");
            }
        });
    }

    private void updateVipList(HashMap<String, PlayerEntity> onlineList, String server) {
        for(PlayerEntity player : mVipList.getValue()) {
            if(!player.getServer().equals(server)) {
                continue;
            }
            String name = player.getName();
            if(onlineList.containsKey(name)) {
                mRepository.updatePlayer(onlineList.get(name));
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

    public void removePlayer(final PlayerEntity player) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mRepository.removePlayer(player);
            }
        });
    }
}
