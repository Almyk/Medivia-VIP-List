package com.almyk.mediviaviplist.Model;

import com.almyk.mediviaviplist.Database.DeathEntity;
import com.almyk.mediviaviplist.Database.HighscoreEntity;
import com.almyk.mediviaviplist.Database.KillEntity;
import com.almyk.mediviaviplist.Database.PlayerEntity;
import com.almyk.mediviaviplist.Database.TaskEntity;

import java.util.List;

public class Player {
    private PlayerEntity playerEntity;
    private List<DeathEntity> deaths;
    private List<KillEntity> kills;
    private List<TaskEntity> tasks;
    private List<HighscoreEntity> highscoresAll;
    private List<HighscoreEntity> highscoresVoc;

    public Player() {
    }

    public Player(PlayerEntity playerEntity, List<DeathEntity> deaths, List<KillEntity> kills, List<TaskEntity> tasks) {
        this.playerEntity = playerEntity;
        this.deaths = deaths;
        this.kills = kills;
        this.tasks = tasks;
    }

    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }

    public void setPlayerEntity(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    public List<DeathEntity> getDeaths() {
        return deaths;
    }

    public void setDeaths(List<DeathEntity> deaths) {
        this.deaths = deaths;
    }

    public List<KillEntity> getKills() {
        return kills;
    }

    public void setKills(List<KillEntity> kills) {
        this.kills = kills;
    }

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    public String getPlayerName() {
        return this.playerEntity.getName();
    }

    public List<HighscoreEntity> getHighscoresAll() {
        return highscoresAll;
    }

    public void setHighscoresAll(List<HighscoreEntity> highscoresAll) {
        this.highscoresAll = highscoresAll;
    }

    public List<HighscoreEntity> getHighscoresVoc() {
        return highscoresVoc;
    }

    public void setHighscoresVoc(List<HighscoreEntity> highscoresVoc) {
        this.highscoresVoc = highscoresVoc;
    }

    public String getVoc() {
        return this.playerEntity.getVocation();
    }
}
