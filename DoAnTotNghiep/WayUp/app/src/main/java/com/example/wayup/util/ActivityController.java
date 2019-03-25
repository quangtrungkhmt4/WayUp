package com.example.wayup.util;

import android.content.Context;
import android.content.Intent;

import com.example.wayup.model.AbstractModel;

public class ActivityController {

    public static final String KEY_PASS_DATA = "KEY_PASS_DATA";
    public<E extends AbstractModel> void switchActivityWithData(Context context, Class<?> target, E object){
        Intent intent = new Intent(context, target);
        intent.putExtra(KEY_PASS_DATA, object);
        context.startActivity(intent);
    }

    public static void switchActivityWithoutData(Context context, Class<?> target){
        Intent intent = new Intent(context, target);
        context.startActivity(intent);
    }
}
