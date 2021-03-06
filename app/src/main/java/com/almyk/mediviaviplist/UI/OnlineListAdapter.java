package com.almyk.mediviaviplist.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.almyk.mediviaviplist.Database.Entities.PlayerEntity;
import com.almyk.mediviaviplist.R;

import java.util.ArrayList;
import java.util.List;

public class OnlineListAdapter extends RecyclerView.Adapter<OnlineListAdapter.OnlineListViewHolder> {
    private Context mContext;
    private static List<PlayerEntity> mOnlineList = new ArrayList<>();

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

    public class OnlineListViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, MenuItem.OnMenuItemClickListener, View.OnCreateContextMenuListener {
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

            itemView.setOnClickListener(OnlineListViewHolder.this);
            itemView.setOnCreateContextMenuListener(OnlineListViewHolder.this);
        }

        @Override
        public void onClick(View v) {
            v.showContextMenu();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int pos = getAdapterPosition();
            if(pos == -1) {
                Toast.makeText(mContext, "Player data is updating, please try again shortly", Toast.LENGTH_LONG).show();
                return false;
            }
            String name = mOnlineList.get(pos).getName();
            switch(item.getItemId()) {
                case 0: // ADD VIP
                    OnlineListFragment.addVip(name);
                    Toast.makeText(mContext, "ADDED VIP: "+name, Toast.LENGTH_SHORT).show();
                    return true;
                case 1: // SEARCH PLAYER
                    SearchCharacterFragment fragment = SearchCharacterFragment.newInstance();
                    fragment.setName(name);
                    ((MainActivity) mContext).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                    Toast.makeText(mContext, "SEARCH PLAYER: "+name, Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, 0, 0,"ADD VIP")
                    .setOnMenuItemClickListener(OnlineListViewHolder.this);

            menu.add(1, 1, 1,"SEARCH PLAYER")
                    .setOnMenuItemClickListener(OnlineListViewHolder.this);

            MenuCompat.setGroupDividerEnabled(menu, true);
        }
    }
}
