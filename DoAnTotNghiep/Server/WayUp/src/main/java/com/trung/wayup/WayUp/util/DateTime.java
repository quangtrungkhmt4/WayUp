package com.trung.wayup.WayUp.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTime {
    public static String currentDateTime(){
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    public static String convertDate(String date){
        String[] strings = date.split("_");
        String[] strs = strings[0].split("");
        return strs[6] + strs[7] + "/" + strs[4] + strs[5]
                + "/" + strs[0] + strs[1] + strs[2] + strs[3];
    }
}
