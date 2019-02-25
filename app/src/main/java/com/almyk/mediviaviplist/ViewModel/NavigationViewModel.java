package com.almyk.mediviaviplist.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import com.almyk.mediviaviplist.MediviaVipListApp;
import com.almyk.mediviaviplist.Repository.DataRepository;

public class NavigationViewModel extends AndroidViewModel {
    private DataRepository mRepository;

    public NavigationViewModel(@NonNull Application application) {
        super(application);
        mRepository = ((MediviaVipListApp) application).getRepository();
    }

    public void prepareOnlineListFragment(String server) {
    }
}
