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

    private String logoutTime;

    @Ignore
    public BedmageEntity() {
    }

    public BedmageEntity(@NonNull String name, String logoutTime) {
        this.name = name;
        this.logoutTime = logoutTime;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(String logoutTime) {
        this.logoutTime = logoutTime;
    }
}
