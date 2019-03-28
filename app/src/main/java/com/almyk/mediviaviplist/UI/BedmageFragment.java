package com.almyk.mediviaviplist.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almyk.mediviaviplist.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BedmageFragment extends Fragment {


    public BedmageFragment() {
    }

    public static BedmageFragment newInstance() {
        return new BedmageFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bedmage, container, false);

        return rootView;
    }

}
