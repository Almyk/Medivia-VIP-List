package com.example.almyk.mediviaviplist.Database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface PlayerDao {
    @Query("SELECT * FROM player")
    List<PlayerEntity> getAll();

    @Insert
    void insert(PlayerEntity player);

    @Delete
    void delete(PlayerEntity player);
}
