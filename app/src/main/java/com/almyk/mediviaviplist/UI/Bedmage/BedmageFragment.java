package com.almyk.mediviaviplist.UI.Bedmage;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.almyk.mediviaviplist.Database.Entities.BedmageEntity;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.ViewModel.BedmageViewModel;

import java.util.List;

public class BedmageFragment extends Fragment implements View.OnClickListener {
    private final static String TAG = BedmageFragment.class.getSimpleName();

    private BedmageViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private BedmageAdapter mAdapter;
    private FloatingActionButton mFab;
    private CoordinatorLayout mCoordLayout;

    private MenuItem mBedmageMuteItem;
    private MenuItem mBedmageUnMuteItem;

    public BedmageFragment() {
    }

    public static BedmageFragment newInstance() {
        return new BedmageFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bedmage, container, false);

        mCoordLayout = rootView.findViewById(R.id.coord_layout);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewModel();
        setupTouchHelper();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuCompat.setGroupDividerEnabled(menu, true);
        inflater.inflate(R.menu.menu_bedmage, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mBedmageMuteItem = menu.findItem(R.id.bedmage_mute);
        mBedmageUnMuteItem = menu.findItem(R.id.bedmage_unmute);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isMuted = preferences.getBoolean(getString(R.string.bedmage_isMuted), false);
        Log.d(TAG, "onPrepareOptionsMenu, isMuted: " + isMuted);
        if (isMuted) {
            mBedmageUnMuteItem.setVisible(true);
            mBedmageMuteItem.setVisible(false);
        } else {
            mBedmageUnMuteItem.setVisible(false);
            mBedmageMuteItem.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        switch (item.getItemId()) {
            case R.id.bedmage_mute:
                preferences.edit().putBoolean(getString(R.string.bedmage_isMuted), true).apply();
                mBedmageMuteItem.setVisible(false);
                mBedmageUnMuteItem.setVisible(true);
                Log.d(TAG, "mute bedmages");
                return true;
            case R.id.bedmage_unmute:
                preferences.edit().putBoolean(getString(R.string.bedmage_isMuted), false).apply();
                mBedmageMuteItem.setVisible(true);
                mBedmageUnMuteItem.setVisible(false);
                Log.d(TAG, " unmute bedmages");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                Snackbar snackbar = Snackbar.make(mCoordLayout, "Take back deletion of " + bedmage.getName() + "?", Snackbar.LENGTH_LONG);
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
