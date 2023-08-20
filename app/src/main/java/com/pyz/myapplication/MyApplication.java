package com.pyz.myapplication;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        ARouter.init(this);
    }
}
