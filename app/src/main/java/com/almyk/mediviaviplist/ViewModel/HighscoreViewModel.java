package com.almyk.mediviaviplist.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.almyk.mediviaviplist.Database.HighscoreEntity;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;

import java.util.List;

public class HighscoreViewModel extends AndroidViewModel {
    private DataRepository mRepository;

    private LiveData<List<HighscoreEntity>> mHighscores;

    private String mServer;
    private String mVocation;
    private String mSkill;

    public HighscoreViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = ((MediviaVipListApp) application).getRepository();
        this.mVocation = "all";
        this.mSkill = "level";
    }

    public void init(String server) {
        this.mServer = server;
        this.mHighscores = mRepository.getHighscores(mServer, mVocation, mSkill);
    }

    public LiveData<List<HighscoreEntity>> getHighscores() {
        return mHighscores;
    }
}
