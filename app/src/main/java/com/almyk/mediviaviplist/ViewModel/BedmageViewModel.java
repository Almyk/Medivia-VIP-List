package com.almyk.mediviaviplist.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.almyk.mediviaviplist.Database.Entities.BedmageEntity;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;

import java.util.List;

public class BedmageViewModel extends AndroidViewModel {
    private DataRepository mRepository;
    private LiveData<List<BedmageEntity>> mBedmages;

    public BedmageViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((MediviaVipListApp) application).getRepository();
        mBedmages = mRepository.getBedmages();
    }

    public LiveData<List<BedmageEntity>> getBedmages() {
        return mBedmages;
    }

    public void removeBedmage(BedmageEntity bedmage) {
        mRepository.deleteBedmage(bedmage, 0);
    }

    public void addBedmage(BedmageEntity bedmage) {
        mRepository.addBedmage(bedmage);
    }
}
