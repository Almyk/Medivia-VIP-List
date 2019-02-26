package com.almyk.mediviaviplist.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almyk.mediviaviplist.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HighscoreFragment extends Fragment {
    private String mServer;


    public HighscoreFragment() {
    }

    public static HighscoreFragment newInstance() {
        return new HighscoreFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_highscore, container, false);
    }

    public void setServer(String server) {
        this.mServer = server;
    }
}
