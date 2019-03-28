package com.almyk.mediviaviplist.Database.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.almyk.mediviaviplist.Database.Entities.DeathEntity;

import java.util.List;

@Dao
public interface DeathDao {
    @Query("SELECT * FROM death_table WHERE player_id = :playerId ORDER BY `key` DESC")
    List<DeathEntity> getDeaths(String playerId);

    @Insert
    void insertDeath(DeathEntity death);

    @Delete
    void deleteDeath(DeathEntity death);

    @Query("DELETE FROM death_table")
    void nukeTable();
}
