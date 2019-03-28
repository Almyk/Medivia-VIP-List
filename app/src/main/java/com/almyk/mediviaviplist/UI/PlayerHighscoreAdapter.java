package com.almyk.mediviaviplist.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almyk.mediviaviplist.Database.Entities.HighscoreEntity;
import com.almyk.mediviaviplist.R;

import java.util.ArrayList;
import java.util.List;

public class PlayerHighscoreAdapter extends RecyclerView.Adapter<PlayerHighscoreAdapter.MyViewHolder> {
    private Context mContext;
    private List<HighscoreEntity> mHighscores = new ArrayList<>();

    public PlayerHighscoreAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public PlayerHighscoreAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.two_horizontal_textviews, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerHighscoreAdapter.MyViewHolder holder, int i) {
        HighscoreEntity highscore = mHighscores.get(i);
        String skill = highscore.getSkill();
        int rank = highscore.getRank();
        String value = highscore.getValue() + " (#" + rank + ")";

        holder.mSkill.setText(skill);
        holder.mValue.setText(value);
    }

    @Override
    public int getItemCount() {
        return mHighscores.size();
    }

    public void setHighscores(List<HighscoreEntity> highscores) {
        this.mHighscores = highscores;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mSkill;
        private TextView mValue;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.mSkill = itemView.findViewById(R.id.tv_left);
            this.mValue = itemView.findViewById(R.id.tv_right);

            mSkill.setGravity(Gravity.CENTER);
            mValue.setGravity(Gravity.CENTER);
        }
    }
}
