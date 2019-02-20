package com.example.almyk.mediviaviplist.Worker;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.almyk.mediviaviplist.Utilities.Constants;
import com.example.almyk.mediviaviplist.MediviaVipListApp;
import com.example.almyk.mediviaviplist.Repository.DataRepository;

import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateVipListWorker extends Worker {
    private final static String TAG = UpdateVipListWorker.class.getSimpleName();
    private static long mSleepTime = 60000;

    public UpdateVipListWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        DataRepository repository = ((MediviaVipListApp) getApplicationContext()).getRepository();
        setSleepTime(repository.getSyncInterval());
        Log.d(TAG, "mSleepTime: " + mSleepTime);
        updateVipList(repository);
        enqueueNextRequest();
        return Result.success();
    }

    private void enqueueNextRequest() {
        // Create a new periodic work request so that we can have intervals <15m
        sleep();
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                UpdateVipListWorker.class, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .addTag(Constants.UPDATE_VIP_LIST_TAG)
                .build();
        WorkManager.getInstance().enqueueUniquePeriodicWork(Constants.UPDATE_VIP_LIST_UNIQUE_NAME, ExistingPeriodicWorkPolicy.REPLACE,workRequest);
    }

    private void sleep() {
        try {
            Thread.sleep(mSleepTime, 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateVipList(DataRepository repository) {
        repository.updateVipList("Pendulum");
        repository.updateVipList("Prophecy");
        repository.updateVipList("Destiny");
        repository.updateVipList("Legacy");
        Log.d(TAG, "updated vip list");
    }

    public void setSleepTime(long SleepTime) {
        this.mSleepTime = SleepTime;
    }
}
