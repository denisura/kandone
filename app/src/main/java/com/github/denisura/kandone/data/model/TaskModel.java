package com.github.denisura.kandone.data.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.github.denisura.kandone.data.database.TaskColumns;

import org.joda.time.LocalDate;

import java.io.Serializable;

public class TaskModel implements Serializable {

    private long id;
    private String task;
    private String notes;
    private int priority;
    private LocalDate dueDate;
    private LocalDate doneDate;

    public TaskModel() {

    }

    public TaskModel(Cursor cursor) {

        int idx_id = cursor.getColumnIndex(TaskColumns._ID);
        int idx_task = cursor.getColumnIndex(TaskColumns.TASK);
        int idx_notes = cursor.getColumnIndex(TaskColumns.NOTES);
        int idx_priority = cursor.getColumnIndex(TaskColumns.PRIORITY);
        int idx_dueDate = cursor.getColumnIndex(TaskColumns.DUEDATE);
        int idx_doneDate = cursor.getColumnIndex(TaskColumns.DONEDATE);

        setId(cursor.getLong(idx_id));
        setTask(cursor.getString(idx_task));
        setNotes(cursor.getString(idx_notes));
        setPriority(cursor.getInt(idx_priority));
        setDueDate(cursor.getLong(idx_dueDate));
        setDoneDate(cursor.getLong(idx_doneDate));
    }

    public ContentValues getContentValues() {
        // Gets a new ContentValues object
        ContentValues v = new ContentValues();

        if (getId() > 0) {
            v.put(TaskColumns._ID, getId());
        }

        v.put(TaskColumns.TASK, getTask());
        v.put(TaskColumns.NOTES, getNotes());
        v.put(TaskColumns.PRIORITY, getPriority());

        v.put(TaskColumns.DUEDATE, getDueDate().toDateTimeAtStartOfDay().getMillis());

        if (getDoneDate() != null) {
            v.put(TaskColumns.DONEDATE, getDoneDate().toDateTimeAtStartOfDay().getMillis());
        }
        return v;
    }


    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

//    public int getDueDate() {
//
//
//        String time = "";
//
////        try {
////            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
////            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
////            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
////            time= sdfTime.format(sdf.parse(getDate()));
////        } catch (ParseException e) {
////            e.printStackTrace();
////        }
//////        Log.d("denisura", "Z date: " + getDate() + " time: " + time);
////        return time;
//
//
//        return dueDate;
//    }

//    public void setDueDate(int dueDate) {
//        this.dueDate = dueDate;
//    }
//
//    public int getDoneDate() {
//        return doneDate;
//    }

//    public void setDoneDate(int doneDate) {
//        this.doneDate = doneDate;
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDueDate(long timestamp) {
        this.dueDate = new LocalDate(timestamp);
    }

    public void setDueDate(LocalDate date) {
        this.dueDate = date;
    }

    public void setDoneDate(long timestamp) {
        if (timestamp > 0) {
            this.doneDate = new LocalDate(timestamp);
        }
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getDoneDate() {
        return doneDate;
    }
}
