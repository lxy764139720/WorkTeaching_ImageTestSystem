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

    public static int getPhotoRotationAngle() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context); //SharedPreferences的本质是基于XML文件存储key-value键值对数据
        String angle = preference.getString("photo_rotation_angle", "0");
        assert angle != null;
        return Integer.parseInt(angle);
    }

    public static int getCameraId() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        String angle = preference.getString("camera_id", "0");
        assert angle != null;
        return Integer.parseInt(angle);
    }

    public static void save(String key, String value) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
