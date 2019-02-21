package com.almyk.mediviaviplist;

import android.app.Application;

import com.almyk.mediviaviplist.Database.AppDatabase;
import com.almyk.mediviaviplist.Repository.DataRepository;

public class MediviaVipListApp extends Application {

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase(), this);
    }
}
