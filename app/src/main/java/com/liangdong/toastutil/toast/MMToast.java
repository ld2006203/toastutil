package com.liangdong.toastutil.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liangdong.toastutil.R;


/**
 * 自定义吐司， 在无通知权限时尝试弹出
 * Created by liangdong on 2018/9/30.
 */
public class MMToast implements IToast {

    private WindowManager windowManager;
    private Long mDurationMillis = 2000L;
    private ViewGroup mViewGroup;
    private View mNextView;
    private WindowManager.LayoutParams systemToastLayoutParams;
    private Context mContext;

    @SuppressLint("ShowToast")
    private MMToast(Context context) {
        this.mContext = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (systemToastLayoutParams == null) {
            systemToastLayoutParams = new WindowManager.LayoutParams();
            systemToastLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            systemToastLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            systemToastLayoutParams.format = PixelFormat.TRANSLUCENT;
            systemToastLayoutParams.windowAnimations = R.style.Animation_MMToast;
//            systemToastLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            systemToastLayoutParams.setTitle("MMToast");
            systemToastLayoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        }
    }

    public static IToast makeText(Context context, String text, int duration, int screenLocation) {
        return new MMToast(context).setText(text).setDuration(duration).setScreenLocation(screenLocation);
    }

    @Override
    public IToast setScreenLocation(int screenLocation) {
        switch (screenLocation) {
            case ToastUtil.ON_SCREEN_TOP:
                systemToastLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                systemToastLayoutParams.y = ToastUtil.getScreenHeight(mContext) / 4;
                break;
            case ToastUtil.ON_SCREEN_CENTER:
                systemToastLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
                break;
            case ToastUtil.ON_SCREEN_BOTTOM:
                systemToastLayoutParams.gravity = Resources.getSystem().getInteger(Resources.getSystem().getIdentifier("config_toastDefaultGravity", "integer", "android"));
                systemToastLayoutParams.y = Resources.getSystem().getDimensionPixelOffset(Resources.getSystem().getIdentifier("toast_y_offset", "dimen", "android"));
                break;
        }
        return this;
    }

    @Override
    public IToast setDuration(int duration) {
        switch (duration) {
            case Toast.LENGTH_LONG:
                mDurationMillis = 3500L;
                break;
            case Toast.LENGTH_SHORT:
            default:
                mDurationMillis = 2000L;
                break;
        }
        return this;
    }

    @Override
    public IToast setView(View view) {
        if (mViewGroup != null) {
            mViewGroup.removeAllViews();
            mViewGroup.addView(view);
        } else {
            mViewGroup = new LinearLayout(mContext);
            mViewGroup.addView(view);
        }
        mNextView = view;
        return this;
    }

    @SuppressLint("ShowToast")
    @Override
    public IToast setText(String text) {
        if (mNextView != null) {
            TextView tv = mNextView.findViewById(android.R.id.message);
            tv.setText(text);
        } else {
            setView(Toast.makeText(mContext, text, Toast.LENGTH_SHORT).getView());
        }
        return this;
    }

    @Override
    public void show() {
        if (!mViewGroup.isAttachedToWindow()) {
            try {
                windowManager.addView(mViewGroup, systemToastLayoutParams);
                mViewGroup.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cancel();
                    }
                }, mDurationMillis);
            } catch (Exception ignore) {

            }
        }
    }

    @Override
    public void cancel() {
        try {
            if (windowManager != null && mViewGroup != null) {
                windowManager.removeView(mViewGroup);
            }
        } catch (Exception ignored) {
        }
        mViewGroup = null;
        mNextView = null;
    }
}