package com.example.bizmekatalk.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import com.example.bizmekatalk.R;

public class ProgressDialog {
    private Context context;
    private Dialog dialog;
    public ProgressDialog(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
    }

    // 호출할 다이얼로그 함수 정의
    public void show() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 커스텀 다이얼로그의 레이아웃 설정

        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        dialog.show();
    }

    public void close(){
        dialog.cancel();
    }
}
