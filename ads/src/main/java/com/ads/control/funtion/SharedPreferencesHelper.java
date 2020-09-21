package com.ads.control.funtion;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesHelper {
    private static final String FILE_SETTING = "setting.pref";
    private static final String IS_PURCHASE = "IS_PURCHASE";
    private static final String IS_RATE = "IS_RATE";

    public static void setPurchased(Activity activity, boolean isPurcharsed) {
        SharedPreferences pref = activity.getSharedPreferences(FILE_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_PURCHASE, isPurcharsed);
        editor.apply();
    }

    public static boolean isPurchased(Activity activity) {
        return activity.getSharedPreferences(FILE_SETTING, Context.MODE_PRIVATE).getBoolean(IS_PURCHASE, false);
    }

    public static void setRated(Context context, boolean isRate) {
        SharedPreferences pref = context.getSharedPreferences(FILE_SETTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_RATE, isRate);
        editor.apply();
    }

    public static boolean isRated(Context context) {
        return context.getSharedPreferences(FILE_SETTING, Context.MODE_PRIVATE).getBoolean(IS_RATE, false);
    }

}
