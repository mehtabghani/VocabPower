package com.bathem.vocabpower.Application;

import android.app.Application;
import android.content.Context;

/**
 * Created by mehtab on 10/06/16.
 */
public class AppManager extends Application {


    static AppManager sInstance;
    Context mApplicationContext;

    public static AppManager getsInstance() {
        if(sInstance ==  null)
            sInstance = new AppManager();

        return sInstance;
    }


}
