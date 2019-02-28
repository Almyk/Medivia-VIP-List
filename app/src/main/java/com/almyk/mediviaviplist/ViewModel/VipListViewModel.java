package com.almyk.mediviaviplist.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.almyk.mediviaviplist.Utilities.AppExecutors;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.Repository.DataRepository;

import java.util.List;


public class VipListViewModel extends AndroidViewModel {
    private static final String TAG = VipListViewModel.class.getSimpleName();

    private DataRepository mRepository;
    private static AppExecutors mExecutors;

    private LiveData<List<PlayerEntity>> mVipList;

    public VipListViewModel(@NonNull Application application) {
        super(application);

        mRepository = ((MediviaVipListApp) application).getRepository();
        mExecutors = AppExecutors.getInstance();
        PreferenceManager.getDefaultSharedPreferences(application).registerOnSharedPreferenceChangeListener(mRepository);
    }

    public void init() {
        setupVipList();
    }

    private void setupVipList() {
        mVipList = mRepository.getVipList();
    }

    public void updateVipList() {
        Toast.makeText(getApplication(), "Update all players data\n(Levels, name, guild, house etc)", Toast.LENGTH_LONG).show();
        for(PlayerEntity player : mVipList.getValue()) {
            mRepository.updatePlayer(player.getName());
        }
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

    public void addPlayerEntity(final PlayerEntity player) {
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mRepository.addPlayerDB(player);
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
        mRepository.forceUpdateVipList();
    }
}
