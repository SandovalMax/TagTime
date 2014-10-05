package com.maris.tagtime;

import android.content.Context;

/**
 * Created by Sandoval on 25/09/2014.
 */
public class MyApp extends android.app.Application {

    private static MyApp instance;

    public MyApp() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

}
