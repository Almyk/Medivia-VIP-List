package com.almyk.mediviaviplist.Worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.almyk.mediviaviplist.Database.Entities.PlayerEntity;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;

import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateAllPlayersWorker extends Worker {
    private final static String TAG = UpdateAllPlayersWorker.class.getSimpleName();
    private DataRepository mRepository;

    public UpdateAllPlayersWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mRepository = ((MediviaVipListApp) getApplicationContext()).getRepository();
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            List<PlayerEntity> players = mRepository.getVipList().getValue();

            if (players != null) {
                for (PlayerEntity player : players) {
                    mRepository.updatePlayer(player.getName());
                }

                return Result.success();
            } else {
                return Result.failure();
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to update player due to exception: " + e.toString());
            return Result.failure();
        }
    }
}
