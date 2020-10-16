package com.example.bizmekatalk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.api.common.RequestParamBuilder;
import com.example.bizmekatalk.api.common.RequestParams;
import com.example.bizmekatalk.api.webapi.common.WebApiController;
import com.example.bizmekatalk.api.webapi.request.RequestAPI;
import com.example.bizmekatalk.databinding.PinLoginActivityBinding;
import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.items.UserItem;
import com.example.bizmekatalk.common.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class PinLoginActivity extends AppCompatActivity {

    private final int MSG_USER_FLAG = 0 ;
    private final int MSG_DEPT_FLAG = 1 ;

    private boolean pinLock=false;

    private Animation pinDotsAni;
    private List<ImageView> pinDots = new Vector<ImageView>();
    private List<TextView> pinBtns = new Vector<TextView>();
    private LinkedList<Integer> pinPassList = new LinkedList<Integer>();
    private PinLoginActivityBinding binding;

    private Handler mHandler;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDataBinding();

        setPinDotsImage();

        setPinButtons();

        setListener();
    }//onCreate



    private void setDataBinding() {
        binding = DataBindingUtil.setContentView(this,R.layout.pin_login_activity);
        binding.setPinLoginActivity(this);
        getSupportActionBar().hide();

    }

    private void setListener() {
        //지우기 버튼
        binding.pinRemove.setOnClickListener(view -> {
            if(pinPassList.size()>0){
                //핀 Dot 이미지 변경 및 LinkedList에 핀값 삭제
                pinPassList.removeLast();
                pinDots.get(pinPassList.size()).setImageDrawable(getResources().getDrawable(R.drawable.shape_round_black,null));
            }
        });

        //로그인 버튼 동작
        binding.pinLogin.setOnClickListener(view -> {
            //LoginActivity 화면으로 이동
            PreferenceManager.clear();
            Intent intent = new Intent(PinLoginActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setPinButtons() {
        for(int i = 0; i< PreferenceManager.getPinButtonCount() ; i++){
            TextView pin = binding.pinButtons.pinGrid.findViewWithTag("pin"+String.valueOf(i));
            final int pinNumber = i;
            //숫자 버튼 클릭시
            pin.setOnClickListener(view -> {
                //핀 Dot 이미지 변경 및 LinkedList에 핀값 저장
                setPins(pinNumber,PreferenceManager.getPinMaxCount());
            });
            pinBtns.add(pin);
        }

    }

    private void setPinDotsImage() {
        //도트 부분 애니메이션 처리
        pinDotsAni = new TranslateAnimation(-20.0f,20.0f,0.0f,0.0f);
        pinDotsAni.setDuration(50);
        pinDotsAni.setRepeatMode(Animation.REVERSE);
        pinDotsAni.setRepeatCount(4);
        //핀 Dot 이미지 생성
        for(int i = 0; i< PreferenceManager.getPinMaxCount(); i++){
            pinDots.add((ImageView)binding.pinLinear.findViewWithTag("pinDot"+String.valueOf(i)));
        }
    }


    //핀값 저장 및 핀 이미지 변경
    private void setPins(int pinNumber,int maxCount) {

        if(pinLock) return;

        pinPassList.addLast(pinNumber);
        pinDots.get(pinPassList.size()-1).setImageDrawable(getResources().getDrawable(R.drawable.shape_round_blue,null));

        if(pinPassList.size() == maxCount) {
            pinLock = true;
            //new Handler(Looper.myLooper()).postDelayed(() -> moveToMain(), 200);
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
                        moveToMain();
                    }
                }
            };
            setAllUserInfo(new RequestParamBuilder().getAllUserInfoParam().build());
            setAllDeptInfo(new RequestParamBuilder().getAllDeptInfoParam().build());
        }


    }///setPins(int pinNumber,int maxCount)



    //
    private boolean validatePin(){

        //사용자 핀 설정
        String pass = PreferenceManager.getString(PreferenceManager.getPinKey());

        //입력 핀 설정
        StringBuffer buffer = new StringBuffer();
        for(int i=0; i<pinPassList.size(); i++){
            pinDots.get(i).setImageDrawable(getResources().getDrawable(R.drawable.shape_round_black,null));
            buffer.append(pinPassList.get(i));
        }

        return pass.equals(buffer.toString());

    }

    private void moveToMain(){

        if(validatePin()){
            Intent intent = new Intent(PinLoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            binding.pinLinear.startAnimation(pinDotsAni);
            Log.i(PreferenceManager.TAG,"핀 로그인 입력 오류");
            pinLock = false;
        }

        pinPassList.clear();
    }//validatePins()


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
