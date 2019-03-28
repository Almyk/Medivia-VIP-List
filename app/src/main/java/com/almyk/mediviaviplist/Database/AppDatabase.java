package com.almyk.mediviaviplist.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.almyk.mediviaviplist.Database.DAOs.BedmageDao;
import com.almyk.mediviaviplist.Database.DAOs.DeathDao;
import com.almyk.mediviaviplist.Database.DAOs.HighscoreDao;
import com.almyk.mediviaviplist.Database.DAOs.KillDao;
import com.almyk.mediviaviplist.Database.DAOs.LevelProgressionDao;
import com.almyk.mediviaviplist.Database.DAOs.PlayerDao;
import com.almyk.mediviaviplist.Database.DAOs.TaskDao;
import com.almyk.mediviaviplist.Database.Entities.DeathEntity;
import com.almyk.mediviaviplist.Database.Entities.HighscoreEntity;
import com.almyk.mediviaviplist.Database.Entities.KillEntity;
import com.almyk.mediviaviplist.Database.Entities.LevelProgressionEntity;
import com.almyk.mediviaviplist.Database.Entities.PlayerEntity;
import com.almyk.mediviaviplist.Database.Entities.TaskEntity;


@Database(entities = {PlayerEntity.class,
        HighscoreEntity.class, DeathEntity.class,
        KillEntity.class, TaskEntity.class, LevelProgressionEntity.class},
        version = 10, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "medivia_vip_list";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3,
                                MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7,
                                MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract PlayerDao playerDao();
    public abstract HighscoreDao highscoreDao();
    public abstract DeathDao deathDao();
    public abstract KillDao killDao();
    public abstract TaskDao taskDao();
    public abstract LevelProgressionDao progDao();
    public abstract BedmageDao bedmageDao();

    private static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE vip_list ADD COLUMN previous_name TEXT");
            database.execSQL("ALTER TABLE vip_list ADD COLUMN residence TEXT");
            database.execSQL("ALTER TABLE vip_list ADD COLUMN guild TEXT");
            database.execSQL("ALTER TABLE vip_list ADD COLUMN house TEXT");
            database.execSQL("ALTER TABLE vip_list ADD COLUMN sex TEXT");
            database.execSQL("ALTER TABLE vip_list ADD COLUMN account_status TEXT");
            database.execSQL("ALTER TABLE vip_list ADD COLUMN comment TEXT");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE vip_list ADD COLUMN last_login TEXT");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `highscore_list` (`server_skill_voc_rank_key` TEXT NOT NULL, `server` TEXT, `skill` TEXT, `rank` TEXT, `name` TEXT, `value` TEXT, `vocation` TEXT, PRIMARY KEY(`server_skill_voc_rank_key`))");
        }
    };

    private static final Migration MIGRATION_4_5 = new Migration(4,5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `highscore_list_tmp` (`server_skill_voc_rank_key` TEXT NOT NULL, `server` TEXT, `skill` TEXT, `rank` INTEGER NOT NULL, `name` TEXT, `vocation` TEXT, `value` TEXT, PRIMARY KEY(`server_skill_voc_rank_key`))");
            database.execSQL("INSERT INTO `highscore_list_tmp`(`server_skill_voc_rank_key`, `server`, `skill`, `rank`, `name`, `vocation`, `value`) " +
                    "SELECT `server_skill_voc_rank_key`, `server`, `skill`, `rank`, `name`, `vocation`, `value` " +
                    "FROM `highscore_list`");
            database.execSQL("DROP TABLE `highscore_list`");
            database.execSQL("ALTER TABLE `highscore_list_tmp` RENAME TO `highscore_list`");
        }
    };

    private static final Migration MIGRATION_5_6 = new Migration(5,6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `death_table` (`key` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `player_id` TEXT, `date` TEXT, `details` TEXT)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `kill_table` (`key` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `player_id` TEXT, `date` TEXT, `details` TEXT)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `task_table` (`key` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `player_id` TEXT, `monster` TEXT, `details` TEXT)");
        }
    };

    private static final Migration MIGRATION_6_7 = new Migration(6,7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE vip_list ADD COLUMN banishment TEXT");
        }
    };

    private static final Migration MIGRATION_7_8 = new Migration(7,8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE vip_list ADD COLUMN transfer TEXT");
        }
    };

    private static final Migration MIGRATION_8_9 = new Migration(8,9) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE vip_list ADD COLUMN note TEXT");
        }
    };

    private static final Migration MIGRATION_9_10 = new Migration(9,10) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `level_progression` (`name` TEXT NOT NULL, `one` TEXT, `two` TEXT, `three` TEXT, `four` TEXT, `five` TEXT, `six` TEXT, `seven` TEXT, PRIMARY KEY(`name`))");
            database.execSQL("INSERT INTO `level_progression`(`name`, `one`, `two`, `three`, `four`, `five`, `six`, `seven`) " +
                    "SELECT `player_name`, `level`, `level`, `level`, `level`, `level`, `level`, `level` " +
                    "FROM `vip_list`");
            database.execSQL("ALTER TABLE vip_list ADD COLUMN lv_prog TEXT");
        }
    };
}
