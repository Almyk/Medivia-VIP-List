package com.example.almyk.mediviaviplist.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;



@Dao
public interface PlayerDao {
    @Query("SELECT * FROM player")
    List<PlayerEntity> getAll();

    @Insert
    void insert(PlayerEntity player);

    @Delete
    void delete(PlayerEntity player);

    @Update
    void update(PlayerEntity player);
}
