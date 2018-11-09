package com.liangdong.toastutil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.liangdong.toastutil.toast.ToastUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShortToast(v.getContext(), "弹出一个toast");
            }
        });
    }
}
