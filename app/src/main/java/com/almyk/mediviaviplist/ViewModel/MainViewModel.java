package com.almyk.mediviaviplist.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.almyk.mediviaviplist.Database.Entities.PlayerEntity;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private static LiveData<List<PlayerEntity>> mOnlineLegacy;
    private static LiveData<List<PlayerEntity>> mOnlinePendulum;
    private static LiveData<List<PlayerEntity>> mOnlineDestiny;
    private static LiveData<List<PlayerEntity>> mOnlineProphecy;

    private DataRepository mRepository;


    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((MediviaVipListApp) application).getRepository();

        mOnlineLegacy = mRepository.getOnlineByServer("legacy");
        mOnlinePendulum = mRepository.getOnlineByServer("pendulum");
        mOnlineDestiny = mRepository.getOnlineByServer("destiny");
        mOnlineProphecy = mRepository.getOnlineByServer("prophecy");
    }

    public LiveData<List<PlayerEntity>> getOnlineLegacy() {
        return mOnlineLegacy;
    }

    public LiveData<List<PlayerEntity>> getOnlinePendulum() {
        return mOnlinePendulum;
    }

    public LiveData<List<PlayerEntity>> getOnlineDestiny() {
        return mOnlineDestiny;
    }

    public LiveData<List<PlayerEntity>> getOnlineProphecy() {
        return mOnlineProphecy;
    }
}
