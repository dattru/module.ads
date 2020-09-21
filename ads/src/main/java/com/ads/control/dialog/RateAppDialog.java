package com.ads.control.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.ads.control.R;
import com.ads.control.funtion.SharedPreferencesHelper;
import com.ads.control.funtion.UtilsApp;
import com.ymb.ratingbar_lib.RatingBar;


public class RateAppDialog extends Dialog {
    Context mContext;
    private Thread th;

    public RateAppDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_rate);
        initView();
    }

    private void initView() {
        RatingBar rating = findViewById(R.id.rating);
        final TextView tvStatus = findViewById(R.id.tv_status);
        rating.setOnRatingChangedListener(new RatingBar.OnRatingChangedListener() {
            @Override
            public void onRatingChange(float v, float v1) {
                if (v == 0.0) {
                    tvStatus.setText("Very bad");
                } else if (v == 1.0) {
                    tvStatus.setText("Bad");
                } else if (v == 2.0) {
                    tvStatus.setText("Good");
                } else if (v == 3.0) {
                    tvStatus.setText("Very good");
                } else if (v == 4.0) {
                    tvStatus.setText("Excelente");
                } else {
                    tvStatus.setText("Awesome");
                }

                if (th != null) {
                    th.interrupt();
                }
                th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            UtilsApp.rateApp(getContext());
                            SharedPreferencesHelper.setRated(mContext, true);
                            dismiss();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                th.start();
            }
        });

    }
}
