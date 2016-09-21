package com.github.denisura.kandone.ui.task;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.denisura.kandone.R;
import com.github.denisura.kandone.data.model.TaskModel;
import com.github.denisura.kandone.ui.task.helper.ItemTouchHelperViewHolder;
import com.github.denisura.kandone.utils.JodaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.denisura.kandone.R.id.checkBox;
import static com.github.denisura.kandone.R.id.dueDate;
import static com.github.denisura.kandone.R.id.task;


public class TaskCollectionItemViewHolder
        extends RecyclerView.ViewHolder
        implements View.OnClickListener, ItemTouchHelperViewHolder {

    private TaskModel mTaskModel;
    private TaskCollectionAdapter mAdapter;

    @BindView(task)
    TextView mTaskText;

    @BindView(dueDate)
    TextView mDueDate;

    @BindView(R.id.score)
    TextView mPrioriyScore;

    @BindView(checkBox)
    CheckBox mCheckBox;


    public TaskCollectionItemViewHolder(View itemView, TaskCollectionAdapter adapter) {
        super(itemView);
        itemView.setClickable(true);
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
        mAdapter = adapter;
    }

    public void bindTodoItem(TaskModel taskModel, boolean isSelected) {
        mTaskModel = taskModel;
        mTaskText.setText(mTaskModel.getTask());

        if (mTaskModel.getDoneDate() != null) {
            mCheckBox.setChecked(true);
            mTaskText.setPaintFlags(mTaskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mTaskText.setTextColor(mAdapter.getContext().getResources().getColor(android.R.color.tertiary_text_light));
            mDueDate.setText("");
        } else {
            mCheckBox.setChecked(false);
            mTaskText.setPaintFlags(mTaskText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            mTaskText.setTextColor(mAdapter.getContext().getResources().getColor(android.R.color.primary_text_light));
            mDueDate.setText(JodaUtils.formatDueReminder(mAdapter.getContext(), mTaskModel.getDueDate()));
        }
        mPrioriyScore.setText(mAdapter.getContext().getResources().getString(R.string.format_priority_score, mTaskModel.getPriority()));
    }

    @Override
    public void onItemSelected() {
    }

    @Override
    public void onItemClear() {
    }

    @Override
    public void onClick(View v) {
        mAdapter.mSelected = mTaskModel.getId();
        mAdapter.getCallback().onTaskSelected(mTaskModel);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(checkBox)
    public void onToggleCheckbox(View view) {
        if (((CheckBox) view).isChecked()) {
            mAdapter.getCallback().onCheckAction(mTaskModel.getId());
        } else {
            mAdapter.getCallback().onUncheckAction(mTaskModel.getId());
        }
        mAdapter.notifyDataSetChanged();
    }
}

