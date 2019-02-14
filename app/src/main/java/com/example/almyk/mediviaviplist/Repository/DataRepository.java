package com.example.almyk.mediviaviplist.Repository;

import android.arch.lifecycle.LiveData;

import com.example.almyk.mediviaviplist.Database.AppDatabase;
import com.example.almyk.mediviaviplist.Database.PlayerEntity;
import com.example.almyk.mediviaviplist.Scraping.Scraper;

import java.util.HashMap;
import java.util.List;

public class DataRepository {
    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private final Scraper mScraper;

    private LiveData<List<PlayerEntity>> mVipList;

    private DataRepository(final AppDatabase database) {
        this.mDatabase = database;
        mVipList = mDatabase.playerDao().getAll();
        mScraper = new Scraper();
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

    public HashMap<String, PlayerEntity> getOnline(String server) {
        return mScraper.scrapeOnline(server);
    }

    public PlayerEntity getPlayerWeb(String name) {
        return mScraper.scrapePlayer(name);
    }

    public PlayerEntity getPlayer(String name) {
        return mDatabase.playerDao().getPlayer(name);
    }

    public void addPlayer(PlayerEntity player) {
        mDatabase.playerDao().insert(player);
    }

    public void removePlayer(PlayerEntity player) {
        mDatabase.playerDao().delete(player);
    }

    public void updatePlayer(PlayerEntity player) {
        mDatabase.playerDao().update(player);
    }
}
