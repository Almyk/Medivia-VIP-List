package com.almyk.mediviaviplist.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.R;

import java.util.List;

public class OnlineListAdapter extends RecyclerView.Adapter<OnlineListAdapter.OnlineListViewHolder> {
    private Context mContext;
    private List<PlayerEntity> mOnlineList;

    public OnlineListAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public OnlineListAdapter.OnlineListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.online_entry, viewGroup, false);
        return new OnlineListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineListAdapter.OnlineListViewHolder holder, int i) {
        PlayerEntity player = mOnlineList.get(i);
        String name = player.getName();
        String level = player.getLevel();
        String vocation = player.getVocation();
        String lastLogin = player.getLastLogin();

        holder.mName.setText(name);
        holder.mLevel.setText(level);
        holder.mVocation.setText(vocation);
        holder.mLastLogin.setText(lastLogin);
    }

    @Override
    public int getItemCount() {
        return mOnlineList.size();
    }

    public void setOnlineList(List<PlayerEntity> onlineList) {
        this.mOnlineList = onlineList;
        notifyDataSetChanged();
    }

    public class OnlineListViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private TextView mLevel;
        private TextView mVocation;
        private TextView mLastLogin;

        public OnlineListViewHolder(@NonNull View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.tv_name);
            mLevel = itemView.findViewById(R.id.tv_level);
            mVocation = itemView.findViewById(R.id.tv_vocation);
            mLastLogin = itemView.findViewById(R.id.tv_last_login);
        }
    }
}
