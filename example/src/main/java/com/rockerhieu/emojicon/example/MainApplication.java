package com.rockerhieu.emojicon.example;

import android.app.Application;

import com.facebook.soloader.SoLoader;

/**
 * Created by cpu10661 on 10/4/17.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, false);
    }
}
