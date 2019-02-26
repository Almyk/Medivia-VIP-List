package com.almyk.mediviaviplist.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "highscore_list")
public class HighscoreEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "server_skill_voc_rank_key")
    private String serverSkillVocRankKey;

    private String server;
    private String skill;
    private String rank;
    private String name;

    private String vocation;
    private String value;

    @Ignore
    public HighscoreEntity() {}

    public HighscoreEntity(String server, String skill, String rank, String name, String value, String vocation) {
        this.serverSkillVocRankKey = server+skill+vocation+rank;
        this.server = server;
        this.skill = skill;
        this.rank = rank;
        this.name = name;
        this.value = value;
        this.vocation = vocation;
    }

    @NonNull
    public String getServerSkillVocRankKey() {
        return serverSkillVocRankKey;
    }

    public void setServerSkillVocRankKey(@NonNull String serverSkillVocRankKey) {
        this.serverSkillVocRankKey = serverSkillVocRankKey;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getVocation() {
        return vocation;
    }

    public void setVocation(String vocation) {
        this.vocation = vocation;
    }
}
