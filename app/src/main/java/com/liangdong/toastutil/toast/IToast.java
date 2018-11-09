package com.liangdong.toastutil.toast;

import android.view.View;

public interface IToast {

    IToast setScreenLocation(int screenLocation);

    IToast setDuration(int durationMillis);

    IToast setView(View view);

    IToast setText(String text);

    void show();

    void cancel();
}