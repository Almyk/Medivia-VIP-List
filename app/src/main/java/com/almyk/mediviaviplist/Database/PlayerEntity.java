package com.almyk.mediviaviplist.Database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "vip_list")
public class PlayerEntity {
    @PrimaryKey
    @ColumnInfo(name = "player_name")
    @NonNull
    private String name;

    @ColumnInfo(name = "previous_name")
    private String previousName;

    @ColumnInfo(name = "server_name")
    private String server;

    @ColumnInfo(name = "level")
    private String level;

    @ColumnInfo(name = "vocation")
    private String vocation;

    @ColumnInfo(name = "online")
    private boolean online;

    @ColumnInfo(name = "residence")
    private String residence;

    @ColumnInfo(name = "guild")
    private String guild;

    @ColumnInfo(name = "house")
    private String house;

    @ColumnInfo(name = "sex")
    private String sex;

    @ColumnInfo(name = "account_status")
    private String accountStatus;

    @ColumnInfo(name = "comment")
    private String comment;

    @ColumnInfo(name = "last_login")
    private String lastLogin;

    @Ignore
    public PlayerEntity() {
    }

    @Ignore
    public PlayerEntity(String name, String server, String level, String vocation) {
        this.name = name;
        this.server = server;
        this.level = level;
        this.vocation = vocation;
    }

    public PlayerEntity(@NonNull String name, String previousName, String server, String level, String vocation, boolean online, String residence, String guild, String house, String sex, String accountStatus, String comment, String lastLogin) {
        this.name = name;
        this.previousName = previousName;
        this.server = server;
        this.level = level;
        this.vocation = vocation;
        this.online = online;
        this.residence = residence;
        this.guild = guild;
        this.house = house;
        this.sex = sex;
        this.accountStatus = accountStatus;
        this.comment = comment;
        this.lastLogin = lastLogin;
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

    public String getPreviousName() {
        return previousName;
    }

    public void setPreviousName(String previousName) {
        this.previousName = previousName;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getGuild() {
        return guild;
    }

    public void setGuild(String guild) {
        this.guild = guild;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
}
