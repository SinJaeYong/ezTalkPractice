package com.example.bizmekatalk.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bizmekatalk.API.TestAPI;
import com.example.bizmekatalk.R;
import com.example.bizmekatalk.utils.CustomDialog;

import com.example.bizmekatalk.utils.HttpRequest;
import com.example.bizmekatalk.utils.PreferenceManager;
import com.example.bizmekatalk.utils.SoftKeyboard;
import com.example.bizmekatalk.utils.Validation;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    private EditText userIdEdit;
    private EditText userPwdEdit;
    private EditText compIdEdit;
    private Button loginBtn;
    private LinearLayout loginImgLayout;
    private LinearLayout loginTextLayout;
    private Context mContext;
    private SoftKeyboard softKeyboard;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mContext = getApplicationContext();
        userIdEdit = findViewById(R.id.userIdEdit);
        userPwdEdit = findViewById(R.id.userPwdEdit);
        compIdEdit = findViewById(R.id.compIdEdit);
        loginBtn = findViewById(R.id.loginBtn);
        loginImgLayout = findViewById(R.id.loginImgLayout);
        loginTextLayout = findViewById(R.id.loginTextLayout);

        //responseBodyConverter(Object.class,null,null).convert(ResponseBody.create(MediaType.parse("application/json;charset=utf-8"), "null"))


        //로그인 버튼 클릭
        if(loginBtn != null){
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveToPinRegister();
                }
            });
        }else{
            Log.i(PreferenceManager.TAG,"로그인 버튼 오류");
        }

        //마지막 입력값에서 엔터 입력시 동작
        if(compIdEdit != null){
            compIdEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    moveToPinRegister();
                    return true;
                }
            });
        }else Log.i(PreferenceManager.TAG,"회사아이디 위젯 오류");


        //키보드 반응형 레이아웃 설정
        final LinearLayout loginLayout = findViewById(R.id.loginLayout);
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        softKeyboard = new SoftKeyboard(loginLayout,im);
        if(loginImgLayout!=null&&loginTextLayout!=null){
            SoftKeyboard.SoftKeyboardChanged softKeyboardChanged = new SoftKeyboard.SoftKeyboardChanged() {
                ViewGroup.LayoutParams param1;
                ViewGroup.LayoutParams param2;
                @Override
                public void onSoftKeyboardShow() {
                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0,0,0,0);
                            param1 = loginImgLayout.getLayoutParams();
                            param2 = loginTextLayout.getLayoutParams();
                            loginImgLayout.setLayoutParams(params);
                            loginTextLayout.setLayoutParams(params);
                        }
                    });
                }

                @Override
                public void onSoftKeyboardHide() {
                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            loginImgLayout.setLayoutParams(param1);
                            loginTextLayout.setLayoutParams(param2);
                        }
                    });
                }
            };
            softKeyboard.setSoftKeyboardCallback(softKeyboardChanged);//setSoftKeyboardCallback
        }
        else{
            Log.i(PreferenceManager.TAG,"키보드 오류");
        }

    }//onCreate()

    private void createMyPost(String path, JSONObject jsonObj){

        TestAPI testAPI = new TestAPI();
        Call<String> call = testAPI.getCall(path,null,jsonObj,PreferenceManager.HTTP_METHOD_POST);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.i("jay.LoginActivity","retrofit response : "+response);
                String respBodyStr = response.body();
                Log.i("jay.LoginActivity","retrofit response.body() : "+respBodyStr);
                String ltoken=null;
                try {
                    ltoken = new JSONObject(respBodyStr).get("ltoken").toString();
                    PreferenceManager.setString(LoginActivity.this,PreferenceManager.L_TOKEN,ltoken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(!("".equals(ltoken)||ltoken==null)){
                    Intent intent = new Intent(LoginActivity.this, PinRegisterActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Log.i(PreferenceManager.TAG,"토큰 저장 실패");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("jay.LoginActivity","fail");
            }
        });
    }



    //PinRegister로 이동하기 위하여 입력값 validate, token 저장 후 이동(동기처리를 위하여 delay 2초 대기)
    private boolean moveToPinRegister(){
        if(Validation.validateLogin(userIdEdit,userPwdEdit,compIdEdit)){

            String userid = "fhZ6hZSfV5UG/CjJEyTsUA==";
            String compid = "P1Ao25+VqxuNq9ijelCnnw==";
            String pwd = "1dBcLJsXZJkGl/WdAxYtcw==";
            String type = "M";

            //Json String으로 전달
            String path = "Authentication/Login";
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("userid",userid);
                jsonObj.put("compid",compid);
                jsonObj.put("pwd",pwd);
                jsonObj.put("type",type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PreferenceManager.setString(LoginActivity.this,PreferenceManager.USER_ID,userid);
            PreferenceManager.setString(LoginActivity.this,PreferenceManager.COMP_ID,compid);
            createMyPost(path,jsonObj);

        }
        else{
            CustomDialog customDialog = new CustomDialog(LoginActivity.this);
            customDialog.callFunction("입력 정보가 올바르지 않습니다.");
            return false;
        }
        return true;
    }//moveToPin


}
