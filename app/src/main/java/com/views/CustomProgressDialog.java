package com.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.sp.R;
import com.utils.Utils;

/**
 * Created by Naresh on 12-Jan-17.
 */

public class CustomProgressDialog extends ProgressDialog {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loader_view);
        TextView tv_loading  = (TextView)findViewById(R.id.tv_loading);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setFillAfter(true);
        tv_loading.setTypeface(Utils.getAwesomeTypeface(context));
        tv_loading.startAnimation(rotate);
    }

    public CustomProgressDialog(Context context) {
        super(context);
        this.context  = context;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context  = context;
    }
}
