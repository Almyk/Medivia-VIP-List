package com.almyk.mediviaviplist.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "bedmage_table")
public class BedmageEntity {
    @NonNull
    @PrimaryKey
    private String name;

    private long logoutTime;
    private long timer;
    private long timeLeft;

    @Ignore
    public BedmageEntity() {
    }

    public BedmageEntity(@NonNull String name, long logoutTime, long timer, long timeLeft) {
        this.name = name;
        this.logoutTime = logoutTime;
        this.timer = timer;
        this.timeLeft = timeLeft;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public long getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(long logoutTime) {
        this.logoutTime = logoutTime;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }
}
