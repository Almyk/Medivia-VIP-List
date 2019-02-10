package com.example.almyk.mediviaviplist.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Player {
    @PrimaryKey
    @ColumnInfo(name = "player_name")
    public String name;

    @ColumnInfo(name = "server_name")
    public String server;

    @ColumnInfo(name = "level")
    public int level;

    @ColumnInfo(name = "vocation")
    public String vocation;
}
