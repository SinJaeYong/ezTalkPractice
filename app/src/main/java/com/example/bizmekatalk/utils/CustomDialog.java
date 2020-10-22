package com.example.bizmekatalk.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;

import android.view.Window;
import android.widget.Button;

import android.widget.TextView;


import com.example.bizmekatalk.R;
import com.example.bizmekatalk.common.PreferenceManager;

/**
 * Created by Administrator on 2017-08-07.
 */

public class CustomDialog {

    private Context context;

    public CustomDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수 정의
    public void callFunction(final String message) {


        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 커스텀 다이얼로그의 레이아웃 설정
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView alertTitle = (TextView)dialog.findViewById(R.id.alert_title);
        final Button alertOkButton = (Button) dialog.findViewById(R.id.alert_ok_button);
        final Button alertCancelButton = (Button) dialog.findViewById(R.id.alert_cancel_button);
        alertTitle.setText(message);
        dialog.show();

        alertOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(PreferenceManager.TAG,"Dialogue_Message : "+message);
                dialog.dismiss();
            }
        });//람다식 사용하기
        alertCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그 종료
                dialog.dismiss();
            }
        });
    }
}
