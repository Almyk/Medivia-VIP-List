package com.almyk.mediviaviplist.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "death_table")
public class DeathEntity {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int key;

    @ColumnInfo(name = "player_id")
    private String playerID;

    private String date;
    private String details;

    @Ignore
    public DeathEntity() {
    }

    public DeathEntity(int key, String playerID, String date, String details) {
        this.key = key;
        this.playerID = playerID;
        this.date = date;
        this.details = details;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
