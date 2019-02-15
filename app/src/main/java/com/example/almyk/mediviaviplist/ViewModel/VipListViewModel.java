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
    private static final String PROPHECY = "Prophecy";
    private static final String LEGACY = "Legacy";
    private static final String DESTINY = "Destiny";
    private static final String PENDULUM = "Pendulum";

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
                mOnlineListProphecy = mRepository.getOnline(PROPHECY);
                Log.d(TAG, "prophecy scraped");
                updateVipList(mOnlineListProphecy, PROPHECY);
            }
        });
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mOnlineListLegacy = mRepository.getOnline(LEGACY);
                Log.d(TAG, "legacy scraped");
                updateVipList(mOnlineListLegacy, LEGACY);
            }
        });
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mOnlineListPendulum = mRepository.getOnline(PENDULUM);
                Log.d(TAG, "pendulum scraped");
                updateVipList(mOnlineListPendulum, PENDULUM);
            }
        });
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mOnlineListDestiny = mRepository.getOnline(DESTINY);
                Log.d(TAG, "destiny scraped");
                updateVipList(mOnlineListDestiny, DESTINY);
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
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                final PlayerEntity player = mRepository.getPlayerWeb(name);
                if(player == null) {
                    return;
                }
                mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mRepository.addPlayer(player);
                    }
                });
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
