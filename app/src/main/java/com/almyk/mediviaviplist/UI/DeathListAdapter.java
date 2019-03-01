package com.almyk.mediviaviplist.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almyk.mediviaviplist.Database.DeathEntity;
import com.almyk.mediviaviplist.R;

import java.util.List;

public class DeathListAdapter extends RecyclerView.Adapter<DeathListAdapter.MyViewHolder> {
    private static final String TAG = DeathListAdapter.class.getSimpleName();
    private Context mContext;
    private static List<DeathEntity> mDeaths;

    public DeathListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public DeathListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.two_horizontal_textviews, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeathListAdapter.MyViewHolder holder, int i) {
        String date = mDeaths.get(i).getDate();
        String details = mDeaths.get(i).getDetails();
        holder.mDate.setText(date);
        holder.mDetails.setText(details);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "mDeaths size: "+mDeaths.size());
        return mDeaths.size();
    }

    public void setDeaths(List<DeathEntity> deaths) {
        this.mDeaths = deaths;
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
