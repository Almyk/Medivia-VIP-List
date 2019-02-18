package com.example.almyk.mediviaviplist.Repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.almyk.mediviaviplist.Database.AppDatabase;
import com.example.almyk.mediviaviplist.Database.PlayerEntity;
import com.example.almyk.mediviaviplist.NotificationUitls;
import com.example.almyk.mediviaviplist.Scraping.Scraper;

import java.util.HashMap;
import java.util.List;

public class DataRepository {
    private static DataRepository sInstance;
    private Context mContext;

    private final AppDatabase mDatabase;
    private final Scraper mScraper;

    private LiveData<List<PlayerEntity>> mVipList;

    private DataRepository(final AppDatabase database, Context context) {
        this.mDatabase = database;
        mVipList = mDatabase.playerDao().getAll();
        mScraper = new Scraper();
        this.mContext = context;
    }


    public static DataRepository getInstance(final AppDatabase database, Context context) {
        if(sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database, context);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<PlayerEntity>> getVipList() {
        return mVipList;
    }

    public void updateVipList(String server) {
        HashMap<String, PlayerEntity> map = mScraper.scrapeOnline(server);
        updateDatabaseVipList(map, server);
    }

    public PlayerEntity getPlayerWeb(String name) {
        return mScraper.scrapePlayer(name);
    }

    public void addPlayer(String name) {
        PlayerEntity player = mScraper.scrapePlayer(name);
        if(player != null) {
            addPlayerDB(player);
        }
    }

    public PlayerEntity getPlayerDB(String name) {
        return mDatabase.playerDao().getPlayer(name);
    }

    public void addPlayerDB(PlayerEntity player) {
        mDatabase.playerDao().insert(player);
    }

    public void removePlayerDB(PlayerEntity player) {
        mDatabase.playerDao().delete(player);
    }

    public void updatePlayerDB(PlayerEntity player) {
        mDatabase.playerDao().update(player);
    }

    private void updateDatabaseVipList(HashMap<String, PlayerEntity> onlineList, String server) {
        for(PlayerEntity player : mVipList.getValue()) {
            if(!player.getServer().equals(server)) {
                continue;
            }
            String name = player.getName();
            if(onlineList.containsKey(name)) {
                if(!player.isOnline()) { // player logged in
                    NotificationUitls.makeStatusNotification("Player " + name + " has logged in.", mContext);
                }
                updatePlayerDB(onlineList.get(name));
            } else {
                player.setOnline(false);
                updatePlayerDB(player);
            }
        }
    }
}
