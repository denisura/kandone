package com.github.denisura.kandone.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

@Database(version = AppDatabase.VERSION,
        packageName = AppContract.PROVIDER_NAMESPACE)

public final class AppDatabase {
    private AppDatabase() {
    }
    public static final int VERSION = 1;

    public static class Tables {
        @Table(TaskColumns.class)
        public static final String TASKS = AppContract.TaskEntry.TABLE_NAME;
    }

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.TASKS);
       onCreate(context, db);
    }
}