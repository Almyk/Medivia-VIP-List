package com.almyk.mediviaviplist.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.Utilities.Constants;

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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        holder.isMuted = preferences.getBoolean(name + "_muted", false);
        if(holder.isMuted) {
            holder.mMutedIcon.setVisibility(View.VISIBLE);
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

    public class VipListViewHolder extends RecyclerView.ViewHolder implements MenuItem.OnMenuItemClickListener {
        private TextView mName;
        private TextView mLevel;
        private TextView mVocation;
        private TextView mServer;
        private TextView mOnline;
        private ImageView mMutedIcon;
        private boolean isMuted;


        public VipListViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_name);
            mLevel = itemView.findViewById(R.id.tv_level);
            mVocation = itemView.findViewById(R.id.tv_vocation);
            mServer = itemView.findViewById(R.id.tv_server);
            mOnline = itemView.findViewById(R.id.tv_online);
            mMutedIcon = itemView.findViewById(R.id.ic_muted);

            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    if(isMuted) {
                        menu.add("Turn ON Notifications").setOnMenuItemClickListener(VipListViewHolder.this);
                    } else {
                        menu.add("Turn OFF Notifications").setOnMenuItemClickListener(VipListViewHolder.this);
                    }
                    menu.add("Player Details").setOnMenuItemClickListener(VipListViewHolder.this);
                }
            });
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String title = item.getTitle().toString();
            switch (title) {
                case "Turn OFF Notifications": // notification buttons
                case "Turn ON Notifications":
                    isMuted = !isMuted;
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                    preferences.edit().putBoolean(mName.getText() + "_muted", isMuted).commit();
                    int visibility = isMuted ? View.VISIBLE : View.GONE;
                    mMutedIcon.setVisibility(visibility);
                    Toast.makeText(mContext, title + " for " + mName.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                case "Player Details":
                    Toast.makeText(mContext, "Player Details", Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        }
    }
}
