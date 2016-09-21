package com.github.denisura.kandone.data.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.denisura.kandone.data.database.TaskColumns;

public class AppDatabase extends SQLiteOpenHelper {
  private static final int DATABASE_VERSION = 1;

  public static final String TASKS = "CREATE TABLE tasks ("
   + TaskColumns._ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE AUTOINCREMENT,"
   + TaskColumns.TASK + " TEXT NOT NULL,"
   + TaskColumns.NOTES + " TEXT NOT NULL,"
   + TaskColumns.DUEDATE + " INTEGER NOT NULL,"
   + TaskColumns.PRIORITY + " INTEGER NOT NULL,"
   + TaskColumns.DONEDATE + " INTEGER)";

  private static volatile AppDatabase instance;

  private Context context;

  private AppDatabase(Context context) {
    super(context.getApplicationContext(), "appDatabase.db", null, DATABASE_VERSION);
    this.context = context.getApplicationContext();
  }

  public static AppDatabase getInstance(Context context) {
    if (instance == null) {
      synchronized (AppDatabase.class) {
        if (instance == null) {
          instance = new AppDatabase(context);
        }
      }
    }
    return instance;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(TASKS);
    com.github.denisura.kandone.data.database.AppDatabase.onCreate(context, db);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    com.github.denisura.kandone.data.database.AppDatabase.onUpgrade(context, db, oldVersion, newVersion);
  }
}
