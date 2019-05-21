package com.example.ratio;

import android.app.Application;
import android.content.Context;

public class ContextApplication extends Application {
    private static Application instance = null;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
