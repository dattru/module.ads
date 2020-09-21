package com.ads.control.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ads.control.Admod;
import com.ads.control.R;
import com.ads.control.funtion.UtilsApp;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.ymb.ratingbar_lib.RatingBar;


public class ExitAppDialog extends Dialog {
    Context mContext;
    private String nativeId;

    public ExitAppDialog(Context context, String nativeId) {
        super(context);
        mContext = context;
        this.nativeId = nativeId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_exit_app);
        initView();
    }

    private void initView() {
        findViewById(R.id.tv_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        findViewById(R.id.tv_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
            }
        });
        Admod.getInstance().loadNative((Activity) mContext
                , (FrameLayout) findViewById(R.id.fl_adplaceholder)
                , (ShimmerFrameLayout) findViewById(R.id.shimmer_container),
                nativeId);
    }
}
