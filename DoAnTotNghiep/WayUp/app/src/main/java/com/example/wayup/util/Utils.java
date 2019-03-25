package com.example.wayup.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;

public class Utils {
    public static boolean checkPermission(Context mContext) {
        @SuppressLint("InlinedApi") int READ_EXTERNAL_STORAGE = mContext.checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNAL_STORAGE = mContext.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ACCESS_FINE_LOCATION = mContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        return READ_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED
                && WRITE_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED
                && ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED;
    }
}
