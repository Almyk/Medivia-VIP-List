package com.example.almyk.mediviaviplist.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "player")
public class PlayerEntity {
    @PrimaryKey
    @ColumnInfo(name = "player_name")
    public String name;

    @ColumnInfo(name = "server_name")
    public String server;

    @ColumnInfo(name = "level")
    public String level;

    @ColumnInfo(name = "vocation")
    public String vocation;

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
    }

    public PlayerEntity(String name, String server, String level, String vocation) {
        this.name = name;
        this.server = server;
        this.level = level;
        this.vocation = vocation;
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
