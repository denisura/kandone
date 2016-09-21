package com.github.denisura.kandone.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.NotifyDelete;
import net.simonvt.schematic.annotation.NotifyInsert;
import net.simonvt.schematic.annotation.NotifyUpdate;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = AppContract.CONTENT_AUTHORITY,
        database = AppDatabase.class,
        packageName = AppContract.PROVIDER_NAMESPACE)
public class AppProvider {

    private AppProvider() {
    }

    static final Uri BASE_CONTENT_URI = AppContract.BASE_CONTENT_URI;

    interface Path {
        String TASKS = AppContract.PATH_TASKS;
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = AppContract.TaskEntry.TABLE_NAME)
    public static class Tasks {
        @ContentUri(
                path = Path.TASKS,
                allowUpdate = false,
                type = AppContract.TaskEntry.CONTENT_TYPE,
                defaultSort = TaskColumns.PRIORITY)
        public static final Uri CONTENT_URI = buildUri(Path.TASKS);

        @InexactContentUri(
                path = Path.TASKS + "/#",
                name = "TASK_ID",
                allowInsert = false,
                allowQuery = false,
                type = AppContract.TaskEntry.CONTENT_ITEM_TYPE,
                whereColumn = AppContract.TaskEntry._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.TASKS, String.valueOf(id));
        }

        @NotifyInsert(paths = Path.TASKS)
        public static Uri[] onInsert(ContentValues values) {
            return new Uri[]{
                    AppProvider.Tasks.CONTENT_URI
            };
        }

        @NotifyUpdate(paths = Path.TASKS + "/#")
        public static Uri[] onUpdate(Context context, Uri uri, String where, String[] whereArgs) {
            final long id = Long.valueOf(uri.getPathSegments().get(1));
            return new Uri[]{
                    Tasks.withId(id)
            };
        }

        @NotifyDelete(paths = Path.TASKS + "/#")
        public static Uri[] onDelete(Context context, Uri uri) {
            final long id = Long.valueOf(uri.getPathSegments().get(1));
            return new Uri[]{
                    Tasks.withId(id), AppProvider.Tasks.CONTENT_URI
            };
        }
    }

    @TableEndpoint(table = AppContract.TaskEntry.TABLE_NAME)
    public static class Priority {
        @ContentUri(
                path = AppContract.PATH_PRIORITY + "/up",
                type = AppContract.TaskEntry.CONTENT_TYPE,
                allowDelete = false,
                allowInsert = false,
                allowQuery = false,
                defaultSort = TaskColumns.PRIORITY + " DESC")
        public static Uri UP_CONTENT_URI = buildUri(AppContract.PATH_PRIORITY, "up");

        @ContentUri(
                path = AppContract.PATH_PRIORITY + "/down",
                type = AppContract.TaskEntry.CONTENT_TYPE,
                allowDelete = false,
                allowInsert = false,
                allowQuery = false,
                defaultSort = TaskColumns.PRIORITY + " DESC")
        public static Uri DOWN_CONTENT_URI = buildUri(AppContract.PATH_PRIORITY, "down");
    }
}