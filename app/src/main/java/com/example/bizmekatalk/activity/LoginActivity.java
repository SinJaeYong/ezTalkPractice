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
import com.example.bizmekatalk.utils.CustomDialog;
import com.example.bizmekatalk.utils.HttpRequest;
import com.example.bizmekatalk.utils.PreferenceManager;
import com.example.bizmekatalk.utils.SoftKeyboard;
import com.example.bizmekatalk.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

        if(compIdEdit != null){
            compIdEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    moveToPinRegister();
                    return true;
                }
            });
        }else Log.i(PreferenceManager.TAG,"회사아이디 위젯 오류");


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


    private boolean moveToPinRegister() {
        if(Validation.validateLogin(userIdEdit,userPwdEdit,compIdEdit)){
            final String userId = "fhZ6hZSfV5UG/CjJEyTsUA==";
            final String compId = "P1Ao25+VqxuNq9ijelCnnw==";
            final String pwd = "1dBcLJsXZJkGl/WdAxYtcw==";
            setToken(userId,compId,pwd);



            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(tokenFlag){
                        PreferenceManager.setString(LoginActivity.this,PreferenceManager.USER_ID,userId);
                        PreferenceManager.setString(LoginActivity.this,PreferenceManager.COMP_ID,compId);
                        Intent intent = new Intent(LoginActivity.this, PinRegisterActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Log.i(PreferenceManager.TAG,"token 에러");
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


    //ltoken 요청 및 Preference에 저장
    private void setToken(String userId,String compId,String pwd){
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


    //비동기적으로 ltoken값을 전달받아 Preference에 저장
    private class TokenAsyncTask extends AsyncTask<HttpRequest,Void,Response>{
        @Override
        protected Response doInBackground(HttpRequest... httpRequests) {
            Response response=null;
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
                PreferenceManager.setString(LoginActivity.this,PreferenceManager.L_TOKEN,ltoken);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            //토큰값 저장을 알리는 Flag
            tokenFlag = ("".equals(ltoken)||ltoken==null) ? false:true;
            Log.i(PreferenceManager.TAG,"tokenFlag "+tokenFlag);
        }
    }

}
