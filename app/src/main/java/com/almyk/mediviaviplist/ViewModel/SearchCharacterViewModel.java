package com.almyk.mediviaviplist.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Model.Player;
import com.almyk.mediviaviplist.Repository.DataRepository;

public class SearchCharacterViewModel extends AndroidViewModel {
    private DataRepository mRepository;

    private LiveData<Player> mPlayer;

    public SearchCharacterViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = ((MediviaVipListApp) application).getRepository();
        mPlayer = mRepository.getSearchCharacterLiveData();
    }

    public LiveData<Player> getPlayer() {
        return mPlayer;
    }

    public void searchPlayer(String name) {
        String tmpName = name.trim();
        mRepository.searchPlayer(tmpName);
    }


}
