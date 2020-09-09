package com.example.bizmekatalk.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.utils.CustomDialog;
import com.example.bizmekatalk.utils.PreferenceManager;
import com.example.bizmekatalk.utils.SoftKeyboard;
import com.example.bizmekatalk.utils.Validation;

public class PinRegisterActivity extends AppCompatActivity {
    private EditText pinNum;
    private EditText pinNumConf;
    private Button pinUpdateBtn;
    private SoftKeyboard softKeyboard;
    private LinearLayout pinImgLayout;
    private LinearLayout pinTextLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_register_activity);
        getSupportActionBar().hide();

        pinNum = findViewById(R.id.pinNum);
        pinNumConf = findViewById(R.id.pinNumConf);
        pinUpdateBtn = findViewById(R.id.pinUpdateBtn);
        pinImgLayout = findViewById(R.id.pin_img_layout);
        pinTextLayout = findViewById(R.id.pin_text_layout);

        if ( this.pinUpdateBtn!= null){
            this.pinUpdateBtn.setOnClickListener(view -> moveToMain());
        }
        else{
            Log.i(PreferenceManager.TAG,"핀 업데이트 오류");
        }

        if(this.pinNumConf != null){
            pinNumConf.setOnEditorActionListener((textView, i, keyEvent) -> {
                moveToMain();
                return true;
            });
        }else{ Log.i(PreferenceManager.TAG,"핀 확인 위젯 오류");}


        //키보드 반응형 레이아웃 설정
        final LinearLayout pinLayout = findViewById(R.id.pinLayout);
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        softKeyboard = new SoftKeyboard(pinLayout,im);
        SoftKeyboard.SoftKeyboardChanged softKeyboardChanged = new SoftKeyboard.SoftKeyboardChanged() {
            ViewGroup.LayoutParams param1;
            ViewGroup.LayoutParams param2;
            @Override
            public void onSoftKeyboardShow() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,0,0);
                    param1 = pinImgLayout.getLayoutParams();
                    param2 = pinTextLayout.getLayoutParams();
                    pinImgLayout.setLayoutParams(params);
                    pinTextLayout.setLayoutParams(params);
                });
            }

            @Override
            public void onSoftKeyboardHide() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    pinImgLayout.setLayoutParams(param1);
                    pinTextLayout.setLayoutParams(param2);
                });
            }
        };
        softKeyboard.setSoftKeyboardCallback(softKeyboardChanged);//setSoftKeyboardCallback

    }

    //입력값 검증,Pin값 저장 후 MainActivity로 이동
    private void moveToMain() {
        if(Validation.validatePin(pinNum,pinNumConf)){
            PreferenceManager.setString(PinRegisterActivity.this, PreferenceManager.PIN_KEY, pinNum.getText().toString());
            Intent intent = new Intent(PinRegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            CustomDialog customDialog = new CustomDialog(PinRegisterActivity.this);
            customDialog.callFunction("입력 정보가 올바르지 않습니다.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        softKeyboard.unRegisterSoftKeyboardCallback();
    }



}
