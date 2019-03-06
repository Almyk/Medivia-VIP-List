package com.almyk.mediviaviplist.Worker;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;

import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateAllPlayersWorker extends Worker {
    private DataRepository mRepository;

    public UpdateAllPlayersWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mRepository = ((MediviaVipListApp) getApplicationContext()).getRepository();
    }

    @NonNull
    @Override
    public Result doWork() {
        List<PlayerEntity> players = mRepository.getVipList().getValue();

        if(players != null) {
            for (PlayerEntity player : players) {
                mRepository.updatePlayer(player.getName());
            }

            return Result.success();
        } else {
            return Result.failure();
        }
    }
}
