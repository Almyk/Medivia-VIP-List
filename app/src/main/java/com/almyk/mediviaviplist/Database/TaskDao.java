package com.almyk.mediviaviplist.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task_table WHERE player_id = :playerId ORDER BY `key` DESC")
    LiveData<List<TaskEntity>> getTasks(String playerId);

    @Insert
    void insertTask(TaskEntity task);

    @Delete
    void deleteTask(TaskEntity task);
}
