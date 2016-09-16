package com.github.denisura.kandone.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.github.denisura.kandone.KandoneApplication;
import com.github.denisura.kandone.R;
import com.squareup.leakcanary.RefWatcher;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

import static com.github.denisura.kandone.R.id.datePicker;


/**
 * TODO: check task is not empty
 *
 */
public class SaveTaskDialogFragment extends DialogFragment {

    private Unbinder unbinder;

    private static final String ARG_TITLE = "title";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;


    @BindView(R.id.dueDate)
    public TextView mDueDateText;


    @BindView(R.id.task)
    public TextInputEditText mTask;


    @BindView(R.id.notes)
    public TextInputEditText mNotes;

    public SaveTaskDialogFragment() {
    }

    public static SaveTaskDialogFragment newAddInstance() {
        SaveTaskDialogFragment frag = new SaveTaskDialogFragment();
        Bundle args = new Bundle();
        //TODO move sting to resource
        args.putString(ARG_TITLE, "Add task");
        frag.setArguments(args);
        return frag;
    }


    public static SaveTaskDialogFragment newEditInstance() {
        SaveTaskDialogFragment frag = new SaveTaskDialogFragment();
        Bundle args = new Bundle();
        //TODO move sting to resource
        args.putString(ARG_TITLE, "Edit task");
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_save_task, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getArguments().getString(ARG_TITLE));

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        }
        setHasOptionsMenu(true);

        return rootView;
    }


    @OnClick(datePicker)
    public void onDatePick(View view) {
        DatePickerFragment dialog = DatePickerFragment.newInstance(null);
        dialog.setTargetFragment(SaveTaskDialogFragment.this, REQUEST_DATE);
        dialog.show(getFragmentManager(), DIALOG_DATE);

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_ak, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            // handle confirmation button click here

            // todo save and dissmiss
            Timber.d("save %s %s %s", mTask.getText(), mNotes.getText(), mDueDateText.getText());
            return true;
        } else if (id == android.R.id.home) {
            // handle close button click here
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        RefWatcher refWatcher = KandoneApplication.getRefWatcher();
        refWatcher.watch(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mDueDateText.setText(date.toString());
        }
    }
}