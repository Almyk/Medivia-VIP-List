package com.example.almyk.mediviaviplist.Repository;

import android.arch.lifecycle.LiveData;

import com.example.almyk.mediviaviplist.Database.AppDatabase;
import com.example.almyk.mediviaviplist.Database.PlayerEntity;

import java.util.List;

public class DataRepository {
    private static DataRepository sInstance;

    private final AppDatabase mDatabase;

    private LiveData<List<PlayerEntity>> mVipList;

    private DataRepository(final AppDatabase database) {
        this.mDatabase = database;
        mVipList = mDatabase.playerDao().getAll();
    }


    public static DataRepository getInstance(final AppDatabase database) {
        if(sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<PlayerEntity>> getVipList() {
        return mVipList;
    }
}
