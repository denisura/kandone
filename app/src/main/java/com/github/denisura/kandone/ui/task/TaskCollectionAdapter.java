package com.github.denisura.kandone.ui.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.denisura.kandone.R;
import com.github.denisura.kandone.data.database.AppProvider;
import com.github.denisura.kandone.data.database.TaskColumns;
import com.github.denisura.kandone.data.model.TaskModel;
import com.github.denisura.kandone.ui.CursorRecyclerViewAdapter;
import com.github.denisura.kandone.ui.task.helper.ItemTouchHelperAdapter;

import timber.log.Timber;


public class TaskCollectionAdapter
        extends CursorRecyclerViewAdapter<TaskCollectionItemViewHolder>
        implements ItemTouchHelperAdapter {

    private ItemListener mCallbacks;
    private Context mContext;

    public long mSelected = -1;

    public TaskCollectionAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public void setCallback(ItemListener callbacks) {
        mCallbacks = callbacks;
    }

    public ItemListener getCallback() {
        return mCallbacks;
    }


    @Override
    public TaskCollectionItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView) {
            int layoutId = R.layout.cardview_task;
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            return new TaskCollectionItemViewHolder(view, this);
        }
        throw new RuntimeException("Not bound to RecyclerView");
    }

    @Override
    public void onBindViewHolder(TaskCollectionItemViewHolder viewHolder, Cursor cursor) {
        TaskModel taskModel = new TaskModel(cursor);
        viewHolder.bindTodoItem(taskModel, mSelected == taskModel.getId());
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        long downId, upId;
        if (fromPosition > toPosition) {
            downId = getItemId(fromPosition);
            upId = getItemId(toPosition);
        } else {
            downId = getItemId(toPosition);
            upId = getItemId(fromPosition);
        }

        ContentResolver cr = mContext.getContentResolver();
        String mSelectionClause = TaskColumns._ID + "= ?";
        String[] mSelectionArgs = {String.valueOf(downId)};
        String[] mUpSelectionArgs = {String.valueOf(upId)};
        cr.update(AppProvider.Priority.DOWN_CONTENT_URI, null, mSelectionClause, mSelectionArgs);
        cr.update(AppProvider.Priority.UP_CONTENT_URI, null, mSelectionClause, mUpSelectionArgs);

        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        Timber.d("onItemDismiss Position %d with ID %d ", position, getItemId(position));
        getCursor().moveToPosition(position);
        TaskModel todoItem = new TaskModel(getCursor());
        String mSelectionClause = TaskColumns.PRIORITY + "> ?";
        String[] mSelectionArgs = {String.valueOf(todoItem.getPriority())};
        ContentResolver cr = mContext.getContentResolver();
        cr.update(AppProvider.Priority.DOWN_CONTENT_URI, null, mSelectionClause, mSelectionArgs);
        cr.delete(AppProvider.Tasks.withId(todoItem.getId()), null, null);

        notifyItemRemoved(position);
    }
}