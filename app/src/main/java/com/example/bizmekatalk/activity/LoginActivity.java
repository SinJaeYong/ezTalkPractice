package com.example.bizmekatalk.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.items.ProfileItem;
import com.example.bizmekatalk.utils.CustomDialog;
import com.example.bizmekatalk.utils.HttpRequest;
import com.example.bizmekatalk.utils.PreferenceManager;
import com.example.bizmekatalk.utils.SoftKeyboard;
import com.example.bizmekatalk.utils.Validate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText userIdEdit;
    private EditText userPwdEdit;
    private EditText compIdEdit;
    private Button loginBtn;
    private LinearLayout loginImgLayout;
    private LinearLayout loginTextLayout;
    private Context mContext;
    private SoftKeyboard softKeyboard;

    private boolean tokenFlag;

    private final Handler handler = new Handler(Looper.myLooper());
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
        Log.i("jay","Login");


        if(loginBtn != null){
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveToPin();
                }
            });
        }else{
            Log.i("jay","로그인 버튼 오류");
        }

        if(compIdEdit != null){
            compIdEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    moveToPin();
                    return true;
                }
            });
        }else Log.i("jay","회사아이디 위젯 오류");


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
            Log.i("jay","키보드 오류");
        }

    }//onCreate()


    @Override
    protected void onDestroy() {
        Log.i("jay","Login onDestroy");
        super.onDestroy();
        softKeyboard.unRegisterSoftKeyboardCallback();
    }

    @Override
    protected void onStop() {
        Log.i("jay","Login onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.i("jay","Login onPause");
        super.onPause();
    }


    private boolean moveToPin() {
        Log.i("jay.LoginActivity","moveToPin");
        if(Validate.loginValidation(userIdEdit,userPwdEdit,compIdEdit)){
            Log.i("jay.LoginActivity","loginValidation");
            final String userId = "fhZ6hZSfV5UG/CjJEyTsUA==";
            final String compId = "P1Ao25+VqxuNq9ijelCnnw==";
            final String pwd = "1dBcLJsXZJkGl/WdAxYtcw==";
            setToken(userId,compId,pwd);



            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("jay.LoginActivity","Runnable run");
                    if(tokenFlag){
                        PreferenceManager.setString(LoginActivity.this,PreferenceManager.USER_ID,userId);
                        PreferenceManager.setString(LoginActivity.this,PreferenceManager.COMP_ID,compId);
                        Log.i("jay.LoginActivity","compid "+PreferenceManager.getString(LoginActivity.this,PreferenceManager.COMP_ID));
                        Intent intent = new Intent(LoginActivity.this, PinActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Log.i("jay.LoginActivity","token 에러");
                    }
                }
            },2000);

        }
        else{
            CustomDialog customDialog = new CustomDialog(LoginActivity.this);
            customDialog.callFunction("입력 정보가 올바르지 않습니다.");
            return false;
        }
        return true;
    }//moveToPin

    private void setToken(String userId,String compId,String pwd){
        Log.i("jay.LoginActivity","setToken");
        String url = "http://10.0.102.59:31033/api/Authentication/Login";
        JSONObject jsonObject = new JSONObject();
        Map<String,String> headerMap = new HashMap<String,String>();
        int method = PreferenceManager.HTTP_METHOD_POST;
        try {
            jsonObject.put("userid",userId);
            jsonObject.put("compid",compId);
            jsonObject.put("pwd",pwd);
            jsonObject.put("type","M");
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            HttpRequest httpRequest = new HttpRequest(url,headerMap,jsonObject,method);
            new TokenAsyncTask().execute(httpRequest);
        }



    };


    private class TokenAsyncTask extends AsyncTask<HttpRequest,Void,Response>{
        @Override
        protected Response doInBackground(HttpRequest... httpRequests) {
            Response response=null;
            Log.i("jay.LoginActivity","TokenAsyncTask");
            try {
                response=httpRequests[0].post();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            String ltoken=null;
            try {
                ltoken = new JSONObject(response.body().string()).get("ltoken").toString();
                Log.i("jay.LoginActivity","responseToken "+ltoken);
                PreferenceManager.setString(LoginActivity.this,PreferenceManager.L_TOKEN,ltoken);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            tokenFlag = ("".equals(ltoken)||ltoken==null) ? false:true;
            Log.i("jay.LoginActivity","tokenFlag "+tokenFlag);
        }
    }

}
