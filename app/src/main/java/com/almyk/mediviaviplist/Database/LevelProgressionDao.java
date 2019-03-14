package com.almyk.mediviaviplist.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface LevelProgressionDao {
    @Query("SELECT :day FROM level_progression WHERE name = :name")
    String getPlayerProgression(String name, String day);

    @Query("SELECT * FROM level_progression WHERE name = :name")
    LevelProgressionEntity getPlayerProgressionRow(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LevelProgressionEntity progression);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(LevelProgressionEntity progression);

    @Delete
    void delete(LevelProgressionEntity progression);
}
