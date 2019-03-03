package com.almyk.mediviaviplist.UI;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.almyk.mediviaviplist.Database.DeathEntity;
import com.almyk.mediviaviplist.Database.KillEntity;
import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.Database.TaskEntity;
import com.almyk.mediviaviplist.Model.Player;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.Utilities.EditTextBackEvent;
import com.almyk.mediviaviplist.ViewModel.SearchCharacterViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchCharacterFragment extends Fragment
        implements View.OnClickListener, TextView.OnEditorActionListener {

    private EditTextBackEvent mNameEditTextView;
    private ImageButton mSearchButton;

    private TextView mNameView;
    private TextView mPrevNameView;
    private TextView mServerView;
    private TextView mLevelView;
    private TextView mVocationView;
    private TextView mOnlineView;
    private TextView mResidenceView;
    private TextView mGuildView;
    private TextView mHouseView;
    private TextView mGenderView;
    private TextView mAccountStatusView;
    private TextView mCommentView;
    private TextView mLastLoginView;
    private TextView mBanishmentView;
    private TextView mDeathListTextView;
    private TextView mKillListTextView;
    private TextView mTaskListTextView;
    private TextView mTransferView;

    private RecyclerView mDeathListRecycler;
    private RecyclerView mKillListRecycler;
    private RecyclerView mTaskListRecycler;

    private DeathListAdapter mDeathListAdapter;
    private KillListAdapter mKillListAdapter;
    private TaskListAdapter mTaskListAdapter;

    private LinearLayout mPrevNameContainer;
    private LinearLayout mGuildContainer;
    private LinearLayout mHouseContainer;
    private LinearLayout mCommentContainer;
    private LinearLayout mBanishmentContainer;
    private LinearLayout mPlayerContainer;
    private LinearLayout mTransferContainer;

    private ProgressBar mProgressBar;

    private static String mName;

    private SearchCharacterViewModel mViewModel;


    public static SearchCharacterFragment newInstance() {
        return new SearchCharacterFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_character, container, false);

        mNameEditTextView = rootView.findViewById(R.id.et_player_name);
        mNameEditTextView.setOnEditorActionListener(this);
        mNameEditTextView.setOnEditTextImeBackListener(new EditTextBackEvent.EditTextImeBackListener() {
            @Override
            public void onImeBack(EditTextBackEvent ctrl, String text) {
                mNameEditTextView.clearFocus();
            }
        });
        mSearchButton = rootView.findViewById(R.id.button_search);
        mSearchButton.setOnClickListener(this);

        mPrevNameContainer = rootView.findViewById(R.id.previous_name_container);
        mGuildContainer = rootView.findViewById(R.id.guild_container);
        mHouseContainer = rootView.findViewById(R.id.house_container);
        mCommentContainer = rootView.findViewById(R.id.comment_container);
        mBanishmentContainer = rootView.findViewById(R.id.banishment_container);
        mPlayerContainer = rootView.findViewById(R.id.player_container);
        mTransferContainer = rootView.findViewById(R.id.transfer_container);

        mProgressBar = rootView.findViewById(R.id.progress_bar);

        mNameView = rootView.findViewById(R.id.tv_name);
        mPrevNameView = rootView.findViewById(R.id.tv_prev_name);
        mServerView = rootView.findViewById(R.id.tv_server);
        mLevelView = rootView.findViewById(R.id.tv_level);
        mVocationView = rootView.findViewById(R.id.tv_vocation);
        mOnlineView = rootView.findViewById(R.id.tv_online);
        mResidenceView = rootView.findViewById(R.id.tv_residence);
        mGuildView = rootView.findViewById(R.id.tv_guild);
        mHouseView = rootView.findViewById(R.id.tv_house);
        mGenderView = rootView.findViewById(R.id.tv_gender);
        mAccountStatusView = rootView.findViewById(R.id.tv_account_status);
        mCommentView = rootView.findViewById(R.id.tv_comment);
        mLastLoginView = rootView.findViewById(R.id.tv_last_login);
        mBanishmentView = rootView.findViewById(R.id.tv_banishment);
        mDeathListTextView = rootView.findViewById(R.id.tv_death_list);
        mKillListTextView = rootView.findViewById(R.id.tv_kill_list);
        mTaskListTextView = rootView.findViewById(R.id.tv_task_list);
        mTransferView = rootView.findViewById(R.id.tv_transfer);

        setupRecyclerViews(rootView);

        return rootView;
    }

    private void setupRecyclerViews(View rootView) {
        mDeathListRecycler = rootView.findViewById(R.id.rv_death_list);
        mDeathListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDeathListRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mDeathListRecycler.setNestedScrollingEnabled(false);
        mDeathListAdapter = new DeathListAdapter(getActivity());
        mDeathListRecycler.setAdapter(mDeathListAdapter);

        mKillListRecycler = rootView.findViewById(R.id.rv_kill_list);
        mKillListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mKillListRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mKillListRecycler.setNestedScrollingEnabled(false);
        mKillListAdapter = new KillListAdapter(getActivity());
        mKillListRecycler.setAdapter(mKillListAdapter);

        mTaskListRecycler = rootView.findViewById(R.id.rv_task_list);
        mTaskListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskListRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mTaskListRecycler.setNestedScrollingEnabled(false);
        mTaskListAdapter = new TaskListAdapter(getActivity());
        mTaskListRecycler.setAdapter(mTaskListAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Search Character");
        this.mViewModel = ViewModelProviders.of(this).get(SearchCharacterViewModel.class);
        if(!TextUtils.isEmpty(mName)) {
            mPlayerContainer.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mViewModel.searchPlayer(mName);
        }
        setupViewModel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_vip:
                if(!TextUtils.isEmpty(mName)) {
                    mViewModel.addVip(mName);
                    Toast.makeText(getActivity(), "Added " + mName.toUpperCase() + " to VIP list", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewModel() {
        mViewModel.getPlayer().observe(this, new Observer<Player>() {
            @Override
            public void onChanged(@Nullable Player player) {
                if(player != null) {
//                    if (!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(player.getPlayerName())
//                            && player.getPlayerName().toLowerCase().equals(mName.toLowerCase())) {
                        mPlayerContainer.setVisibility(View.VISIBLE);
                        populateView(player);
//                    }
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    mPlayerContainer.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Could not find player '" +mName+"'", Toast.LENGTH_SHORT).show();;
                    mNameEditTextView.requestFocus();
                }
            }
        });
    }

    private void populateView(Player player) {
        populatePlayerEntity(player);
        populateDeathList(player);
        populateKillList(player);
        populateTaskList(player);
    }

    private void populateDeathList(Player player) {
        List<DeathEntity> deaths = player.getDeaths();
        if(deaths != null && !deaths.isEmpty()) {
            mDeathListRecycler.setVisibility(View.VISIBLE);
            mDeathListTextView.setVisibility(View.VISIBLE);
            mDeathListAdapter.setDeaths(deaths);
        } else {
            mDeathListRecycler.setVisibility(View.GONE);
            mDeathListTextView.setVisibility(View.GONE);
        }
    }

    private void populateKillList(Player player) {
        List<KillEntity> kills = player.getKills();
        if(kills != null && !kills.isEmpty()) {
            mKillListRecycler.setVisibility(View.VISIBLE);
            mKillListTextView.setVisibility(View.VISIBLE);
            mKillListAdapter.setKills(kills);
        } else {
            mKillListRecycler.setVisibility(View.GONE);
            mKillListTextView.setVisibility(View.GONE);
        }
    }

    private void populateTaskList(Player player) {
        List<TaskEntity> tasks = player.getTasks();
        if(tasks != null && !tasks.isEmpty()) {
            mTaskListRecycler.setVisibility(View.VISIBLE);
            mTaskListTextView.setVisibility(View.VISIBLE);
            mTaskListAdapter.setTasks(tasks);
        } else {
            mTaskListRecycler.setVisibility(View.GONE);
            mTaskListTextView.setVisibility(View.GONE);
        }

    }

    private void populatePlayerEntity(Player player) {
        PlayerEntity playerEntity = player.getPlayerEntity();
        mNameView.setText(playerEntity.getName());
        mServerView.setText(playerEntity.getServer());
        mLevelView.setText(playerEntity.getLevel());
        mVocationView.setText(playerEntity.getVocation());
        mResidenceView.setText(playerEntity.getResidence());
        mGenderView.setText(playerEntity.getSex());
        mAccountStatusView.setText(playerEntity.getAccountStatus());
        mLastLoginView.setText(playerEntity.getLastLogin());

        boolean online = playerEntity.isOnline();
        if(online) {
            mOnlineView.setText("Online");
            mOnlineView.setTextColor(Color.GREEN);
        } else {
            mOnlineView.setText("Offline");
            mOnlineView.setTextColor(Color.RED);
        }

        String prevNem = playerEntity.getPreviousName();
        if(!TextUtils.isEmpty(prevNem)) {
            mPrevNameView.setText(prevNem);
            mPrevNameContainer.setVisibility(View.VISIBLE);
        } else {
            mPrevNameContainer.setVisibility(View.GONE);
        }

        String guild = playerEntity.getGuild();
        if(!TextUtils.isEmpty(guild)) {
            mGuildView.setText(playerEntity.getGuild());
            mGuildContainer.setVisibility(View.VISIBLE);
        } else {
            mGuildContainer.setVisibility(View.GONE);
        }

        String house = playerEntity.getHouse();
        if(!TextUtils.isEmpty(house)) {
            mHouseView.setText(playerEntity.getHouse());
            mHouseContainer.setVisibility(View.VISIBLE);
        } else {
            mHouseContainer.setVisibility(View.GONE);
        }

        String comment = playerEntity.getComment();
        if(!TextUtils.isEmpty(comment)) {
            mCommentView.setText(playerEntity.getComment());
            mCommentContainer.setVisibility(View.VISIBLE);
        } else {
            mCommentContainer.setVisibility(View.GONE);
        }

        String banishment = playerEntity.getBanishment();
        if(!TextUtils.isEmpty(banishment)) {
            mBanishmentView.setText(banishment);
            mBanishmentContainer.setVisibility(View.VISIBLE);
        } else {
            mBanishmentContainer.setVisibility(View.GONE);
        }

        String transfer = playerEntity.getTransfer();
        if(!TextUtils.isEmpty(transfer)) {
            mTransferView.setText(transfer);
            mTransferContainer.setVisibility(View.VISIBLE);
        } else {
            mTransferContainer.setVisibility(View.GONE);
        }

        mProgressBar.setVisibility(View.GONE);
//        mPlayerContainer.setVisibility(View.VISIBLE);
    }

    public void setName(String Name) {
        this.mName = Name;
    }

    @Override
    public void onClick(View v) {
        searchPlayer();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch(actionId) {
            case EditorInfo.IME_ACTION_DONE:
                searchPlayer();
            case KeyEvent.KEYCODE_BACK:
                mNameEditTextView.clearFocus();
            default:
                return false;
        }
    }

    private void searchPlayer() {
        String text = mNameEditTextView.getText().toString();
        if(!TextUtils.isEmpty(text)) {
            mPlayerContainer.setVisibility(View.GONE);
            mDeathListRecycler.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mNameEditTextView.getText().clear();
            mNameEditTextView.clearFocus();
            this.mName = text;
            mViewModel.searchPlayer(mName);
        } else {
            Toast.makeText(getActivity(), "Please enter a name and try again", Toast.LENGTH_LONG).show();
        }
    }
}
