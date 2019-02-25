package com.almyk.mediviaviplist.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;
import com.almyk.mediviaviplist.UI.MainActivity;

import java.util.List;

public class OnlineListViewModel extends AndroidViewModel {
    private DataRepository mRepository;

    private LiveData<List<PlayerEntity>> mOnlineList;

    public OnlineListViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((MediviaVipListApp) application).getRepository();
    }

    public LiveData<List<PlayerEntity>> getOnlineList() {
        return mOnlineList;
    }

    public void init(String server) {
        mOnlineList = mRepository.getOnlineByServer(server);
    }
}
