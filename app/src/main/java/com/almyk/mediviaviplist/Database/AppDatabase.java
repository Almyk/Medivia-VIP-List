package com.almyk.mediviaviplist.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;



@Database(entities = {PlayerEntity.class}, version = 2, exportSchema = false)
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
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract PlayerDao playerDao();

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
}
