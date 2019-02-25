package com.almyk.mediviaviplist.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.almyk.mediviaviplist.Database.AppDatabase;
import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.Utilities.Constants;
import com.almyk.mediviaviplist.Utilities.NotificationUtils;
import com.almyk.mediviaviplist.Scraping.Scraper;
import com.almyk.mediviaviplist.Worker.UpdateAllPlayersWorker;
import com.almyk.mediviaviplist.Worker.UpdatePlayerWorker;
import com.almyk.mediviaviplist.Worker.UpdateVipListWorker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    private MutableLiveData<List<PlayerEntity>> mOnlineLegacy = new MutableLiveData<>();
    private MutableLiveData<List<PlayerEntity>> mOnlinePendulum = new MutableLiveData<>();
    private MutableLiveData<List<PlayerEntity>> mOnlineDestiny = new MutableLiveData<>();
    private MutableLiveData<List<PlayerEntity>> mOnlineProphecy = new MutableLiveData<>();

    // user preferences
    private long mSyncInterval;
    private boolean mDoBackgroundSync;
    private boolean mShowNotifications;
    private long mBackgroundSyncLastSleep;
    private long mBackgroundSyncLastCancel;

    private DataRepository(final AppDatabase database, Context context) {
        this.mDatabase = database;
        mVipList = mDatabase.playerDao().getAll();
        mScraper = new Scraper();
        this.mContext = context;
        this.mBackgroundSyncLastCancel = 0;
        this.mBackgroundSyncLastSleep = 0;
        initializeUserPreferences(context);
        setupWorkManager();
    }

    private void setupWorkManager() {
        mWorkManager = WorkManager.getInstance();
        if(mDoBackgroundSync) {
            initializeVipListBackgroundSync(ExistingWorkPolicy.REPLACE);
        } else {
            mWorkManager.cancelAllWork();
        }

    }

    private void initializeVipListBackgroundSync(ExistingWorkPolicy policy) {
        mWorkManager.cancelUniqueWork(Constants.UPDATE_VIP_LIST_UNIQUE_NAME);
        Data data = new Data.Builder().putBoolean(Constants.DO_BGSYNC, mDoBackgroundSync).build();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UpdateVipListWorker.class)
                .addTag(Constants.UPDATE_VIP_LIST_TAG)
                .setInputData(data)
                .build();
        mWorkManager.enqueueUniqueWork(Constants.UPDATE_VIP_LIST_UNIQUE_NAME, policy, workRequest);

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(UpdateAllPlayersWorker.class, 30, TimeUnit.MINUTES)
                .addTag(Constants.UPDATE_VIP_DETAIL_TAG)
                .build();
        mWorkManager.enqueueUniquePeriodicWork(Constants.UPDATE_VIP_DETAIL_UNIQUE_NAME, ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
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
        if(onlineList == null) {
            Log.e(TAG, "onlineList is null in updateDatabaseVipList()");
            return;
        }
        List<String> loginList = new ArrayList<>();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        for(PlayerEntity player : mVipList.getValue()) {
            if(!player.getServer().equals(server)) {
                continue;
            }
            String name = player.getName();
            boolean isMuted = preferences.getBoolean(name + "_muted", false);
            if(onlineList.containsKey(name)) { // player is online
                if(!getPlayerDB(name).isOnline() && !isMuted) { // player was offline
                    loginList.add(name);
                }
                PlayerEntity newData = onlineList.get(name);
                player.setLevel(newData.getLevel());
                player.setVocation(newData.getVocation());
                player.setOnline(newData.isOnline());
                updatePlayerDB(player);
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
                    mWorkManager.cancelUniqueWork(Constants.UPDATE_VIP_DETAIL_UNIQUE_NAME);
                    mBackgroundSyncLastCancel = new Date().getTime();
                    Toast.makeText(mContext, "Background sync turned off, to manually update pull down on vip list.", Toast.LENGTH_LONG).show();
                } else if( // if we have a sleeping thread we don't start a new one
                        // or when app was started and sync was off, but now turned on, then we need to start new thread
                        mBackgroundSyncLastCancel - mBackgroundSyncLastSleep > mSyncInterval || mBackgroundSyncLastCancel == 0) {
                        initializeVipListBackgroundSync(ExistingWorkPolicy.REPLACE);
                } else { // only launch UpdatePlayerWorker
                    PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(UpdateAllPlayersWorker.class, 30, TimeUnit.MINUTES)
                            .addTag(Constants.UPDATE_VIP_DETAIL_TAG)
                            .build();
                    mWorkManager.enqueueUniquePeriodicWork(Constants.UPDATE_VIP_DETAIL_UNIQUE_NAME, ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
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
                .build();
        mWorkManager.enqueue(workRequest);
    }

    public void updateVipAndOnlineList(String server) {
        HashMap<String, PlayerEntity> map = mScraper.scrapeOnline(server);
        if(map == null) {
            Log.e(TAG, "Scraper.scrapeOnline("+server+") returned null");
            return;
        }
        updateDatabaseVipList(map, server);
        updateOnlineList(map, server);
    }

    private void updateOnlineList(HashMap<String, PlayerEntity> map, String server) {
        Collection<PlayerEntity> players = map.values();
        List<PlayerEntity> onlineList = new ArrayList<>(players);
        Collections.sort(onlineList, new Comparator<PlayerEntity>() {
            @Override
            public int compare(PlayerEntity o1, PlayerEntity o2) {
                return Integer.parseInt(o2.getLevel()) - Integer.parseInt(o1.getLevel());
            }
        });

        switch(server.toLowerCase()) {
            case "legacy":
                mOnlineLegacy.postValue(onlineList);
                break;
            case "pendulum":
                mOnlinePendulum.postValue(onlineList);
                break;
            case "destiny":
                mOnlineDestiny.postValue(onlineList);
                break;
            case "prophecy":
                mOnlineProphecy.postValue(onlineList);
                break;
        }

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

    public void updatePlayer(String name) {
        Data data = new Data.Builder().putString(Constants.UPDATE_PLAYER_KEY, name).build();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UpdatePlayerWorker.class)
                .setInputData(data)
                .build();
        mWorkManager.enqueue(workRequest);
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

    public WorkManager getWorkManager() {
        return mWorkManager;
    }

    public void setBackgroundSyncLastSleep(long time) {
        this.mBackgroundSyncLastSleep = time;
    }

    public LiveData<List<PlayerEntity>> getOnlineByServer(String server) {
        switch(server.toLowerCase()) {
            case "legacy":
                return mOnlineLegacy;
            case "pendulum":
                return mOnlinePendulum;
            case "destiny":
                return mOnlineDestiny;
            case "prophecy":
                return mOnlineProphecy;
        }
        return null;
    }
}
