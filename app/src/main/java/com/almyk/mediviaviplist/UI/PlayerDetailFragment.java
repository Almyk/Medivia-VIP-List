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
public class PlayerDetailFragment extends Fragment {


    public PlayerDetailFragment() {
        // Required empty public constructor
    }

    public static PlayerDetailFragment newInstance() {
        return new PlayerDetailFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_player_detail, container, false);

        // TODO populate the views here

        return rootView;
    }

}
