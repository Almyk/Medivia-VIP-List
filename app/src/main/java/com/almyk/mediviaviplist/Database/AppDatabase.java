package com.almyk.mediviaviplist.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;



@Database(entities = {PlayerEntity.class,
        HighscoreEntity.class, DeathEntity.class,
        KillEntity.class, TaskEntity.class},
        version = 8, exportSchema = true)
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
                                MIGRATION_7_8)
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
}
