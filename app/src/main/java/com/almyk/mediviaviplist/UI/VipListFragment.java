package com.almyk.mediviaviplist.UI;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.UI.Settings.SettingsActivity;
import com.almyk.mediviaviplist.Utilities.EditTextBackEvent;
import com.almyk.mediviaviplist.ViewModel.VipListViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

import java.util.List;

public class VipListFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, TextView.OnEditorActionListener {
    private static final String TAG = VipListFragment.class.getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;

    private VipListViewModel mViewModel;

    private SwipeRefreshLayout mSwipeContainer;
    private RecyclerView mRecyclerView;
    private VipListAdapter mAdapter;

    private Button mAddPlayerButton;
    private EditTextBackEvent mNewPlayerTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.vip_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.rv_vip);
        if (mRecyclerView == null) {
            Log.e(TAG, "mRecyclerView is null");
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), VERTICAL));
        mAdapter = new VipListAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mSwipeContainer = rootView.findViewById(R.id.swipe_container);
        mSwipeContainer.setOnRefreshListener(this);
        setupTouchHelper();


        mNewPlayerTextView = rootView.findViewById(R.id.et_player_name);
        mNewPlayerTextView.setOnEditorActionListener(this);
        mNewPlayerTextView.setOnEditTextImeBackListener(new EditTextBackEvent.EditTextImeBackListener() {
            @Override
            public void onImeBack(EditTextBackEvent ctrl, String text) {
                mNewPlayerTextView.clearFocus();
            }
        });

        mAddPlayerButton = rootView.findViewById(R.id.bt_add_player);
        mAddPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.addPlayer(mNewPlayerTextView.getText().toString());
                mNewPlayerTextView.getText().clear();
                mNewPlayerTextView.clearFocus();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(VipListViewModel.class);
        mViewModel.init();

        mViewModel.getVipList().observe(this, new Observer<List<PlayerEntity>>() {
            @Override
            public void onChanged(@Nullable List<PlayerEntity> playerEntities) {
                mAdapter.setPlayers(playerEntities);
            }
        });
    }

    public static VipListFragment newInstance() {
        return new VipListFragment();
    }

    @Override
    public void onRefresh() {
        mViewModel.forceUpdateVipList();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeContainer.setRefreshing(false);
            }
        }, 2000);
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
                List<PlayerEntity> players = mAdapter.getPlayers();
                mViewModel.removePlayer(players.get(pos));
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_vip_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Log.d(TAG, "ActionId: " + actionId);
        switch(actionId) {
            case EditorInfo.IME_ACTION_DONE:
                mViewModel.addPlayer(mNewPlayerTextView.getText().toString());
                mNewPlayerTextView.getText().clear();
                mNewPlayerTextView.clearFocus();
            case KeyEvent.KEYCODE_BACK:
                mNewPlayerTextView.clearFocus();
            default:
                return false;
        }
    }
}