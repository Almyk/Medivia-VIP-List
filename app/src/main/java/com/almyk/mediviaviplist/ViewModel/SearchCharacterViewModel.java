package com.almyk.mediviaviplist.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;

public class SearchCharacterViewModel extends AndroidViewModel {
    private DataRepository mRepository;

    private MutableLiveData<PlayerEntity> mPlayer = new MutableLiveData<>();

    public SearchCharacterViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = ((MediviaVipListApp) application).getRepository();
    }

    public LiveData<PlayerEntity> getPlayer() {
        return mPlayer;
    }

    public void searchPlayer(String name) {
        mPlayer.postValue(mRepository.searchPlayer(name).getValue());
    }


}
