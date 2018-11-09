package com.liangdong.toastutil.toast;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationManagerCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 吐司工具类，尽量不要使用applicationContext，否则在没有通知权限的时候可能无法弹出
 * Created by liangdong on 2018/2/28.
 */
public class ToastUtil {

    public final static int ON_SCREEN_TOP = 0;
    public final static int ON_SCREEN_CENTER = 1;
    public final static int ON_SCREEN_BOTTOM = 2;

    /**
     * 弹出一个短时吐司，
     *
     * @param context
     * @param text
     */
    public static void showShortToast(Context context, String text) {
        showToast(context, text, null, Toast.LENGTH_SHORT, ON_SCREEN_BOTTOM);
    }

    /**
     * @param context
     * @param resId
     */
    public static void showShortToast(Context context, int resId) {
        showToast(context, context.getText(resId).toString(), null, Toast.LENGTH_SHORT, ON_SCREEN_BOTTOM);
    }

    public static void showLongToast(Context context, String text) {
        showToast(context, text, null, Toast.LENGTH_LONG, ON_SCREEN_BOTTOM);
    }

    public static void showLongToast(Context context, int resId) {
        showToast(context, context.getText(resId).toString(), null, Toast.LENGTH_LONG, ON_SCREEN_BOTTOM);
    }

    public static void showCenterTopShortToast(Context context, String text) {
        showToast(context, text, null, Toast.LENGTH_SHORT, ON_SCREEN_TOP);
    }

    /**
     * @param context
     * @param view
     */
    public static void showCenterShortToastWithCustomView(Context context, View view) {
        showToast(context, "", view, Toast.LENGTH_SHORT, ON_SCREEN_CENTER);
    }

    //---------------------——-------//
    private static IToast toastText = null;
    private static IToast toastView = null;

    @SuppressLint("ShowToast")
    private static IToast makeToast(Context context, String text, View view, int duration, int location) {
        boolean isPushOpen = areNotificationsEnabled(context);

        if (context instanceof Application) {
            isPushOpen = true;
        }

        IToast toastTemp;
        if (view != null) {
            toastTemp = toastView;
        } else {
            toastTemp = toastText;
        }

        if (isPushOpen) {
            if (!(toastTemp instanceof SysToast)) {
                toastTemp = SysToast.makeText(context, text, duration, location);
            }
        } else {
            if (!(toastTemp instanceof MMToast)) {
                toastTemp = MMToast.makeText(context, text, duration, location);
            }
        }

        if (view != null) {
            toastTemp.setView(view);
        } else {
            toastTemp.setText(text);
        }

        toastTemp.setDuration(duration);
        toastTemp.setScreenLocation(location);
        return toastTemp;
    }

    private static void showToast(final Context context, final String text, final View view, final int duration, final int location) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            makeToast(context, text, view, duration, location).show();
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    makeToast(context, text, view, duration, location).show();
                }
            });
        }
    }

    public static boolean areNotificationsEnabled(Context context) {
        if (context != null) {
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        }
        return true;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = defaultDisplayMetrics(context);
        if (metrics == null) {
            return 1;
        }
        return metrics.heightPixels;
    }

    private static DisplayMetrics defaultDisplayMetrics(Context context) {
        if (context == null) {
            return null;
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return null;
        }
        Display display = wm.getDefaultDisplay();
        if (display == null) {
            return null;
        }
        DisplayMetrics metric = new DisplayMetrics();
        display.getMetrics(metric);
        return metric;
    }
}