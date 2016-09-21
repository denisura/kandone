package com.github.denisura.kandone.data.provider.values;

import android.content.ContentValues;

import com.github.denisura.kandone.data.database.TaskColumns;

public class TasksValuesBuilder {
  ContentValues values = new ContentValues();

  public TasksValuesBuilder Id(Integer value) {
    values.put(TaskColumns._ID, value);
    return this;
  }

  public TasksValuesBuilder Id(Long value) {
    values.put(TaskColumns._ID, value);
    return this;
  }

  public TasksValuesBuilder task(String value) {
    values.put(TaskColumns.TASK, value);
    return this;
  }

  public TasksValuesBuilder notes(String value) {
    values.put(TaskColumns.NOTES, value);
    return this;
  }

  public TasksValuesBuilder duedate(Integer value) {
    values.put(TaskColumns.DUEDATE, value);
    return this;
  }

  public TasksValuesBuilder duedate(Long value) {
    values.put(TaskColumns.DUEDATE, value);
    return this;
  }

  public TasksValuesBuilder priority(Integer value) {
    values.put(TaskColumns.PRIORITY, value);
    return this;
  }

  public TasksValuesBuilder priority(Long value) {
    values.put(TaskColumns.PRIORITY, value);
    return this;
  }

  public TasksValuesBuilder donedate(Integer value) {
    values.put(TaskColumns.DONEDATE, value);
    return this;
  }

  public TasksValuesBuilder donedate(Long value) {
    values.put(TaskColumns.DONEDATE, value);
    return this;
  }

  public ContentValues values() {
    return values;
  }
}
