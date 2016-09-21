package com.github.denisura.kandone.ui.task;

import com.github.denisura.kandone.data.model.TaskModel;

public interface ItemListener {

    void onEditAction(TaskModel todoItem);

    void onCheckAction(long todoItemId);

    void onUncheckAction(long todoItemId);

    void onTaskSelected(TaskModel todoItem);

    void onTaskDeleted(long itemId);
}
