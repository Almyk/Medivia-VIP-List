package com.almyk.mediviaviplist.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almyk.mediviaviplist.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BedmageFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private BedmageAdapter mAdapter;

    public BedmageFragment() {
    }

    public static BedmageFragment newInstance() {
        return new BedmageFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bedmage, container, false);

        mRecyclerView = rootView.findViewById(R.id.rv_bedmage_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new BedmageAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

}
