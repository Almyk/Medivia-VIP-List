package com.almyk.mediviaviplist.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface KillDao {
    @Query("SELECT * FROM kill_table WHERE player_id = :playerId ORDER BY `key` DESC")
    List<KillEntity> getKills(String playerId);

    @Insert
    void insertKill(KillEntity kill);

    @Delete
    void deleteKill(KillEntity kill);
}
