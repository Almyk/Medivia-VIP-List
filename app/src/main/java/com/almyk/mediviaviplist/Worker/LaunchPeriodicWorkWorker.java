package com.almyk.mediviaviplist.Worker;

import android.content.Context;
import android.support.annotation.NonNull;

import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class LaunchPeriodicWorkWorker extends Worker {
    private DataRepository mRepository;

    public LaunchPeriodicWorkWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mRepository = ((MediviaVipListApp) context).getRepository();
    }

    @NonNull
    @Override
    public Result doWork() {
        if(mRepository != null) {
            mRepository.startPeriodicWorkers();
        }
        return Result.success();
    }
}
