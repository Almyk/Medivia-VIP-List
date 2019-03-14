package com.almyk.mediviaviplist.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "level_progession")
public class LevelProgressionEntity {
    @PrimaryKey
    @NonNull
    private String name;

    // levels are stored for each day of the week
    private String one;
    private String two;
    private String three;
    private String four;
    private String five;
    private String six;
    private String seven;

    @Ignore
    public LevelProgressionEntity() {
    }

    public LevelProgressionEntity(@NonNull String name, String one, String two, String three, String four, String five, String six, String seven) {
        this.name = name;
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        this.five = five;
        this.six = six;
        this.seven = seven;
    }
}
