package com.almyk.mediviaviplist.Database.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.almyk.mediviaviplist.Database.Entities.TaskEntity;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task_table WHERE player_id = :playerId ORDER BY `key` DESC")
    List<TaskEntity> getTasks(String playerId);

    @Insert
    void insertTask(TaskEntity task);

    @Delete
    void deleteTask(TaskEntity task);

    @Query("DELETE FROM task_table")
    void nukeTable();
}
