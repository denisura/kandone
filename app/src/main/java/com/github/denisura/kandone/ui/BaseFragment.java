package com.github.denisura.kandone.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.denisura.kandone.KandoneApplication;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseFragment
        extends Fragment {

    private Unbinder unbinder;

    @LayoutRes
    protected int getLayoutResId() {
        throw new RuntimeException("Fragment Layout is not set");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutResId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        RefWatcher refWatcher = KandoneApplication.getRefWatcher();
        refWatcher.watch(this);
    }

}