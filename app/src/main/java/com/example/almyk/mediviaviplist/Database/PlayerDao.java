package com.example.almyk.mediviaviplist.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;



@Dao
public interface PlayerDao {
    @Query("SELECT * FROM player")
    LiveData<List<PlayerEntity>> getAll();

    @Query("SELECT * FROM player where player_name = :name")
    PlayerEntity getPlayer(String name);

    @Insert
    void insert(PlayerEntity player);

    @Delete
    void delete(PlayerEntity player);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(PlayerEntity player);
}
