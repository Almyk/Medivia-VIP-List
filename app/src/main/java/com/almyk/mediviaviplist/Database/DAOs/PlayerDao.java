package com.almyk.mediviaviplist.Database.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.almyk.mediviaviplist.Database.Entities.PlayerEntity;

import java.util.List;



@Dao
public interface PlayerDao {
    @Query("SELECT * FROM vip_list ORDER BY online DESC, player_name ASC")
    LiveData<List<PlayerEntity>> getAll();

    @Query("SELECT * FROM vip_list where player_name = :name")
    PlayerEntity getPlayer(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlayerEntity player);

    @Delete
    void delete(PlayerEntity player);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(PlayerEntity player);
}
