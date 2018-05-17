package com.bootcamp.bootcampcrud;

import android.app.Application;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;


@Database(name = ApplicationBootCamp.NAME, version = ApplicationBootCamp.VERSION)
public class ApplicationBootCamp extends Application {

    public static final String NAME = "Bootcamp";

    public static final int VERSION = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());

    }
}
