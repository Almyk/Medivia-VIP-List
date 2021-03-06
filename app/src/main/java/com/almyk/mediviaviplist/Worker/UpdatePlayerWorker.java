package com.almyk.mediviaviplist.Worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.almyk.mediviaviplist.Database.Entities.PlayerEntity;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;
import com.almyk.mediviaviplist.Utilities.Constants;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdatePlayerWorker extends Worker {
    private final static String TAG = UpdatePlayerWorker.class.getSimpleName();
    private DataRepository mRepository;

    public UpdatePlayerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mRepository = ((MediviaVipListApp) getApplicationContext()).getRepository();
    }

    @NonNull
    @Override
    public Result doWork() {
        String name = getInputData().getString(Constants.UPDATE_PLAYER_KEY);
        try {
            PlayerEntity player = mRepository.getPlayerEntityWeb(name);
            if (player != null) {
                PlayerEntity oldPlayer = mRepository.getPlayerDB(name);
                player.setNote(oldPlayer.getNote());
                mRepository.updatePlayerDB(player);
                Log.d(TAG, "Updated player: " + player.getName());
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to update player due to exception: " + e.toString());
        }

        return Result.success();
    }
}
