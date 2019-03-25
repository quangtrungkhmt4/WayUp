package com.example.wayupmanagement.util;

import android.annotation.SuppressLint;

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

    public static int random(){
//        int year = Calendar.getInstance().get(Calendar.YEAR);
//        int month = Calendar.getInstance().get(Calendar.MONTH);
//        int date = Calendar.getInstance().get(Calendar.DATE);
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int min = Calendar.getInstance().get(Calendar.MINUTE);
        int sec = Calendar.getInstance().get(Calendar.SECOND);
        int miliSec = Calendar.getInstance().get(Calendar.MILLISECOND);
        return Integer.parseInt(String.valueOf(hour) + min + sec
                + miliSec);

    }
}
