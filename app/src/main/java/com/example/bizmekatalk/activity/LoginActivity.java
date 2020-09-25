package com.example.bizmekatalk.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bizmekatalk.api.common.ApiPath;
import com.example.bizmekatalk.api.common.RequestParamBuilder;
import com.example.bizmekatalk.api.webapi.common.WebApiController;
import com.example.bizmekatalk.api.webapi.request.RequestAPI;
import com.example.bizmekatalk.databinding.LoginActivityBinding;
import com.example.bizmekatalk.api.common.RequestParams;
import com.example.bizmekatalk.utils.CustomDialog;

import com.example.bizmekatalk.utils.PreferenceManager;
import com.example.bizmekatalk.utils.SoftKeyboard;
import com.example.bizmekatalk.utils.Validation;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private SoftKeyboard softKeyboard;
    private LoginActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginActivityBinding.inflate(getLayoutInflater());
        View lView = binding.getRoot();
        setContentView(lView);
        getSupportActionBar().hide();


        //로그인 버튼 클릭
        binding.loginBtn.setOnClickListener(view -> moveToPinRegister());


        //마지막 입력값에서 엔터 입력시 동작
        binding.compIdEdit.setOnEditorActionListener((textView, i, keyEvent) ->moveToPinRegister());


        //키보드 반응형 레이아웃 설정
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        softKeyboard = new SoftKeyboard(binding.loginLayout,im);
        //code convention
        SoftKeyboard.SoftKeyboardChanged softKeyboardChanged = new SoftKeyboard.SoftKeyboardChanged() {
            ViewGroup.LayoutParams param1;
            ViewGroup.LayoutParams param2;
            @Override
            public void onSoftKeyboardShow() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,0,0);
                    param1 = binding.loginImgLayout.getLayoutParams();
                    param2 = binding.loginTextLayout.getLayoutParams();
                    binding.loginImgLayout.setLayoutParams(params);
                    binding.loginTextLayout.setLayoutParams(params);
                });
            }

            @Override
            public void onSoftKeyboardHide() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    binding.loginImgLayout.setLayoutParams(param1);
                    binding.loginTextLayout.setLayoutParams(param2);
                });
            }
        };
        softKeyboard.setSoftKeyboardCallback(softKeyboardChanged);//setSoftKeyboardCallback


    }//onCreate()

    private void createMyPost(RequestParams params){

        RequestAPI requestAPI = new RequestAPI();

        requestAPI.<String>getCall(params).ifPresent(call->{
            new WebApiController<String>().request(call,(result)->{
                if(result.isSuccess()){
                    try {

                        PreferenceManager.setString(LoginActivity.this,PreferenceManager.USER_ID, params.getBodyJson().get("userid").toString());
                        PreferenceManager.setString(LoginActivity.this,PreferenceManager.COMP_ID, params.getBodyJson().get("compid").toString());
                        PreferenceManager.setString(LoginActivity.this, PreferenceManager.L_TOKEN, new JSONObject(result.getData()).get("ltoken").toString());

                        Intent intent = new Intent(LoginActivity.this, PinRegisterActivity.class);
                        startActivity(intent);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("jay.LoginActivity","로그인 에러. request : "+result.getError());
                }
            });
        });

    }



    //PinRegister로 이동하기 위하여 입력값 validate, token 저장 후 이동(동기처리를 위하여 delay 2초 대기)
    private boolean moveToPinRegister(){
        if(Validation.validateLogin(binding.userIdEdit, binding.userPwdEdit, binding.compIdEdit)){

            createMyPost(
                    new RequestParamBuilder(this.getBaseContext()).
                            setPath(new ApiPath("Authentication","Login")).
                            setBodyJson("userid","fhZ6hZSfV5UG/CjJEyTsUA==").
                            setBodyJson("compid","P1Ao25+VqxuNq9ijelCnnw==").
                            setBodyJson("pwd","1dBcLJsXZJkGl/WdAxYtcw==").
                            setBodyJson("type","M").
                            build()
            );

        }
        else{

            CustomDialog customDialog = new CustomDialog(LoginActivity.this);
            customDialog.callFunction("입력 정보가 올바르지 않습니다.");
            return false;

        }
        return true;
    }//moveToPin

}
