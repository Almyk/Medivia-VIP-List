package com.example.almyk.mediviaviplist.Repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.almyk.mediviaviplist.Database.AppDatabase;
import com.example.almyk.mediviaviplist.Database.PlayerEntity;
import com.example.almyk.mediviaviplist.Utilities.Constants;
import com.example.almyk.mediviaviplist.Utilities.NotificationUtils;
import com.example.almyk.mediviaviplist.Scraping.Scraper;
import com.example.almyk.mediviaviplist.Worker.UpdateVipListWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class DataRepository implements SharedPreferences.OnSharedPreferenceChangeListener{
    private final static String TAG = DataRepository.class.getSimpleName();

    private static DataRepository sInstance;
    private Context mContext;

    private final AppDatabase mDatabase;
    private final Scraper mScraper;
    private static WorkManager mWorkManager;

    private final LiveData<List<PlayerEntity>> mVipList;

    // user preferences
    private long mSyncInterval;
    private boolean mDoBackgroundSync;
    private boolean mShowNotifications;

    private DataRepository(final AppDatabase database, Context context) {
        this.mDatabase = database;
        mVipList = mDatabase.playerDao().getAll();
        mScraper = new Scraper();
        this.mContext = context;
        initializeUserPreferences(context);
        setupWorkManager();
    }

    private void setupWorkManager() {
        mWorkManager = WorkManager.getInstance();
        if(mDoBackgroundSync) {
            initializeVipListBackgroundSync();
        } else {
            mWorkManager.cancelAllWork();
        }

    }

    private void initializeVipListBackgroundSync() {
        mWorkManager.cancelAllWork();
        Data data = new Data.Builder().putBoolean(Constants.DO_BGSYNC, mDoBackgroundSync).build();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UpdateVipListWorker.class)
                .addTag(Constants.UPDATE_VIP_LIST_TAG)
                .setInputData(data)
                .build();
        mWorkManager.enqueueUniqueWork(Constants.UPDATE_VIP_LIST_UNIQUE_NAME, ExistingWorkPolicy.REPLACE, workRequest);
    }

    private void initializeUserPreferences(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        // Because user sets interval in seconds, we use seconds as a unit here, which changes to millis in setSyncInterval()
        Long syncInterval = Long.parseLong(preferences.getString("bgsync_freq", "60"));
        if(syncInterval == null) {
            syncInterval = new Long(60);
        }
        setSyncInterval(syncInterval);
        mDoBackgroundSync = preferences.getBoolean("bgsync_switch", true);
        mShowNotifications = preferences.getBoolean("notification_switch", true);
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

        if(mShowNotifications) {
            createNotification(server, loginList);
        }
    }

    private void createNotification(String server, List<String> loginList) {
        if(!loginList.isEmpty()) {
            String names = loginList.toString();
            names = names.replace('[', ' ');
            names = names.replace(']', ' ');
            NotificationUtils.makeStatusNotification("Player " + names + " has logged in.", mContext, server);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch(key) {
            case "bgsync_freq":
                String val = sharedPreferences.getString(key, "60");
                setSyncInterval(Long.parseLong(val));
                break;
            case "bgsync_switch":
                mDoBackgroundSync = sharedPreferences.getBoolean(key, true);
                if(!mDoBackgroundSync) {
                    mWorkManager.cancelUniqueWork(Constants.UPDATE_VIP_LIST_UNIQUE_NAME);
                    Toast.makeText(mContext, "Background sync turned off, to manually update pull down on vip list.", Toast.LENGTH_LONG).show();
                } else {
                    initializeVipListBackgroundSync();
                }
                break;
            case "notification_switch":
                mShowNotifications = sharedPreferences.getBoolean(key, true);
                break;
        }
    }

    private void setSyncInterval(long timeInSeconds) {
        this.mSyncInterval = timeInSeconds * 1000;
    }

    public void forceUpdateVipList() {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UpdateVipListWorker.class)
                .addTag(Constants.UPDATE_VIP_LIST_TAG)
                .build();
        mWorkManager.enqueue(workRequest);
    }

    public void updateVipList(String server) {
        HashMap<String, PlayerEntity> map = mScraper.scrapeOnline(server);
        updateDatabaseVipList(map, server);
    }

    public LiveData<List<PlayerEntity>> getVipList() {
        return mVipList;
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

    public long getSyncInterval() {
        return mSyncInterval;
    }

    public boolean isDoBackgroundSync() {
        return mDoBackgroundSync;
    }
}
