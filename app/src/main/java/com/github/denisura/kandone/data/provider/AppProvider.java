package com.github.denisura.kandone.data.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.github.denisura.kandone.data.database.TaskColumns;

import net.simonvt.schematic.utils.SelectionBuilder;

import java.util.ArrayList;

import timber.log.Timber;

public class AppProvider extends ContentProvider {
    public static final String AUTHORITY = "com.github.denisura.kandone";

    private static final int TASKS_CONTENT_URI = 0;

    private static final int TASKS_TASK_ID = 1;

    private static final int PRIORITY_UP_CONTENT_URI = 2;

    private static final int PRIORITY_DOWN_CONTENT_URI = 3;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, "tasks", TASKS_CONTENT_URI);
        MATCHER.addURI(AUTHORITY, "tasks/#", TASKS_TASK_ID);
        MATCHER.addURI(AUTHORITY, "priority/up", PRIORITY_UP_CONTENT_URI);
        MATCHER.addURI(AUTHORITY, "priority/down", PRIORITY_DOWN_CONTENT_URI);
    }

    private SQLiteOpenHelper database;

    @Override
    public boolean onCreate() {
        database = AppDatabase.getInstance(getContext());
        return true;
    }

    private SelectionBuilder getBuilder(String table) {
        SelectionBuilder builder = new SelectionBuilder();
        return builder;
    }

    private long[] insertValues(SQLiteDatabase db, String table, ContentValues[] values) {
        long[] ids = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            ContentValues cv = values[i];
            db.insertOrThrow(table, null, cv);
        }
        return ids;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        try {
            switch (MATCHER.match(uri)) {
                case TASKS_CONTENT_URI: {
                    long[] ids = insertValues(db, "tasks", values);
                    getContext().getContentResolver().notifyChange(uri, null);
                    break;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return values.length;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> ops) throws OperationApplicationException {
        ContentProviderResult[] results;
        final SQLiteDatabase db = database.getWritableDatabase();
        db.beginTransaction();
        try {
            results = super.applyBatch(ops);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return results;
    }

    @Override
    public String getType(Uri uri) {
        switch (MATCHER.match(uri)) {
            case TASKS_CONTENT_URI: {
                return "vnd.android.cursor.dir/com.github.denisura.kandone/tasks";
            }
            case TASKS_TASK_ID: {
                return "vnd.android.cursor.item/com.github.denisura.kandone/tasks";
            }
            case PRIORITY_UP_CONTENT_URI: {
                return "vnd.android.cursor.dir/com.github.denisura.kandone/tasks";
            }
            case PRIORITY_DOWN_CONTENT_URI: {
                return "vnd.android.cursor.dir/com.github.denisura.kandone/tasks";
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = database.getReadableDatabase();
        switch (MATCHER.match(uri)) {
            case TASKS_CONTENT_URI: {
                SelectionBuilder builder = getBuilder("Tasks");
                if (sortOrder == null) {
                    sortOrder = "priority";
                }
                String table = "tasks";
                final String groupBy = null;
                final String having = null;
                final String limit = null;
                Cursor cursor = builder.table(table)
                        .where(selection, selectionArgs)
                        .query(db, projection, groupBy, having, sortOrder, limit);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = database.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case TASKS_CONTENT_URI: {
                final long id = db.insertOrThrow("tasks", null, values);
                Uri[] notifyUris = com.github.denisura.kandone.data.database.AppProvider.Tasks.onInsert(values);
                for (Uri notifyUri : notifyUris) {
                    getContext().getContentResolver().notifyChange(notifyUri, null);
                }
                return ContentUris.withAppendedId(uri, id);
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        final SQLiteDatabase db = database.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case TASKS_TASK_ID: {
                SelectionBuilder builder = getBuilder("Tasks");
                builder.where("_id=?", uri.getPathSegments().get(1));
                builder.where(where, whereArgs);
                Uri[] notifyUris = com.github.denisura.kandone.data.database.AppProvider.Tasks.onUpdate(getContext(), uri, builder.getSelection(), builder.getSelectionArgs());
                final int count = builder.table("tasks")
                        .update(db, values);
                if (count > 0) {
                    for (Uri notifyUri : notifyUris) {
                        getContext().getContentResolver().notifyChange(notifyUri, null);
                    }
                }
                return count;
            }
            case PRIORITY_UP_CONTENT_URI: {
                Timber.d("update PRIORITY_UP_CONTENT_URI");
                SelectionBuilder builder = getBuilder("Priority");
                builder.where(where, whereArgs);
//        db.update("tasks", values, builder.getSelection(), builder.getSelectionArgs());
////
////        final int count = builder.table("tasks")
////            .update(db, values);

                String query = "UPDATE tasks SET " + TaskColumns.PRIORITY + " = " + TaskColumns.PRIORITY + "+1 ";

                if (builder.getSelection().length() > 0) {
                    query += " WHERE " + builder.getSelection();
                }
                Timber.d("PRIORITY_UP_CONTENT_URI query %s", query);

                db.execSQL(query, builder.getSelectionArgs());
                Uri[] notifyUris = com.github.denisura.kandone.data.database.AppProvider.Tasks.onInsert(values);
                for (Uri notifyUri : notifyUris) {
                    getContext().getContentResolver().notifyChange(notifyUri, null);
                }
                return 1;
            }
            case PRIORITY_DOWN_CONTENT_URI: {
                Timber.d("update PRIORITY_DOWN_CONTENT_URI");
                SelectionBuilder builder = getBuilder("Priority");
                builder.where(where, whereArgs);

                String query = "UPDATE tasks SET " + TaskColumns.PRIORITY + " = " + TaskColumns.PRIORITY + "-1 ";
                if (builder.getSelection().length() > 0) {
                    query += " WHERE " + builder.getSelection();
                }
                Timber.d("PRIORITY_DOWN_CONTENT_URI query %s", query);

                db.execSQL(query, builder.getSelectionArgs());
                Uri[] notifyUris = com.github.denisura.kandone.data.database.AppProvider.Tasks.onInsert(values);
                for (Uri notifyUri : notifyUris) {
                    getContext().getContentResolver().notifyChange(notifyUri, null);
                }
                return 1;
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        final SQLiteDatabase db = database.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case TASKS_CONTENT_URI: {
                SelectionBuilder builder = getBuilder("Tasks");
                builder.where(where, whereArgs);
                final int count = builder
                        .table("tasks")
                        .delete(db);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            }
            case TASKS_TASK_ID: {
                SelectionBuilder builder = getBuilder("Tasks");
                builder.where("_id=?", uri.getPathSegments().get(1));
                builder.where(where, whereArgs);
                Uri[] notifyUris = com.github.denisura.kandone.data.database.AppProvider.Tasks.onDelete(getContext(), uri);
                final int count = builder
                        .table("tasks")
                        .delete(db);
                for (Uri notifyUri : notifyUris) {
                    getContext().getContentResolver().notifyChange(notifyUri, null);
                }
                return count;
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
    }
}
