package com.github.denisura.kandone.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.github.denisura.kandone.KandoneApplication;
import com.github.denisura.kandone.R;
import com.github.denisura.kandone.data.model.TaskModel;
import com.github.denisura.kandone.ui.task.ItemListener;
import com.github.denisura.kandone.utils.JodaUtils;
import com.squareup.leakcanary.RefWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ShowToDoItemFragment extends DialogFragment {

    private Unbinder unbinder;

    private static final String ARG_ITEM = "item";

    @BindView(R.id.show_task)
    public TextView mTaskView;

    @BindView(R.id.show_notes)
    public TextView mNotesView;

    @BindView(R.id.show_due)
    public TextView mDueView;


    private ItemListener mCallbacks;

    public ShowToDoItemFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ShowToDoItemFragment newInstance(TaskModel todoItem) {
        ShowToDoItemFragment frag = new ShowToDoItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, todoItem);
        frag.setArguments(args);
        return frag;
    }


//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
////        String title = getArguments().getString("title");
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//  //      alertDialogBuilder.setTitle(title);
//        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // on success
//            }
//        });
//        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        return alertDialogBuilder.create();
//    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.dialog_show_todo_item, container);
//        unbinder = ButterKnife.bind(this, v);
//        return v;
//    }

    public void setListener(ItemListener listener) {
        mCallbacks = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_show_todo_item, null);
        unbinder = ButterKnife.bind(this, v);

        final TaskModel todoItem = (TaskModel) getArguments().getSerializable(ARG_ITEM);


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(getContext().getResources().getString(R.string.dialog_title_show))
                .setView(v)

                .setNeutralButton(getContext().getResources().getString(R.string.dialog_action_edit),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mCallbacks.onEditAction(todoItem);
                            }
                        }
                )
                .setNegativeButton(getContext().getResources().getString(R.string.dialog_action_back),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //((FragmentAlertDialog)getActivity()).doNegativeClick();
                            }
                        }
                );


        mTaskView.setText(todoItem.getTask());
        mNotesView.setText(todoItem.getNotes());

        if (todoItem.getDoneDate() != null) {
            mDueView.setText(JodaUtils.formatDoneDate(getContext(), todoItem.getDoneDate()));
            dialogBuilder.setPositiveButton(getContext().getResources().getString(R.string.dialog_action_uncheck),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mCallbacks.onUncheckAction(todoItem.getId());
                        }
                    }
            );
        } else {
            mDueView.setText(JodaUtils.formatDueDate(getContext(), todoItem.getDueDate()));
            dialogBuilder.setPositiveButton(getContext().getResources().getString(R.string.dialog_action_check),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            mCallbacks.onCheckAction(todoItem.getId());
                        }
                    }
            );
        }


        return dialogBuilder.create();
    }


//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = KandoneApplication.getRefWatcher();
        refWatcher.watch(this);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}