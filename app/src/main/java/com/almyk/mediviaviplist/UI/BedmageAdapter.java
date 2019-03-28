package com.almyk.mediviaviplist.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almyk.mediviaviplist.R;

import java.util.ArrayList;
import java.util.List;

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
                .inflate(R.layout.two_horizontal_textviews, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BedmageAdapter.MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mBedmageList.size();
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
