package com.almyk.mediviaviplist.Worker;

import android.content.Context;
import android.support.annotation.NonNull;

import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;
import com.almyk.mediviaviplist.Utilities.Constants;

import java.util.Arrays;
import java.util.List;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateHighscoreByServerWorker extends Worker {
    private final List<String> skills = Arrays.asList("level", "maglevel", "fist", "club", "sword", "axe", "distance", "shielding", "fishing", "mining");

    public UpdateHighscoreByServerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String server = getInputData().getString(Constants.UPDATE_HIGHSCORES_SERVER_KEY);
        for(String skill : skills) {
            Data data = new Data.Builder()
                    .putString(Constants.UPDATE_HIGHSCORES_SERVER_KEY, server)
                    .putString(Constants.UPDATE_HIGHSCORES_SKILL_KEY, skill)
                    .build();
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UpdateHighscoreWorker.class)
                    .setInputData(data)
                    .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                    .build();
            WorkManager.getInstance().enqueueUniqueWork(Constants.UPDATE_HIGHSCORE_FOR + server + " " + skill, ExistingWorkPolicy.REPLACE,workRequest);
        }
        return Result.success();
    }
}
