package com.bathem.vocabpower.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.bathem.vocabpower.Application.AppManager;

/**
 * Created by mehtab on 10/06/16.
 */
public class SharedPreferenceHelper {

    static SharedPreferenceHelper sInstance;
    final static String DEFAULT_SHARED_PREFERNECE = "default_shared_preference";

    public static SharedPreferenceHelper getInstance() {
        if(sInstance ==  null)
            sInstance = new SharedPreferenceHelper();

        return sInstance;
    }


    public String getSharedPreferenceByKey(String key) {

        String result = "";
        Context context = AppManager.getsInstance().getApplicationContext();


        SharedPreferences sharedPref = context.getSharedPreferences(DEFAULT_SHARED_PREFERNECE, Context.MODE_PRIVATE);

        if (sharedPref.contains(key))
            result = sharedPref.getString(key, "");

        return result;
    }


    public void setSharedPreferenceByKey(String key, String value) {

        Context context = AppManager.getsInstance().getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(DEFAULT_SHARED_PREFERNECE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();

    }

}
