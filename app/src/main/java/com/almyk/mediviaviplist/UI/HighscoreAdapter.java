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

import com.almyk.mediviaviplist.Database.Entities.HighscoreEntity;
import com.almyk.mediviaviplist.R;

import java.util.List;

public class HighscoreAdapter extends RecyclerView.Adapter<HighscoreAdapter.HighscoreViewHolder> {
    private Context mContext;

    private static List<HighscoreEntity> mHighscores;

    public HighscoreAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public HighscoreAdapter.HighscoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.highscore_entry, viewGroup, false);
        return new HighscoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HighscoreAdapter.HighscoreViewHolder holder, int i) {
        HighscoreEntity entry = mHighscores.get(i);

        int rank = entry.getRank();
        holder.mRank.setText(""+rank);
        holder.mName.setText(entry.getName());
        holder.mValue.setText(entry.getValue());

    }

    @Override
    public int getItemCount() {
        return mHighscores.size();
    }

    public void setEntries(List<HighscoreEntity> highscores) {
        this.mHighscores = highscores;
        notifyDataSetChanged();
    }


    public class HighscoreViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,
            View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener {

        private TextView mRank;
        private TextView mName;
        private TextView mValue;


        public HighscoreViewHolder(@NonNull View itemView) {
            super(itemView);

            mRank = itemView.findViewById(R.id.tv_rank);
            mName = itemView.findViewById(R.id.tv_name);
            mValue = itemView.findViewById(R.id.tv_value);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int pos = getAdapterPosition();
            if(pos == -1) {
                Toast.makeText(mContext, "Player data is updating, please try again shortly", Toast.LENGTH_LONG).show();
                return false;
            }
            String name = mHighscores.get(pos).getName();
            switch(item.getItemId()) {
                case 0: // ADD VIP
                    HighscoreFragment.addVip(name);
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
        public void onClick(View v) {
            v.showContextMenu();
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, 0, 0,"ADD VIP")
                    .setOnMenuItemClickListener(HighscoreViewHolder.this);

            menu.add(1, 1, 1,"SEARCH PLAYER")
                    .setOnMenuItemClickListener(HighscoreViewHolder.this);

            MenuCompat.setGroupDividerEnabled(menu, true);
        }
    }
}
