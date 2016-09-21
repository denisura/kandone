package com.github.denisura.kandone.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.denisura.kandone.R;
import com.github.denisura.kandone.data.database.AppProvider;
import com.github.denisura.kandone.ui.task.ItemListener;
import com.github.denisura.kandone.ui.task.TaskCollectionAdapter;
import com.github.denisura.kandone.ui.task.helper.SimpleItemTouchHelperCallback;

import butterknife.BindView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {


    public static final int TASK_LOADER = 0;

    private TaskCollectionAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ItemListener mCallbacks;


    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.fragment_todo_list;
    }

    @BindView(R.id.recycler_view)
    public RecyclerView mRecyclerView;

    public MainActivityFragment() {
    }

    public static MainActivityFragment newInstance() {
        return new MainActivityFragment();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(TASK_LOADER, null, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);


        mLayoutManager = new LinearLayoutManager(getContext(), 1, false);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TaskCollectionAdapter(getContext().getApplicationContext(), null);
        mAdapter.setCallback(mCallbacks);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (ItemListener) context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                AppProvider.Tasks.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);

//        if (data != null && data.getCount() > 0) {
//            if (mAdapter.mSelected > 0) {
//                while (data.moveToNext()) {
//                    ProfileModel profileModel = new ProfileModel(data);
//                    if (profileModel.getId() == mAdapter.mSelected) {
//                        break;
//                    }
//                    mPosition++;
//                }
//            } else {
//                data.moveToFirst();
//                ProfileModel profileModel = new ProfileModel(data);
//                mAdapter.mSelected = profileModel.getId();
//                mPosition = 0;
//            }
//            handler.sendEmptyMessage(UPDATE_SELECTED);
//        }
//
//        mRecyclerView.getLayoutManager().scrollToPosition(mPosition);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null) {
            mAdapter.changeCursor(null);
        }
    }

    @Override
    public void onDestroyView() {
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(null);
        }
        mAdapter.setCallback(null);
        mCallbacks = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mCallbacks = null;
        super.onDestroy();
    }
}
