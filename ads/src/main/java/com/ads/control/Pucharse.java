package com.ads.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.control.funtion.SharedPreferencesHelper;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

public class Pucharse {
    private static final String LICENSE_KEY = null;
    private static final String MERCHANT_ID = null;
    private BillingProcessor bp;
    public static final String PRODUCT_ID = "no.ads_recovery.2509";
    //    public static final String PRODUCT_ID = "android.test.purchased";
    @SuppressLint("StaticFieldLeak")
    private static Pucharse instance;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Pucharse getInstance(Context ctx) {
        if (instance == null) {
            context = ctx;
            instance = new Pucharse();
        }
        return instance;
    }

    private Pucharse() {
    }

    public void initBilling() {
        bp = new BillingProcessor(context, LICENSE_KEY, MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                SharedPreferencesHelper.setPurchased((Activity) context, true);
            }

            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
            }

            @Override
            public void onBillingInitialized() {

            }

            @Override
            public void onPurchaseHistoryRestored() {
                if (bp.isPurchased(PRODUCT_ID)) {
                    SharedPreferencesHelper.setPurchased((Activity) context, true);
                }
            }
        });
        bp.initialize();
    }

    public boolean isPucharsed() {
        try {
            return bp.isPurchased(PRODUCT_ID) || SharedPreferencesHelper.isPurchased((Activity) context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void purcharse(Activity activity) {
        bp.purchase(activity, PRODUCT_ID);
    }

    public boolean handleActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        return bp.handleActivityResult(requestCode, resultCode, data);
    }

    public String getPrice() {
        return "1.49$";
    }

    public String getOldPrice() {
        return "2.99$";
    }
}
