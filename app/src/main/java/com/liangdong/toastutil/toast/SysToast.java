package com.liangdong.toastutil.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;


public class SysToast implements IToast {

    private Toast mToast;
    private Context mContext;

    public static IToast makeText(Context context, String text, int duration, int location) {
        return new SysToast(context)
                .setText(text)
                .setDuration(duration)
                .setScreenLocation(location);
    }

    @SuppressLint("ShowToast")
    private SysToast(Context context) {
        mContext = context;
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    @Override
    public IToast setScreenLocation(int screenLocation) {
        switch (screenLocation) {
            case ToastUtil.ON_SCREEN_TOP:
                mToast.setGravity(Gravity.TOP, 0, ToastUtil.getScreenHeight(mContext) / 4);
                break;
            case ToastUtil.ON_SCREEN_CENTER:
                mToast.setGravity(Gravity.CENTER, 0, 0);
                break;
            case ToastUtil.ON_SCREEN_BOTTOM:
                mToast.setGravity(Resources.getSystem().getInteger(Resources.getSystem().getIdentifier("config_toastDefaultGravity", "integer", "android")), 0,
                        Resources.getSystem().getDimensionPixelOffset(Resources.getSystem().getIdentifier("toast_y_offset", "dimen", "android")));
                break;
        }
        return this;
    }

    @Override
    public IToast setDuration(int duration) {
        mToast.setDuration(duration);
        return this;
    }

    @Override
    public IToast setView(View view) {
        mToast.setView(view);
        return this;
    }

    @Override
    public IToast setText(String text) {
        mToast.setText(text);
        return this;
    }

    @Override
    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

    @Override
    public void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}