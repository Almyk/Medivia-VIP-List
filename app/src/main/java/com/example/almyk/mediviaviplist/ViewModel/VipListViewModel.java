package com.example.almyk.mediviaviplist.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.example.almyk.mediviaviplist.R;
import com.example.almyk.mediviaviplist.Utilities.AppExecutors;
import com.example.almyk.mediviaviplist.Utilities.Constants;
import com.example.almyk.mediviaviplist.MediviaVipListApp;
import com.example.almyk.mediviaviplist.Database.PlayerEntity;
import com.example.almyk.mediviaviplist.Repository.DataRepository;
import com.example.almyk.mediviaviplist.Worker.UpdateVipListWorker;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


public class VipListViewModel extends AndroidViewModel {
    private static final String TAG = VipListViewModel.class.getSimpleName();
    private static final String PROPHECY = "Prophecy";
    private static final String LEGACY = "Legacy";
    private static final String DESTINY = "Destiny";
    private static final String PENDULUM = "Pendulum";

    private static DataRepository mRepository;
    private static AppExecutors mExecutors;
    private static WorkManager mWorkManager;

    private LiveData<List<PlayerEntity>> mVipList;

    public VipListViewModel(@NonNull Application application) {
        super(application);

        mRepository = ((MediviaVipListApp) application).getRepository();
        mExecutors = AppExecutors.getInstance();
        mWorkManager = WorkManager.getInstance();
        mWorkManager.cancelAllWork();
        PreferenceManager.getDefaultSharedPreferences(application).registerOnSharedPreferenceChangeListener(mRepository);
    }

    public void init() {
        setupVipList();
        synchVipList();
    }

    private void synchVipList() {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                UpdateVipListWorker.class, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .addTag(Constants.UPDATE_VIP_LIST_TAG)
                .build();
        mWorkManager.enqueueUniquePeriodicWork(Constants.UPDATE_VIP_LIST_UNIQUE_NAME, ExistingPeriodicWorkPolicy.REPLACE,workRequest);
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

    public void forceUpdateVipList() {
        synchVipList();
    }
}
