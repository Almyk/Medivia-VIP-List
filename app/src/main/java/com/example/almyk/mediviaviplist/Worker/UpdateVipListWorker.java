package com.example.almyk.mediviaviplist.Worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.almyk.mediviaviplist.Constants;
import com.example.almyk.mediviaviplist.MediviaVipListApp;
import com.example.almyk.mediviaviplist.Repository.DataRepository;

import java.util.concurrent.TimeUnit;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateVipListWorker extends Worker {
    private final static String TAG = UpdateVipListWorker.class.getSimpleName();
    private long mSleepTime = 60000;

    public UpdateVipListWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        updateVipList(((MediviaVipListApp) getApplicationContext()).getRepository());
        Log.d(TAG, "updated vip list");


        // Create a new periodic work request so that we can have intervals <15m
        try {
            Thread.sleep(mSleepTime, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                UpdateVipListWorker.class, 10, TimeUnit.SECONDS)
                .addTag(Constants.UPDATE_VIP_LIST_TAG)
                .build();
        WorkManager.getInstance().enqueueUniquePeriodicWork(Constants.UPDATE_VIP_LIST_UNIQUE_NAME, ExistingPeriodicWorkPolicy.REPLACE,workRequest);
        return Result.success();
    }

    private void updateVipList(DataRepository repository) {
        repository.updateVipList("Pendulum");
        repository.updateVipList("Prophecy");
        repository.updateVipList("Destiny");
        repository.updateVipList("Legacy");
    }
}
