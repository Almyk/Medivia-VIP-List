package com.almyk.mediviaviplist.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almyk.mediviaviplist.Database.Entities.BedmageEntity;
import com.almyk.mediviaviplist.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BedmageAdapter extends RecyclerView.Adapter<BedmageAdapter.MyViewHolder> {
    private Context mContext;
    private static List<BedmageEntity> mBedmageList = new ArrayList<>();

    public BedmageAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public BedmageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.bedmage_entry, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BedmageAdapter.MyViewHolder viewHolder, int i) {
        BedmageEntity bedmage = mBedmageList.get(i);


        // find out remaining time
        Date date = new Date();
        long minutesRemaining;
        long remainingTimeMilli = bedmage.getTimeLeft();
        if (remainingTimeMilli > 0) {
            minutesRemaining = TimeUnit.MILLISECONDS.toMinutes(remainingTimeMilli);
        } else if (remainingTimeMilli == 0){
            minutesRemaining = 0;
        } else {
            minutesRemaining = -1;
        }

        viewHolder.left.setText(bedmage.getName());
        if (minutesRemaining > 0) {
            viewHolder.right.setText("" + minutesRemaining + " min");
        } else if (minutesRemaining == 0) {
            viewHolder.right.setText("Due");
        } else {
            viewHolder.right.setText("Online");
        }
    }

    @Override
    public int getItemCount() {
        return mBedmageList.size();
    }

    public void setBedmages(List<BedmageEntity> bedmageEntities) {
        mBedmageList = bedmageEntities;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView left;
        private TextView right;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            left = itemView.findViewById(R.id.tv_left);
            right = itemView.findViewById(R.id.tv_right);
        }
    }
}
