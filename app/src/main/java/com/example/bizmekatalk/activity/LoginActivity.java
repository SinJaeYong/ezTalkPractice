package com.example.bizmekatalk.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.SupportActionModeWrapper;

import com.example.bizmekatalk.API.RequestAPI;
import com.example.bizmekatalk.R;
import com.example.bizmekatalk.databinding.LoginActivityBinding;
import com.example.bizmekatalk.items.RequestParams;
import com.example.bizmekatalk.utils.CustomDialog;

import com.example.bizmekatalk.utils.PreferenceManager;
import com.example.bizmekatalk.utils.SoftKeyboard;
import com.example.bizmekatalk.utils.Validation;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;

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
            call.enqueue(
                    new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            String respBodyStr = response.body();
                            String ltoken=null;
                            try {
                                ltoken = new JSONObject(respBodyStr).get("ltoken").toString();
                                PreferenceManager.setString(LoginActivity.this,PreferenceManager.L_TOKEN,ltoken);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //null 처리
                            if(!(("".equals(ltoken)) || (ltoken == null))){
                                Intent intent = new Intent(LoginActivity.this, PinRegisterActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Log.i(PreferenceManager.TAG,"토큰 저장 실패");
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.i(PreferenceManager.TAG,"Response Fail");
                            t.printStackTrace();
                        }
                    }
            );
        });
    }



    //PinRegister로 이동하기 위하여 입력값 validate, token 저장 후 이동(동기처리를 위하여 delay 2초 대기)
    private boolean moveToPinRegister(){
        if(Validation.validateLogin(binding.userIdEdit, binding.userPwdEdit, binding.compIdEdit)){

            String userid = "fhZ6hZSfV5UG/CjJEyTsUA==";
            String compid = "P1Ao25+VqxuNq9ijelCnnw==";
            String pwd = "1dBcLJsXZJkGl/WdAxYtcw==";
            String type = "M";

            //Json String으로 전달
            String path = "Authentication/Login";
            JSONObject bodyJson = new JSONObject();
            try {
                bodyJson.put("userid",userid);
                bodyJson.put("compid",compid);
                bodyJson.put("pwd",pwd);
                bodyJson.put("type",type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PreferenceManager.setString(LoginActivity.this,PreferenceManager.USER_ID,userid);
            PreferenceManager.setString(LoginActivity.this,PreferenceManager.COMP_ID,compid);
            createMyPost(new RequestParams(path,null,bodyJson,PreferenceManager.HTTP_METHOD_POST));

        }
        else{
            CustomDialog customDialog = new CustomDialog(LoginActivity.this);
            customDialog.callFunction("입력 정보가 올바르지 않습니다.");
            return false;
        }
        return true;
    }//moveToPin


}
