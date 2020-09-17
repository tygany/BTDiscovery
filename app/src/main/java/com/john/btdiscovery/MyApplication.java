package com.john.btdiscovery;

import android.app.Application;
import android.content.Context;


public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();
    private static Context context;

    public static Context getContext() {
        return context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

}
