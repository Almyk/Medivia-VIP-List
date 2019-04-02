package com.almyk.mediviaviplist.Worker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.almyk.mediviaviplist.Database.Entities.BedmageEntity;
import com.almyk.mediviaviplist.Database.Entities.PlayerEntity;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.Repository.DataRepository;
import com.almyk.mediviaviplist.Utilities.Constants;
import com.almyk.mediviaviplist.Utilities.NotificationUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BedmageWorker extends Worker {
    private static final String TAG = BedmageWorker.class.getSimpleName();

    private DataRepository mRepository;
    private Context mContext;

    public BedmageWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mRepository = ((MediviaVipListApp) getApplicationContext()).getRepository();
        this.mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            List<BedmageEntity> bedmages = mRepository.getBedmagesNotLive();
            if (bedmages != null && !bedmages.isEmpty()) {
                for (BedmageEntity bedmage : bedmages) {
                    PlayerEntity player = mRepository.getPlayerEntityWeb(bedmage.getName());
                    if (player == null) {
                        continue;
                    }
                    Log.d(TAG, "got bedmage " + player.getName());
                    if (!bedmage.getName().equals(player.getName())) {
                        mRepository.deleteBedmage(bedmage, 1);
                        bedmage.setName(player.getName());
                    }
                    if (bedmage.isOnline() && !player.isOnline()) { // was online and is now offline
                        bedmage.setOnline(false);
                        bedmage.setNotified(false);

                        Log.d(TAG, "bedmage logged out");
                        long logoutTime = new Date().getTime();
                        bedmage.setLogoutTime(logoutTime);
                        bedmage.setTimeLeft(bedmage.getTimer());


                    } else if (player.isOnline()) { // bedmage is online
                        Log.d(TAG, "bedmage is online");
                        bedmage.setOnline(true);
                        bedmage.setNotified(false);
                        bedmage.setTimeLeft(-1);
                    } else { // bedmage is offline
                        Log.d(TAG, "bedmage is offline");
                        Date date = new Date();
                        long time = date.getTime();

                        long theoLogoutTime = getTheoreticalLogoutTime(player, time);
                        long logoutTime = bedmage.getLogoutTime();

                        if (logoutTime < theoLogoutTime) { // logged in and out without worker noticed
                            logoutTime = theoLogoutTime;
                            bedmage.setLogoutTime(logoutTime);
                        }

                        long timer = bedmage.getTimer();
                        long remainingTime = timer - (time - logoutTime);
                        if (remainingTime > 0) {
                            bedmage.setTimeLeft(remainingTime);
                            bedmage.setNotified(false);
                        } else {
                            bedmage.setTimeLeft(0);
                            if (!bedmage.isNotified()) {
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                                boolean isMuted = preferences.getBoolean(Constants.BEDMAGE_ISMUTED_PREF, false);
                                if (!isMuted) {
                                    Log.d(TAG, "create notification for bedmage");
                                    NotificationUtils.makeBedmageNotification(bedmage.getName(), mContext);
                                }
                                bedmage.setNotified(true);
                            }
                        }
                    }
                    mRepository.addBedmage(bedmage);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to update bedmages due to exception: " + e.toString());
        }
        rescheduleBedmageWorker();
        return Result.success();
    }

    private long getTheoreticalLogoutTime(PlayerEntity player, long time) {
        String[] lastLogin = player.getLastLogin().split(" ");
        long theoLogoutTime;
        switch (lastLogin[1]) {
            case "minutes":
            case "minute":
                theoLogoutTime = time - TimeUnit.MINUTES.toMillis(Long.parseLong(lastLogin[0]));
                break;
            case "hours":
            case "hour":
                theoLogoutTime = time - TimeUnit.HOURS.toMillis(Long.parseLong(lastLogin[0]));
                break;
            case "seconds":
            case "second":
                theoLogoutTime = time - TimeUnit.SECONDS.toMillis(Long.parseLong(lastLogin[0]));
                break;
            default:
                theoLogoutTime = -1;
                break;
        }
        Log.d(TAG, "Last Login: #" + lastLogin[0] + " unit: " + lastLogin[1]);
        return theoLogoutTime;
    }

    private void rescheduleBedmageWorker() {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(BedmageWorker.class)
                .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .setInitialDelay(60, TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance().enqueueUniqueWork(Constants.BEDMAGE_UNIQUE_NAME, ExistingWorkPolicy.APPEND, workRequest);
        Log.d(TAG, "Scheduled new bedmage worker");
    }
}
