package com.example.bizmekatalk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bizmekatalk.api.common.RequestParams;
import com.example.bizmekatalk.api.webapi.common.WebApiController;
import com.example.bizmekatalk.api.webapi.request.RequestAPI;
import com.example.bizmekatalk.databinding.PinRegisterActivityBinding;
import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.items.UserItem;
import com.example.bizmekatalk.utils.CustomDialog;
import com.example.bizmekatalk.common.PreferenceManager;
import com.example.bizmekatalk.utils.SoftKeyboard;
import com.example.bizmekatalk.utils.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PinRegisterActivity extends AppCompatActivity {

    private SoftKeyboard softKeyboard;
    private PinRegisterActivityBinding binding;
    private Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDataBinding();

        setListener();

        softKeyboard = SoftKeyboard.setReactiveKeyboard(this,
                binding.pinLayout,
                binding.pinImgLayout,
                binding.pinTextLayout);
        //setReactiveKeyboard();

        new Handler(Looper.myLooper()){

        };
    }






    private void setDataBinding() {
        binding = PinRegisterActivityBinding.inflate(getLayoutInflater());
        View lView = binding.getRoot();
        setContentView(lView);
        getSupportActionBar().hide();
    }


    private void setListener() {
        binding.pinUpdateBtn.setOnClickListener(view -> moveToMain());
        binding.pinNumConf.setOnEditorActionListener((textView, i, keyEvent) -> {
            moveToMain();
            return true;
        });
    }



    //입력값 검증,Pin값 저장 후 MainActivity로 이동
    private void moveToMain() {
        if(Validation.validatePin(binding.pinNum,binding.pinNumConf)){
            PreferenceManager.setString(PreferenceManager.getPinKey(), binding.pinNum.getText().toString());
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
