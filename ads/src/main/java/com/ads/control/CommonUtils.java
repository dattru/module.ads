package com.ads.control;

import android.content.Context;

import com.ads.control.dialog.ExitAppDialog;
import com.ads.control.dialog.RateAppDialog;
import com.ads.control.funtion.SharedPreferencesHelper;


public class CommonUtils {
    public static void showRateDialog(final Context mContext) {
        if (SharedPreferencesHelper.isRated(mContext)) {
            return;
        }
        RateAppDialog dialog = new RateAppDialog(mContext);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    public static void showExitAppDialog(final Context mContext, final String nativeId) {
        ExitAppDialog dialog = new ExitAppDialog(mContext, nativeId);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }
}


