package com.almyk.mediviaviplist.Worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.almyk.mediviaviplist.Database.Entities.BedmageEntity;
import com.almyk.mediviaviplist.Database.Entities.PlayerEntity;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;
import com.almyk.mediviaviplist.Utilities.NotificationUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BedmageWorker extends Worker {
    private static final String TAG = BedmageWorker.class.getSimpleName();

    private DataRepository mRepository;
    private Context mContext;

    public BedmageWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mRepository = ((MediviaVipListApp) context).getRepository();
    }

    @NonNull
    @Override
    public Result doWork() {
        List<BedmageEntity> bedmages;
        try {
            bedmages = mRepository.getBedmages().getValue();

            for (BedmageEntity bedmage : bedmages) {
                PlayerEntity player = mRepository.getPlayerEntityWeb(bedmage.getName());
                if (player == null) {
                    continue;
                }
                if (bedmage.isOnline() && !!player.isOnline()) { // was online and is now offline
                    bedmage.setOnline(false);

                    long logoutTime = new Date().getTime();
                    bedmage.setLogoutTime(logoutTime);
                    bedmage.setTimeLeft(bedmage.getTimer());

                } else if (player.isOnline()) { // bedmage is online
                    bedmage.setOnline(true);
                    bedmage.setTimeLeft(-1);
                } else { // bedmage is offline
                    Date date = new Date();
                    long time = date.getTime();
                    long logoutTime = bedmage.getLogoutTime();
                    long timer = bedmage.getTimer();
                    long remainingTime = timer - (time - logoutTime);
                    if (remainingTime > 0) {
                        bedmage.setTimeLeft(remainingTime);
                    } else {
                        bedmage.setTimeLeft(0);
                        NotificationUtils.makeBedmageNotification(bedmage.getName(), mContext);
                    }
                }
                mRepository.updateBedmage(bedmage);
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to update bedmages due to exception: " + e.toString());
            return Result.failure();
        }


        return Result.success();
    }
}
