package com.almyk.mediviaviplist.UI;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.Model.Player;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.Utilities.EditTextBackEvent;
import com.almyk.mediviaviplist.ViewModel.SearchCharacterViewModel;

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
    private TextView mLastLogin;
    private TextView mBanishment;

    private LinearLayout mPrevNameContainer;
    private LinearLayout mGuildContainer;
    private LinearLayout mHouseContainer;
    private LinearLayout mCommentContainer;
    private LinearLayout mBanishmentContainer;
    private FrameLayout mPlayerDetailContainer;
    private ProgressBar mProgressBar;

    private String mName;

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
        mSearchButton = rootView.findViewById(R.id.button_search);
        mSearchButton.setOnClickListener(this);

        mPrevNameContainer = rootView.findViewById(R.id.previous_name_container);
        mGuildContainer = rootView.findViewById(R.id.guild_container);
        mHouseContainer = rootView.findViewById(R.id.house_container);
        mCommentContainer = rootView.findViewById(R.id.comment_container);
        mPlayerDetailContainer = rootView.findViewById(R.id.player_detail_container);
        mBanishmentContainer = rootView.findViewById(R.id.banishment_container);
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
        mLastLogin = rootView.findViewById(R.id.tv_last_login);
        mBanishment = rootView.findViewById(R.id.tv_banishment);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Search Character");
        this.mViewModel = ViewModelProviders.of(this).get(SearchCharacterViewModel.class);
        if(!TextUtils.isEmpty(mName)) {
            mPlayerDetailContainer.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            mViewModel.searchPlayer(mName);
        }
        setupViewModel();
    }

    private void setupViewModel() {
        mViewModel.getPlayer().observe(this, new Observer<Player>() {
            @Override
            public void onChanged(@Nullable Player player) {
                if(player != null) {
                    if (!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(player.getPlayerName())
                            && player.getPlayerName().toLowerCase().equals(mName.toLowerCase())) {
                        populateView(player);
                    }
                } else {
                    mPlayerDetailContainer.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Could not find player '" +mName+"'", Toast.LENGTH_SHORT).show();;
                    mNameEditTextView.requestFocus();
                }
            }
        });
    }

    private void populateView(Player player) {
        PlayerEntity playerEntity = player.getPlayerEntity();
        mNameView.setText(playerEntity.getName());
        mServerView.setText(playerEntity.getServer());
        mLevelView.setText(playerEntity.getLevel());
        mVocationView.setText(playerEntity.getVocation());
        mResidenceView.setText(playerEntity.getResidence());
        mGenderView.setText(playerEntity.getSex());
        mAccountStatusView.setText(playerEntity.getAccountStatus());
        mLastLogin.setText(playerEntity.getLastLogin());

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
        }

        String guild = playerEntity.getGuild();
        if(!TextUtils.isEmpty(guild)) {
            mGuildView.setText(playerEntity.getGuild());
            mGuildContainer.setVisibility(View.VISIBLE);
        }

        String house = playerEntity.getHouse();
        if(!TextUtils.isEmpty(house)) {
            mHouseView.setText(playerEntity.getHouse());
            mHouseContainer.setVisibility(View.VISIBLE);
        }

        String comment = playerEntity.getComment();
        if(!TextUtils.isEmpty(comment)) {
            mCommentView.setText(playerEntity.getComment());
            mCommentContainer.setVisibility(View.VISIBLE);
        }

        String banishment = playerEntity.getBanishment();
        if(!TextUtils.isEmpty(banishment)) {
            mBanishment.setText(banishment);
            mBanishmentContainer.setVisibility(View.VISIBLE);
        }

        mProgressBar.setVisibility(View.GONE);
        mPlayerDetailContainer.setVisibility(View.VISIBLE);
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
            mPlayerDetailContainer.setVisibility(View.GONE);
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
