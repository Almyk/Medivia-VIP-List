package com.almyk.mediviaviplist.Worker;

import android.content.Context;
import android.support.annotation.NonNull;

import com.almyk.mediviaviplist.Database.HighscoreEntity;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;
import com.almyk.mediviaviplist.Utilities.Constants;

import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateHighscoreWorker extends Worker {
    private Context mContext;
    private DataRepository mRepository;

    public UpdateHighscoreWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
        this.mRepository = ((MediviaVipListApp) context).getRepository();
    }

    @NonNull
    @Override
    public Result doWork() {
        String server = getInputData().getString(Constants.UPDATE_HIGHSCORES_SERVER_KEY);
        String skill = getInputData().getString(Constants.UPDATE_HIGHSCORES_SKILL_KEY);
        List<HighscoreEntity> highscores = mRepository.scrapeHighscoreByServerAndSkill(server, skill);

        if(highscores != null && !highscores.isEmpty()) {
            for(HighscoreEntity highscore : highscores) {
                mRepository.uppdateHighscoreDB(highscore);
            }
            mRepository.setHighScores();
            return Result.success();
        }
        return Result.failure();
    }
}
