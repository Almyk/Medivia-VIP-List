package com.almyk.mediviaviplist.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.almyk.mediviaviplist.Database.TaskEntity;
import com.almyk.mediviaviplist.R;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {
    private Context mContext;
    private static List<TaskEntity> mTasks = new ArrayList<>();

    public TaskListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TaskListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.two_horizontal_textviews, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListAdapter.MyViewHolder holder, int i) {
        TaskEntity task = mTasks.get(i);
        String monster = task.getMonster();
        String details = task.getDetails();

        holder.mMonster.setText(monster);
        holder.mDetails.setText(details);
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.mTasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mMonster;
        private TextView mDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mMonster = itemView.findViewById(R.id.tv_left);
            mDetails = itemView.findViewById(R.id.tv_right);
        }
    }
}
