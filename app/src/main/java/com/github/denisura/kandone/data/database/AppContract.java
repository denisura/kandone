package com.github.denisura.kandone.data.database;

import android.content.ContentResolver;
import android.net.Uri;

public class AppContract {

    public static final String CONTENT_AUTHORITY = "com.github.denisura.kandone";
    public static final String PROVIDER_NAMESPACE = "com.github.denisura.kandone.provider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TASKS = "tasks";
    public static final String PATH_PRIORITY = "priority";


    public static final class TaskEntry implements TaskColumns {

        public static final String TABLE_NAME = "tasks";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASKS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASKS;

    }
}
