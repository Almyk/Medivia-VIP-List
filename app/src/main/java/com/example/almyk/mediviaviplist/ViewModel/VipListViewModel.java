package com.example.almyk.mediviaviplist.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.almyk.mediviaviplist.Database.AppDatabase;
import com.example.almyk.mediviaviplist.Database.PlayerEntity;
import com.example.almyk.mediviaviplist.Repository.DataRepository;

import java.util.List;

public class VipListViewModel extends AndroidViewModel {
    private DataRepository mRepository;

    private LiveData<List<PlayerEntity>> mVipList;

    public VipListViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        mRepository = DataRepository.getInstance(database);

        mVipList = mRepository.getVipList();
    }

    public LiveData<List<PlayerEntity>> getVipList() {
        return mVipList;
    }
}
