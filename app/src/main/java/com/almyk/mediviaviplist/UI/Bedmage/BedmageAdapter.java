package com.almyk.mediviaviplist.UI.Bedmage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.almyk.mediviaviplist.Database.Entities.BedmageEntity;
import com.almyk.mediviaviplist.R;
import com.almyk.mediviaviplist.UI.MainActivity;

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

    public List<BedmageEntity> getBedmages() {
        return mBedmageList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        private TextView left;
        private TextView right;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            left = itemView.findViewById(R.id.tv_left);
            right = itemView.findViewById(R.id.tv_right);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            v.showContextMenu();
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, 0, 0,"Change Timer").setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            int pos = getAdapterPosition();

            if (pos < 0) { // Error when getting position
                Toast.makeText(mContext, "List is updating, please try again", Toast.LENGTH_SHORT).show();
                return true;
            }
            switch (id) {
                case 0: // Change Timer
                    BedmageEntity bedmage = mBedmageList.get(pos);
                    DialogFragment dialog = new BedmageDialog();
                    ((BedmageDialog) dialog).editBedmage(bedmage.getName(), bedmage.getTimer());
                    dialog.show(((MainActivity) mContext).getSupportFragmentManager(), "Add Bedmage");
                    return true;
                default:
                    return false;
            }
        }
    }
}
