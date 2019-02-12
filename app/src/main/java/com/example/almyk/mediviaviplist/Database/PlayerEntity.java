package com.example.almyk.mediviaviplist.Database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "player")
public class PlayerEntity {
    @PrimaryKey
    @ColumnInfo(name = "player_name")
    @NonNull
    public String name;

    @ColumnInfo(name = "server_name")
    public String server;

    @ColumnInfo(name = "level")
    public String level;

    @ColumnInfo(name = "vocation")
    public String vocation;

    public boolean online;

    @Ignore
    public PlayerEntity() {

    }

    // TODO : this should not be needed
    @Ignore
    public PlayerEntity(String name) {
        this.name = name;
        this.server = "?";
        this.level = "?";
        this.vocation = "?";
        this.online = false;
    }

    public PlayerEntity(String name, String server, String level, String vocation) {
        this.name = name;
        this.server = server;
        this.level = level;
        this.vocation = vocation;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getVocation() {
        return vocation;
    }

    public void setVocation(String vocation) {
        this.vocation = vocation;
    }
}
