package com.bathem.vocabpower.Application;

import android.app.Application;

/**
 * Created by mehtab on 10/06/16.
 */
public class AppManager extends Application {


    static AppManager sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static AppManager getsInstance() {
        return sInstance;
    }


}
