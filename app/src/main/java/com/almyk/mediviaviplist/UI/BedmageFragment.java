package com.almyk.mediviaviplist.UI;


import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almyk.mediviaviplist.Database.Entities.BedmageEntity;
import com.almyk.mediviaviplist.Database.Entities.PlayerEntity;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.ViewModel.BedmageViewModel;

import java.util.List;

public class BedmageFragment extends Fragment implements View.OnClickListener {
    private BedmageViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private BedmageAdapter mAdapter;
    private FloatingActionButton mFab;

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
        mFab = rootView.findViewById(R.id.fab);
        mFab.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewModel();
        setupTouchHelper();
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(BedmageViewModel.class);

        mViewModel.getBedmages().observe(this, new Observer<List<BedmageEntity>>() {
            @Override
            public void onChanged(@Nullable List<BedmageEntity> bedmageEntities) {
                mAdapter.setBedmages(bedmageEntities);
            }
        });
    }

    private void setupTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int pos = viewHolder.getAdapterPosition();
                List<BedmageEntity> bedmages = mAdapter.getBedmages();
                final BedmageEntity bedmage = bedmages.get(pos);
                mViewModel.removeBedmage(bedmage);
                Snackbar snackbar = Snackbar.make(viewHolder.itemView.getRootView(), "Take back deletion of " + bedmage.getName() + "?", Snackbar.LENGTH_LONG);
                snackbar.setAction("TAKE BACK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewModel.addBedmage(bedmage);
                    }
                });
                snackbar.show();
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                DialogFragment dialog = new BedmageDialog();
                dialog.show(getFragmentManager(), "Add Bedmage");
                break;
        }

    }
}
