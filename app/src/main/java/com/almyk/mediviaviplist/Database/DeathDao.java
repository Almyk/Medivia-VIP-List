package com.almyk.mediviaviplist.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DeathDao {
    @Query("SELECT * FROM death_table WHERE player_id = :playerId ORDER BY `key` DESC")
    List<DeathEntity> getDeaths(String playerId);

    @Insert
    void insertDeath(DeathEntity death);

    @Delete
    void deleteDeath(DeathEntity death);
}
