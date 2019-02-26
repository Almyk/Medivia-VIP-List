package com.almyk.mediviaviplist.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface HighscoreDao {
    @Query("SELECT * FROM highscore_list WHERE server = :server AND skill = :skill AND :voc ORDER BY rank DESC")
    LiveData<List<HighscoreEntity>> getServerBySkillAndVoc(String server, String skill, String voc);

    @Query("SELECT * FROM highscore_list WHERE server = :server AND skill = :skill ORDER BY rank DESC")
    LiveData<List<HighscoreEntity>> getServerBySkill(String server, String skill);

    @Query("SELECT * FROM highscore_list WHERE server_skill_voc_rank_key = :serverSkillVocRank")
    HighscoreEntity getEntryByKey(String serverSkillVocRank);

    @Query("SELECT * FROM highscore_list WHERE name = :name")
    List<HighscoreEntity> getEntryByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HighscoreEntity highscore);

    @Delete
    void delete(HighscoreEntity highscore);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(HighscoreEntity highscore);
}
