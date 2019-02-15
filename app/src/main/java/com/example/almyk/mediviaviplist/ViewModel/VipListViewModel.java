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

    public VipListViewModel(@NonNull Application application) {
        super(application);

        mRepository = ((MediviaVipListApp) application).getRepository();
        mExecutors = AppExecutors.getInstance();
    }

    public void init() {
        setupVipList();
        updateVipList();
    }

    private void setupVipList() {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mVipList = mRepository.getVipList();
            }
        });
    }

    public void updateVipList() {
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mRepository.updateVipList(PROPHECY);
            }
        });
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mRepository.updateVipList(LEGACY);
            }
        });
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mRepository.updateVipList(PENDULUM);
            }
        });
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mRepository.updateVipList(DESTINY);
            }
        });
    }

    public LiveData<List<PlayerEntity>> getVipList() {
        return mVipList;
    }

    public void addPlayer(final String name) {
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mRepository.addPlayer(name);
            }
        });

    }

    public void removePlayer(final PlayerEntity player) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mRepository.removePlayerDB(player);
            }
        });
    }
}
