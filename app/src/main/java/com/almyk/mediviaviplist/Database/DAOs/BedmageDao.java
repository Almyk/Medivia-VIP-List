package com.almyk.mediviaviplist.Database.DAOs;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.almyk.mediviaviplist.Database.Entities.BedmageEntity;

import java.util.List;

@Dao
public interface BedmageDao {
    @Query("SELECT * FROM bedmage_table ORDER BY name ASC")
    LiveData<List<BedmageEntity>> getAll();

    @Query("SELECT * FROM bedmage_table WHERE name = :name")
    BedmageEntity getBedmage(String name);

    @Insert
    void insert(BedmageEntity bedmage);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(BedmageEntity bedmage);

    @Delete
    void delete(BedmageEntity bedmage);
}
