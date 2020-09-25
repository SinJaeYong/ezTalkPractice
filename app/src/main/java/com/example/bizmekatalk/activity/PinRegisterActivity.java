package com.example.bizmekatalk.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bizmekatalk.databinding.PinRegisterActivityBinding;
import com.example.bizmekatalk.utils.CustomDialog;
import com.example.bizmekatalk.utils.PreferenceManager;
import com.example.bizmekatalk.utils.SoftKeyboard;
import com.example.bizmekatalk.utils.Validation;

public class PinRegisterActivity extends AppCompatActivity {

    private SoftKeyboard softKeyboard;
    private PinRegisterActivityBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PinRegisterActivityBinding.inflate(getLayoutInflater());
        View lView = binding.getRoot();
        setContentView(lView);
        getSupportActionBar().hide();
        binding.pinUpdateBtn.setOnClickListener(view -> moveToMain());
        binding.pinNumConf.setOnEditorActionListener((textView, i, keyEvent) -> {
            moveToMain();
            return true;
        });


        //키보드 반응형 레이아웃 설정
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        softKeyboard = new SoftKeyboard(binding.pinLayout,im);
        SoftKeyboard.SoftKeyboardChanged softKeyboardChanged = new SoftKeyboard.SoftKeyboardChanged() {
            ViewGroup.LayoutParams param1;
            ViewGroup.LayoutParams param2;
            @Override
            public void onSoftKeyboardShow() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,0,0);
                    param1 = binding.pinImgLayout.getLayoutParams();
                    param2 = binding.pinTextLayout.getLayoutParams();
                    binding.pinImgLayout.setLayoutParams(params);
                    binding.pinTextLayout.setLayoutParams(params);
                });
            }
            @Override
            public void onSoftKeyboardHide() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    binding.pinImgLayout.setLayoutParams(param1);
                    binding.pinTextLayout.setLayoutParams(param2);
                });
            }
        };
        softKeyboard.setSoftKeyboardCallback(softKeyboardChanged);//setSoftKeyboardCallback

    }


    //입력값 검증,Pin값 저장 후 MainActivity로 이동
    private void moveToMain() {
        if(Validation.validatePin(binding.pinNum,binding.pinNumConf)){
            PreferenceManager.setString(PinRegisterActivity.this, PreferenceManager.PIN_KEY, binding.pinNum.getText().toString());
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
