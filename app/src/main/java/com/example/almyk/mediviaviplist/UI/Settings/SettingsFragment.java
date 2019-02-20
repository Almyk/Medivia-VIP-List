package com.example.almyk.mediviaviplist.UI.Settings;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.almyk.mediviaviplist.MediviaVipListApp;
import com.example.almyk.mediviaviplist.R;
import com.example.almyk.mediviaviplist.Worker.UpdateVipListWorker;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_vip_list, rootKey);
    }
}
