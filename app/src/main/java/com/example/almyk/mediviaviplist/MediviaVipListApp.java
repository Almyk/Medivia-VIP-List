package com.example.almyk.mediviaviplist;

import android.app.Application;

import com.example.almyk.mediviaviplist.Database.AppDatabase;
import com.example.almyk.mediviaviplist.Repository.DataRepository;

public class MediviaVipListApp extends Application {

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase(), this);
    }
}
