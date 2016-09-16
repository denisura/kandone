package com.github.denisura.kandone;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

public class KandoneApplication extends Application {

    private static KandoneApplication _instance;
    private RefWatcher _refWatcher;

    public static KandoneApplication get() {
        return _instance;
    }

    public static RefWatcher getRefWatcher() {
        return KandoneApplication.get()._refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = (KandoneApplication) getApplicationContext();
        _refWatcher = LeakCanary.install(this);

        Timber.plant(new Timber.DebugTree());
    }
}
