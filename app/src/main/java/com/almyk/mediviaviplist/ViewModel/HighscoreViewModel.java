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

    public void changeHighscoreCategory() {
        this.mHighscores = mRepository.getHighscores(mServer, mVocation, mSkill);
    }

    public void setServer(String server) {
        this.mServer = server.toLowerCase();
    }

    public void setVocation(String vocation) {
        this.mVocation = vocation.toLowerCase();
    }

    public void setSkill(String skill) {
        switch(skill) {
            case "Magic Level":
                skill = "maglevel";
                break;
            case "Fist Fighting":
                skill = "fist";
                break;
            case "Club Fighting":
                skill = "club";
                break;
            case "Sword Fighting":
                skill = "sword";
                break;
            case "Axe Fighting":
                skill = "axe";
                break;
            case "Distance Fighting":
                skill = "distance";
                break;
        }

        this.mSkill = skill.toLowerCase();
    }

    public void refreshHighscore() {
        mRepository.updateHighscoreByServer(mServer);
    }

    public void addVip(String name) {
        mRepository.addPlayer(name);
    }
}
