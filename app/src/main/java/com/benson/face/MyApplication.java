package com.benson.face;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by sumixer01 on 2018/4/15.
 */

public class MyApplication extends Application {
    public User user;
    public String faceResult;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

}
