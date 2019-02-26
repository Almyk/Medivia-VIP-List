package com.almyk.mediviaviplist.Worker;

import android.content.Context;
import android.support.annotation.NonNull;

import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;
import com.almyk.mediviaviplist.Utilities.Constants;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateAllHighscoresWorker extends Worker {
    private DataRepository mRepository;

    public UpdateAllHighscoresWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mRepository = ((MediviaVipListApp) context).getRepository();
    }

    @NonNull
    @Override
    public Result doWork() {
        for(String server : Constants.SERVERS) {
            mRepository.updateHighscoreByServer(server);
        }
        return Result.success();
    }
}
