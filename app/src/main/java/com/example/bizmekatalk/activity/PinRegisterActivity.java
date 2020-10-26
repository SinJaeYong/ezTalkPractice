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

import com.example.bizmekatalk.api.common.RequestParamBuilder;
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

    private final int MSG_USER_FLAG = 0 ;
    private final int MSG_DEPT_FLAG = 1 ;
    private Handler mHandler;

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
            mHandler = new Handler(Looper.myLooper()){
                boolean userFlag = false;
                boolean deptFlag = false;
                @Override
                public void handleMessage(@NonNull Message msg) {
                    Log.i("jay.PinLoginActivity","handleMessage");
                    Log.i("jay.PinLoginActivity","msg.what : "+msg.what);
                    switch (msg.what){
                        case MSG_USER_FLAG:
                            userFlag = true;
                            Log.i("jay.PinLoginActivity","userFlag");
                            break;
                        case MSG_DEPT_FLAG:
                            deptFlag = true;
                            break;
                        default:
                            break;
                    }
                    if(userFlag&&deptFlag){
                        Log.i("jay.PinLoginActivity","okFLag");
                        PreferenceManager.setString(PreferenceManager.getPinKey(), binding.pinNum.getText().toString());
                        Intent intent = new Intent(PinRegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            setAllUserInfo(new RequestParamBuilder().getAllUserInfoParam().build());
            setAllDeptInfo(new RequestParamBuilder().getAllDeptInfoParam().build());
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

    private void setAllDeptInfo(RequestParams params) {
        new RequestAPI().<String>getCall(params).ifPresent(call->{
            new WebApiController<String>().request(call,(result)->{
                Log.i("jay.PinLoginActivity","setAllDeptInfo");
                if(result.isSuccess()){
                    try {
                        JSONArray jsonArr = new JSONArray(result.getData());
                        for(int i = 0 ; i < jsonArr.length() ; i++){
                            JSONObject json = new JSONObject(jsonArr.get(i).toString());
                            BizmekaApp.deptList.add(new DeptItem(json));
                        }
                        Message message = mHandler.obtainMessage(MSG_DEPT_FLAG);
                        mHandler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("jay.PinLoginActivity","에러. response : "+result.getError());
                }
            });
        });
    }


    private void setAllUserInfo(RequestParams params) {
        new RequestAPI().<String>getCall(params).ifPresent(call->{
            new WebApiController<String>().request(call,(result)->{
                Log.i("jay.PinLoginActivity","setAllUserInfo");
                if(result.isSuccess()){
                    try {
                        JSONArray jsonArr = new JSONArray(result.getData());
                        for(int i = 0 ; i < jsonArr.length() ; i++){
                            JSONObject json = new JSONObject(jsonArr.get(i).toString());
                            BizmekaApp.userList.add(new UserItem(json));
                        }
                        Message message = mHandler.obtainMessage(MSG_USER_FLAG);
                        mHandler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("jay.PinLoginActivity","에러. response : "+result.getError());
                }
            });
        });
    }

}
