package com.example.wayup.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private static final String KEY_APP = "WAY_UP";

    public static void saveData(String key, String value, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_APP, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getData(String key, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_APP, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

//    public static void saveJob(String key, String value, Context context){
//        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_APP, Context.MODE_PRIVATE);
//        String builder = sharedPreferences.getString(key, "");
//        builder = builder + "<~ahihi~>" + value;
//        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(key, builder);
//        editor.apply();
//    }

}
