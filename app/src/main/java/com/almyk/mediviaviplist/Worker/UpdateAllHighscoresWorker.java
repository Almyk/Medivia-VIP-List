package com.almyk.mediviaviplist.Worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;
import com.almyk.mediviaviplist.Utilities.Constants;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateAllHighscoresWorker extends Worker {
    private static final String TAG = UpdateAllHighscoresWorker.class.getSimpleName();
    private DataRepository mRepository;

    public UpdateAllHighscoresWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mRepository = ((MediviaVipListApp) context).getRepository();
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            for (String server : Constants.SERVERS) {
                mRepository.updateHighscoreByServer(server);
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to update highscores due to exception: " + e.toString());
        }
        return Result.success();
    }
}
