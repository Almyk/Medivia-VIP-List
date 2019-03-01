package com.almyk.mediviaviplist.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almyk.mediviaviplist.Database.KillEntity;
import com.almyk.mediviaviplist.R;

import java.util.List;

public class KillListAdapter extends RecyclerView.Adapter<KillListAdapter.MyViewHolder> {
    private Context mContext;
    private static List<KillEntity> mKills;

    public KillListAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public KillListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.two_horizontal_textviews, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KillListAdapter.MyViewHolder holder, int i) {
        KillEntity kill = mKills.get(i);
        String date = kill.getDate();
        String details = kill.getDetails();

        holder.mDate.setText(date);
        holder.mDetails.setText(details);
    }

    @Override
    public int getItemCount() {
        return mKills.size();
    }

    public void setKills(List<KillEntity> kills) {
        this.mKills = kills;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mDate;
        private TextView mDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mDate = itemView.findViewById(R.id.tv_left);
            mDetails = itemView.findViewById(R.id.tv_right);
        }
    }
}
