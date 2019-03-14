package com.almyk.mediviaviplist.Worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;
import com.almyk.mediviaviplist.Utilities.Constants;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdatePlayerWorker extends Worker {
    private Context mContext;
    private DataRepository mRepository;

    public UpdatePlayerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
        this.mRepository = ((MediviaVipListApp) getApplicationContext()).getRepository();
    }

    @NonNull
    @Override
    public Result doWork() {
        String name = getInputData().getString(Constants.UPDATE_PLAYER_KEY);
        PlayerEntity player = mRepository.getPlayerEntityWeb(name);
        if(player != null) {
            PlayerEntity oldPlayer = mRepository.getPlayerDB(name);
            player.setNote(oldPlayer.getNote());
            mRepository.updatePlayerDB(player);
            return Result.success();
        }

        return Result.failure();
    }
}
