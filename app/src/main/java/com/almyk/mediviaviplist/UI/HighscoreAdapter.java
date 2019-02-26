package com.almyk.mediviaviplist.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.almyk.mediviaviplist.Database.HighscoreEntity;
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


    public class HighscoreViewHolder extends RecyclerView.ViewHolder {
        private TextView mRank;
        private TextView mName;
        private TextView mValue;


        public HighscoreViewHolder(@NonNull View itemView) {
            super(itemView);

            mRank = itemView.findViewById(R.id.tv_rank);
            mName = itemView.findViewById(R.id.tv_name);
            mValue = itemView.findViewById(R.id.tv_value);
        }
    }
}
