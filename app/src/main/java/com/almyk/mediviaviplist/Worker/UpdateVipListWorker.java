package com.almyk.mediviaviplist.Worker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.almyk.mediviaviplist.Utilities.Constants;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateVipListWorker extends Worker {
    private final static String TAG = UpdateVipListWorker.class.getSimpleName();
    private static long mDelay = 60000;

    public UpdateVipListWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Updating VIP List");
        DataRepository repository = ((MediviaVipListApp) getApplicationContext()).getRepository();
        setSleepTime(repository.getSyncInterval());
        updateVipList(repository);
        boolean doBackgroundSync = getInputData().getBoolean(Constants.DO_BGSYNC, false);
        if(doBackgroundSync) {
            enqueueNextRequest(repository);
        }
        return Result.success();
    }

    private void enqueueNextRequest(DataRepository repository) {
        // Create a new work request so that we can have intervals <15m
        Data data = new Data.Builder().putBoolean(Constants.DO_BGSYNC, repository.isDoBackgroundSync()).build();
        WorkManager workManager = repository.getWorkManager();
        Log.d(TAG, "New work scheduled");
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UpdateVipListWorker.class)
                .addTag(Constants.UPDATE_VIP_LIST_TAG)
                .setInputData(data)
                .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .setInitialDelay(mDelay, TimeUnit.MILLISECONDS)
                .build();
        workManager.enqueueUniqueWork(Constants.UPDATE_VIP_LIST_UNIQUE_NAME, ExistingWorkPolicy.APPEND, workRequest);
    }

    private void updateVipList(DataRepository repository) {
        repository.updateVipAndOnlineList("Pendulum");
        repository.updateVipAndOnlineList("Prophecy");
        repository.updateVipAndOnlineList("Destiny");
        repository.updateVipAndOnlineList("Legacy");
        Log.d(TAG, "updated vip list");
    }

    public void setSleepTime(long SleepTime) {
        this.mDelay = SleepTime;
    }
}
