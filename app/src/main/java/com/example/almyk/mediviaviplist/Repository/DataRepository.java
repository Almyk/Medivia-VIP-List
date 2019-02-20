package com.example.almyk.mediviaviplist.Repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.almyk.mediviaviplist.Database.AppDatabase;
import com.example.almyk.mediviaviplist.Database.PlayerEntity;
import com.example.almyk.mediviaviplist.R;
import com.example.almyk.mediviaviplist.Utilities.NotificationUtils;
import com.example.almyk.mediviaviplist.Scraping.Scraper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataRepository implements SharedPreferences.OnSharedPreferenceChangeListener{
    private final static String TAG = DataRepository.class.getSimpleName();

    private static DataRepository sInstance;
    private Context mContext;

    private final AppDatabase mDatabase;
    private final Scraper mScraper;

    private LiveData<List<PlayerEntity>> mVipList;

    private long mSyncInterval;

    private DataRepository(final AppDatabase database, Context context) {
        this.mDatabase = database;
        mVipList = mDatabase.playerDao().getAll();
        mScraper = new Scraper();
        this.mContext = context;
        initializeSyncInterval(context);
    }

    private void initializeSyncInterval(Context context) {
        Long val = Long.parseLong(PreferenceManager.getDefaultSharedPreferences(context)
                        .getString("bgsync_freq", "60000"));
        setSyncInterval(val);
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
        PlayerEntity player = getPlayerWeb(name);
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
        List<String> loginList = new ArrayList<>();
        for(PlayerEntity player : mVipList.getValue()) {
            if(!player.getServer().equals(server)) {
                continue;
            }
            String name = player.getName();
            if(onlineList.containsKey(name)) { // player is online
                if(!getPlayerDB(name).isOnline()) { // player was offline
                    loginList.add(name);
                }
                updatePlayerDB(onlineList.get(name));
                Log.d(TAG, "Updated DB");
            } else {
                player.setOnline(false);
                updatePlayerDB(player);
            }
        }

        if(!loginList.isEmpty()) {
            String names = loginList.toString();
            names = names.replace('[', ' ');
            names = names.replace(']', ' ');
            NotificationUtils.makeStatusNotification("Player " + names + " has logged in.", mContext, server);
            Log.d(TAG, "Created notification for " + names);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch(key) {
            case "bgsync_freq":
                String val = sharedPreferences.getString(key, "60000");
                setSyncInterval(Long.parseLong(val));
        }
    }

    private void setSyncInterval(long timeInSeconds) {
        this.mSyncInterval = timeInSeconds * 1000;
    }

    public long getSyncInterval() {
        return mSyncInterval;
    }
}
