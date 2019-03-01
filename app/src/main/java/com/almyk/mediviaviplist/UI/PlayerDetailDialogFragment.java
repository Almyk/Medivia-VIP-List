package com.almyk.mediviaviplist.UI;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerDetailDialogFragment extends DialogFragment {
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

    private static PlayerEntity mPlayer;


    public PlayerDetailDialogFragment() {
        // Required empty public constructor
    }

    public static PlayerDetailDialogFragment newInstance() {
        return new PlayerDetailDialogFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_player_detail, container, false);

        mPrevNameContainer = rootView.findViewById(R.id.previous_name_container);
        mGuildContainer = rootView.findViewById(R.id.guild_container);
        mHouseContainer = rootView.findViewById(R.id.house_container);
        mCommentContainer = rootView.findViewById(R.id.comment_container);
        mBanishmentContainer = rootView.findViewById(R.id.banishment_container);

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

        view.findViewById(R.id.top_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateView();
    }

    private void populateView() {
        PlayerEntity player = mPlayer;

        mNameView.setText(player.getName());
        mServerView.setText(player.getServer());
        mLevelView.setText(player.getLevel());
        mVocationView.setText(player.getVocation());
        mResidenceView.setText(player.getResidence());
        mGenderView.setText(player.getSex());
        mAccountStatusView.setText(player.getAccountStatus());
        mLastLogin.setText(player.getLastLogin());

        boolean online = player.isOnline();
        if(online) {
            mOnlineView.setText("Online");
            mOnlineView.setTextColor(Color.GREEN);
        } else {
            mOnlineView.setText("Offline");
            mOnlineView.setTextColor(Color.RED);
        }

        String prevNem = player.getPreviousName();
        if(!TextUtils.isEmpty(prevNem)) {
            mPrevNameView.setText(prevNem);
            mPrevNameContainer.setVisibility(View.VISIBLE);
        }

        String guild = player.getGuild();
        if(!TextUtils.isEmpty(guild)) {
            mGuildView.setText(player.getGuild());
            mGuildContainer.setVisibility(View.VISIBLE);
        }

        String house = player.getHouse();
        if(!TextUtils.isEmpty(house)) {
            mHouseView.setText(player.getHouse());
            mHouseContainer.setVisibility(View.VISIBLE);
        }

        String comment = player.getComment();
        if(!TextUtils.isEmpty(comment)) {
            mCommentView.setText(player.getComment());
            mCommentContainer.setVisibility(View.VISIBLE);
        }

        String banishment = player.getBanishment();
        if(!TextUtils.isEmpty(banishment)) {
            mBanishment.setText(banishment);
            mBanishmentContainer.setVisibility(View.VISIBLE);
        }
    }

    public void setPlayer(PlayerEntity player) {
        this.mPlayer = player;
    }
}
