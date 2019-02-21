package com.almyk.mediviaviplist.UI.Settings;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.almyk.mediviaviplist.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_vip_list, rootKey);
    }
}
