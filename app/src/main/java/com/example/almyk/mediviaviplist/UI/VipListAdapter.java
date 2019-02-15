package com.example.almyk.mediviaviplist.UI;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.almyk.mediviaviplist.Database.PlayerEntity;
import com.example.almyk.mediviaviplist.R;

import java.util.List;

public class VipListAdapter extends RecyclerView.Adapter<VipListAdapter.VipListViewHolder> {
    private static final String TAG = VipListAdapter.class.getSimpleName();
    private Context mContext;

    private List<PlayerEntity> mPlayers;

    public VipListAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public VipListAdapter.VipListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.vip_entry, viewGroup, false);

        return new VipListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VipListAdapter.VipListViewHolder holder, int position) {
        PlayerEntity player = mPlayers.get(position);
        String name = player.getName();
        String level = player.getLevel();
        String vocation = player.getVocation();
        String server = player.getServer();
        boolean online = player.isOnline();

        holder.mName.setText(name);
        holder.mLevel.setText(level);
        holder.mVocation.setText(vocation);
        holder.mServer.setText(server);
        if(online) {
            holder.mOnline.setText("Online");
            holder.mOnline.setTextColor(Color.GREEN);
        } else {
            holder.mOnline.setText("Offline");
            holder.mOnline.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        if(mPlayers == null) {
            return 0;
        }
        return mPlayers.size();
    }

    public void setPlayers(List<PlayerEntity> players) {
        mPlayers = players;

        notifyDataSetChanged();
    }

    public List<PlayerEntity> getPlayers() {
        return mPlayers;
    }

    public class VipListViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private TextView mLevel;
        private TextView mVocation;
        private TextView mServer;
        private TextView mOnline;


        public VipListViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_name);
            mLevel = itemView.findViewById(R.id.tv_level);
            mVocation = itemView.findViewById(R.id.tv_vocation);
            mServer = itemView.findViewById(R.id.tv_server);
            mOnline = itemView.findViewById(R.id.tv_online);
        }
    }
}
