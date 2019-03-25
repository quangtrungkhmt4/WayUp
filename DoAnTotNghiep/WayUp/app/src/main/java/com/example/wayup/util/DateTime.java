package com.example.wayup.util;

import android.annotation.SuppressLint;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTime {
    @SuppressLint("SimpleDateFormat")
    public static String currentDateTime(){
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    public static String convertDate(String date){
        String[] strings = date.split("_");
        String[] strs = strings[0].split("");
        return strs[7] + strs[8] + "/" + strs[5] + strs[6]
                + "/" + strs[1] + strs[2] + strs[3] + strs[4];
    }

    public static int randomCode(){
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String[] strings = date.split("_");
        return Integer.parseInt(strings[1]);
    }

}
