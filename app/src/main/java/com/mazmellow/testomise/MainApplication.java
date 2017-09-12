package com.mazmellow.testomise;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by maz on 9/12/2017 AD.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
    }
}
