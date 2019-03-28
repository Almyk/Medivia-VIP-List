package com.almyk.mediviaviplist.Worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.almyk.mediviaviplist.Database.Entities.BedmageEntity;
import com.almyk.mediviaviplist.Database.Entities.PlayerEntity;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;

import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BedmageWorker extends Worker {
    private static final String TAG = BedmageWorker.class.getSimpleName();

    private DataRepository mRepository;

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
                // TODO if player was online but is now offline, update logout time!



                long logoutTime = 0; // TODO
                bedmage.setLogoutTime(logoutTime);
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to update bedmages due to exception: " + e.toString());
            return Result.failure();
        }


        return Result.success();
    }
}
