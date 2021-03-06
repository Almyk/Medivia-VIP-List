package com.almyk.mediviaviplist.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.almyk.mediviaviplist.Database.AppDatabase;
import com.almyk.mediviaviplist.Database.Entities.BedmageEntity;
import com.almyk.mediviaviplist.Database.Entities.DeathEntity;
import com.almyk.mediviaviplist.Database.Entities.HighscoreEntity;
import com.almyk.mediviaviplist.Database.Entities.KillEntity;
import com.almyk.mediviaviplist.Database.Entities.LevelProgressionEntity;
import com.almyk.mediviaviplist.Database.Entities.PlayerEntity;
import com.almyk.mediviaviplist.Database.Entities.TaskEntity;
import com.almyk.mediviaviplist.Model.Player;
import com.almyk.mediviaviplist.Utilities.AppExecutors;
import com.almyk.mediviaviplist.Utilities.Constants;
import com.almyk.mediviaviplist.Utilities.NotificationUtils;
import com.almyk.mediviaviplist.Scraping.Scraper;
import com.almyk.mediviaviplist.Worker.BedmageWorker;
import com.almyk.mediviaviplist.Worker.LaunchPeriodicWorkWorker;
import com.almyk.mediviaviplist.Worker.UpdateAllHighscoresWorker;
import com.almyk.mediviaviplist.Worker.UpdateAllPlayersWorker;
import com.almyk.mediviaviplist.Worker.UpdateHighscoreByServerWorker;
import com.almyk.mediviaviplist.Worker.UpdatePlayerWorker;
import com.almyk.mediviaviplist.Worker.UpdateVipListWorker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class DataRepository implements SharedPreferences.OnSharedPreferenceChangeListener{
    private final static String TAG = DataRepository.class.getSimpleName();

    private static DataRepository sInstance;
    private Context mContext;

    private final AppDatabase mDatabase;
    private final Scraper mScraper;
    private static AppExecutors mExecutors;
    private static WorkManager mWorkManager;

    private final LiveData<List<PlayerEntity>> mVipList;
    private final LiveData<List<BedmageEntity>> mBedmageList;
    private static MutableLiveData<List<PlayerEntity>> mOnlineLegacy = new MutableLiveData<>();
    private static MutableLiveData<List<PlayerEntity>> mOnlinePendulum = new MutableLiveData<>();
    private static MutableLiveData<List<PlayerEntity>> mOnlineDestiny = new MutableLiveData<>();
    private static MutableLiveData<List<PlayerEntity>> mOnlineProphecy = new MutableLiveData<>();
    private static MutableLiveData<List<PlayerEntity>> mOnlineUnity = new MutableLiveData<>();
    private static MutableLiveData<List<PlayerEntity>> mOnlinePurity = new MutableLiveData<>();
    private static MutableLiveData<List<HighscoreEntity>> mHighscores = new MutableLiveData<>();

    private String mHighscoreServer;
    private String mHighscoreVoc;
    private String mHighscoreSkill;

    private MutableLiveData<Player> mSearchCharacter = new MutableLiveData<>();

    // user preferences
    private long mSyncInterval;
    private boolean mDoBackgroundSync;
    private boolean mShowNotifications;

    private DataRepository(final AppDatabase database, Context context) {
        this.mDatabase = database;

//         This code is left here for debugging purposes
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                mDatabase.deathDao().nukeTable();
//                mDatabase.killDao().nukeTable();
//                mDatabase.taskDao().nukeTable();
//                mDatabase.bedmageDao().nukeTable();
//            }
//        }).start();

        mVipList = mDatabase.playerDao().getAll();
        mBedmageList = mDatabase.bedmageDao().getAll();
        mScraper = new Scraper();
        mExecutors = AppExecutors.getInstance();
        this.mContext = context;

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
        initializeBedmageWorker();
    }

    private void initializeBedmageWorker() {
//        mWorkManager.cancelUniqueWork(Constants.BEDMAGE_UNIQUE_NAME);
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(BedmageWorker.class)
                .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build();
        mWorkManager.enqueueUniqueWork(Constants.BEDMAGE_UNIQUE_NAME, ExistingWorkPolicy.REPLACE, workRequest);
    }

    private void initializeVipListBackgroundSync(ExistingWorkPolicy vipListPolicy) {
        mWorkManager.cancelUniqueWork(Constants.UPDATE_VIP_LIST_UNIQUE_NAME);
        Data data = new Data.Builder().putBoolean(Constants.DO_BGSYNC, mDoBackgroundSync).build();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UpdateVipListWorker.class)
                .addTag(Constants.UPDATE_VIP_LIST_TAG)
                .setInputData(data)
                .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build();
        mWorkManager.enqueueUniqueWork(Constants.UPDATE_VIP_LIST_UNIQUE_NAME, vipListPolicy, workRequest);

        OneTimeWorkRequest launchPeriodWorkers = new OneTimeWorkRequest.Builder(LaunchPeriodicWorkWorker.class)
                .setInitialDelay(20, TimeUnit.SECONDS)
                .build();
        mWorkManager.enqueue(launchPeriodWorkers);
    }

    public void startPeriodicWorkers() {
        PeriodicWorkRequest updateAllPlayersWork = new PeriodicWorkRequest.Builder(UpdateAllPlayersWorker.class, 15, TimeUnit.MINUTES)
                .addTag(Constants.UPDATE_VIP_DETAIL_TAG)
                .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build();
        mWorkManager.enqueueUniquePeriodicWork(Constants.UPDATE_VIP_DETAIL_UNIQUE_NAME, ExistingPeriodicWorkPolicy.REPLACE, updateAllPlayersWork);

//        mWorkManager.cancelAllWorkByTag(Constants.UPDATE_HIGHSCORES_TAG);
        PeriodicWorkRequest updateAllHighscoresWork = new PeriodicWorkRequest.Builder(UpdateAllHighscoresWorker.class, 2, TimeUnit.HOURS)
                .addTag(Constants.UPDATE_HIGHSCORES_TAG)
                .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).setRequiresBatteryNotLow(true).build())
                .build();
        mWorkManager.enqueueUniquePeriodicWork(Constants.UPDATE_HIGHSCORES_UNIQUE_NAME, ExistingPeriodicWorkPolicy.KEEP, updateAllHighscoresWork);
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
                player.setLastLogin(newData.getLastLogin());
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
            NotificationUtils.makeStatusNotification(
                    "Player " + names + " has logged in.", mContext, server);
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
                    mWorkManager.cancelAllWork();
                    Toast.makeText(
                            mContext,
                            "Background sync turned off, to manually update pull down on vip list.",
                            Toast.LENGTH_LONG).show();
                } else { // launch UpdatePlayerWorker and UpdateHighscoreWorker
                    initializeVipListBackgroundSync(ExistingWorkPolicy.REPLACE);
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
        Data data = new Data.Builder().putBoolean(Constants.DO_BGSYNC, false).build();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UpdateVipListWorker.class)
                .setInputData(data)
                .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
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
            // TODO: add strife
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
            case "unity":
                mOnlineUnity.postValue(onlineList);
                break;
            case "purity":
                mOnlinePurity.postValue(onlineList);
                break;
        }

    }

    public LiveData<List<PlayerEntity>> getVipList() {
        return mVipList;
    }

    public PlayerEntity getPlayerEntityWeb(String name) {
        return mScraper.scrapePlayerEntity(name);
    }

    public Player getPlayerWeb(String name) {
        return mScraper.scrapePlayer(name);
    }

    public void addPlayer(final String name) {
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                final PlayerEntity player = getPlayerEntityWeb(name);
                if(player != null) {
                    addPlayerDB(player);
                }
            }
        });
    }

    public void updatePlayer(String name) {
        Data data = new Data.Builder().putString(Constants.UPDATE_PLAYER_KEY, name).build();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UpdatePlayerWorker.class)
                .setInputData(data)
                .build();
        mWorkManager.enqueue(workRequest);
        Log.d(TAG, "Scheduled to update player: " + name);
    }

    public void updateHighscoreByServer(final String server) {
        Data data = new Data.Builder().putString(Constants.UPDATE_HIGHSCORES_SERVER_KEY, server).build();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UpdateHighscoreByServerWorker.class)
                .addTag(Constants.UPDATE_HIGHSCORES_TAG)
                .setInputData(data)
                .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build();
        mWorkManager.enqueueUniqueWork(Constants.UPDATE_HIGHSCORE_FOR + server, ExistingWorkPolicy.REPLACE,workRequest);
    }

    public void uppdateHighscoreDB(HighscoreEntity highscore) {
        mDatabase.highscoreDao().insert(highscore);
    }

    public List<HighscoreEntity> getHighscoreWeb(String server) {
        return mScraper.scrapeHighscore(server);
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

    public void updatePlayerDB(final PlayerEntity player) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                updateLevelProgression(player);
                String lvProg = getLevelProgression(player.getName());
                player.setLevelProgression(lvProg);
                mDatabase.playerDao().update(player);
            }
        });
    }

    private String getLevelProgression(String name) {
        String lvProg;
        int currLv = 0;
        int oldLv = 0;

        LevelProgressionEntity progression = mDatabase.progDao().getPlayerProgressionRow(name);


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        switch (calendar.get(calendar.DAY_OF_WEEK)) {
            case 1:
                currLv = Integer.parseInt(progression.getOne());
                oldLv = Integer.parseInt(progression.getTwo());
                break;
            case 2:
                currLv = Integer.parseInt(progression.getTwo());
                oldLv = Integer.parseInt(progression.getThree());
                break;
            case 3:
                currLv = Integer.parseInt(progression.getThree());
                oldLv = Integer.parseInt(progression.getFour());
                break;
            case 4:
                currLv = Integer.parseInt(progression.getFour());
                oldLv = Integer.parseInt(progression.getFive());
                break;
            case 5:
                currLv = Integer.parseInt(progression.getFive());
                oldLv = Integer.parseInt(progression.getSix());
                break;
            case 6:
                currLv = Integer.parseInt(progression.getSix());
                oldLv = Integer.parseInt(progression.getSeven());
                break;
            case 7:
                currLv = Integer.parseInt(progression.getSeven());
                oldLv = Integer.parseInt(progression.getOne());
                break;
        }

        lvProg = Integer.toString((currLv - oldLv));

        return lvProg;
    }

    private void updateLevelProgression(PlayerEntity player) {
        String level = player.getLevel();
        String name = player.getName();
        LevelProgressionEntity lvProg = mDatabase.progDao().getPlayerProgressionRow(name);

        if(lvProg == null) {
            lvProg = new LevelProgressionEntity(name, level, level, level, level, level, level, level);
            mDatabase.progDao().insert(lvProg);
            Log.d(TAG, "New progression insert for " + name);
        } else {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());

            switch (calendar.get(calendar.DAY_OF_WEEK)) {
                case 1:
                    lvProg.setOne(level);
                    break;
                case 2:
                    lvProg.setTwo(level);
                    break;
                case 3:
                    lvProg.setThree(level);
                    break;
                case 4:
                    lvProg.setFour(level);
                    break;
                case 5:
                    lvProg.setFive(level);
                    break;
                case 6:
                    lvProg.setSix(level);
                    break;
                case 7:
                    lvProg.setSeven(level);
                    break;
            }

            mDatabase.progDao().update(lvProg);
        }
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

    public LiveData<List<PlayerEntity>> getOnlineByServer(String server) {
        switch(server.toLowerCase()) {
            // TODO: add strife
            case "legacy":
                return mOnlineLegacy;
            case "pendulum":
                return mOnlinePendulum;
            case "destiny":
                return mOnlineDestiny;
            case "prophecy":
                return mOnlineProphecy;
            case "unity":
                return mOnlineUnity;
            case "purity":
                return mOnlinePurity;
        }
        return null;
    }

    public LiveData<List<HighscoreEntity>> getHighscores(final String server, final String vocation, final String skill) {
        mHighscoreServer = server;
        mHighscoreVoc = vocation;
        mHighscoreSkill = skill;
        getHighscoresDB();
        return mHighscores;
    }

    public void getHighscoresDB() {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                setHighScores();
            }
        });
    }

    public void setHighScores() {
        mHighscores.postValue(mDatabase.highscoreDao().getServerBySkillAndVoc(mHighscoreServer, mHighscoreSkill, mHighscoreVoc));
    }

    public List<HighscoreEntity> scrapeHighscoreByServerAndSkill(String server, String skill) {
        return mScraper.scrapeHighscoreByServerAndSkill(server, skill);
    }

    public void searchPlayer(final String name) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Player player = getPlayerWeb(name);
                if(player == null) {
                    player = new Player();
                    player.setPlayerEntity(getPlayerDB(name));
                }
                if(player.getPlayerEntity() != null) {
                    List<HighscoreEntity> highscoresAll = mDatabase.highscoreDao()
                            .getEntryByNameVoc(player.getPlayerName(), "all");

                    player.setHighscoresAll(highscoresAll);

                    List<HighscoreEntity> highscoresVoc = mDatabase.highscoreDao()
                            .getEntryByNameNotVoc(player.getPlayerName(), "all");

                    player.setHighscoresVoc(highscoresVoc);

                    mSearchCharacter.postValue(player);
                } else {
                    mSearchCharacter.postValue(null);
                }
            }
        });
    }

    public LiveData<Player> getSearchCharacterLiveData() {
        return mSearchCharacter;
    }

    private List<DeathEntity> getDeathsDB(String playerId) {
        return mDatabase.deathDao().getDeaths(playerId);
    }

    private void setDeathsDB(List<DeathEntity> deaths){
        for(DeathEntity death: deaths) {
            mDatabase.deathDao().insertDeath(death);
        }
    }

    private List<KillEntity> getKillsDB(String playerId) {
        return mDatabase.killDao().getKills(playerId);
    }

    private void setKillsDB(List<KillEntity> kills){
        for(KillEntity kill: kills) {
            mDatabase.killDao().insertKill(kill);
        }
    }

    private List<TaskEntity> getTasksDB(String playerId) {
        return mDatabase.taskDao().getTasks(playerId);
    }

    private void setTasksDB(List<TaskEntity> tasks){
        for(TaskEntity task: tasks) {
            mDatabase.taskDao().insertTask(task);
        }
    }

    public LiveData<List<BedmageEntity>> getBedmages() {
        return mBedmageList;
    }

    public void addBedmage(final BedmageEntity bedmage) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.bedmageDao().insert(bedmage);
            }
        });
    }

    public void updateBedmage(final BedmageEntity bedmage) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDatabase.bedmageDao().update(bedmage);
            }
        });
    }

    public List<BedmageEntity> getBedmagesNotLive() {
        return mDatabase.bedmageDao().getAllNotLive();
    }

    /**
     *
     * @param bedmage
     * @param flag set as 0 for main thread callers or 1 for background worker callers
     */
    public void deleteBedmage(final BedmageEntity bedmage, int flag) {
        switch (flag) {
            case 0:
                mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDatabase.bedmageDao().delete(bedmage);
                    }
                });
                break;
            case 1:
                mDatabase.bedmageDao().delete(bedmage);
                break;
        }
    }
}
