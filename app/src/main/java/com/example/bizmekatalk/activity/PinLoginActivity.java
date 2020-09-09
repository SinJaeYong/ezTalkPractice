package com.example.bizmekatalk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.utils.PreferenceManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class PinLoginActivity extends AppCompatActivity {
    private GridLayout pinGrid;
    private LinearLayout pinLinear;
    Animation pinDotsAni;
    private TextView pinRemove;
    private TextView pinLogin;
    private List<ImageView> pinDots = new Vector<ImageView>();
    private List<TextView> pinBtns = new Vector<TextView>();
    private LinkedList<Integer> pinPassList = new LinkedList<Integer>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_login_activity);
        getSupportActionBar().hide();
        pinGrid = findViewById(R.id.pinGrid);
        pinLinear = findViewById(R.id.pinLinear);
        pinDotsAni = new TranslateAnimation(-20.0f,20.0f,0.0f,0.0f);
        pinDotsAni.setDuration(50);
        pinDotsAni.setRepeatMode(Animation.REVERSE);
        pinDotsAni.setRepeatCount(4);

        //핀 Dot 이미지 생성
        if(pinLinear!=null){
            for(int i=0; i<4; i++){
                pinDots.add((ImageView)pinLinear.findViewWithTag("pinDot"+String.valueOf(i)));
            }
        }else {
            Log.i(PreferenceManager.TAG,"pinDots 생성 오류");
        }

        //핀 숫자버튼 생성
        if(pinGrid != null){
            for(int i = 0; i<10 ; i++){
                TextView pin = pinGrid.findViewWithTag("pin"+String.valueOf(i));
                final int pinNumber = i;
                //숫자 버튼 클릭시
                pin.setOnClickListener(view -> {
                    //핀 Dot 이미지 변경 및 LinkedList에 핀값 저장
                    setPins(pinNumber,PreferenceManager.PIN_MAX_COUNT);
                });
                pinBtns.add(pin);
            }
        }else {
            Log.i(PreferenceManager.TAG,"pinGrid 생성 오류");
        }

        //지우기 버튼
        pinRemove = findViewById(R.id.pinRemove);
        if(pinRemove != null){
            pinRemove.setOnClickListener(view -> {
                if(pinPassList.size()>0){
                    //핀 Dot 이미지 변경 및 LinkedList에 핀값 삭제
                    pinDots.get(pinPassList.size()).setImageDrawable(getResources().getDrawable(R.drawable.shape_round_black,null));
                    pinPassList.removeLast();
                }
            });
        }else{
            Log.i(PreferenceManager.TAG,"pinRemove 오류");
        }


        //로그인 버튼
        pinLogin = findViewById(R.id.pinLogin);
        if(pinLogin != null){
            pinLogin.setOnClickListener(view -> {
                //LoginActivity 화면으로 이동
                PreferenceManager.clear(PinLoginActivity.this);
                Intent intent = new Intent(PinLoginActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
        }else {
            Log.i(PreferenceManager.TAG,"핀로그인에서 로그인 이동 오류");
        }

    }//onCreate


    //핀값 저장 및 핀 이미지 변경경
    private void setPins(int pinNumber,int maxCount) {
        pinPassList.addLast(pinNumber);
        pinDots.get(pinPassList.size()-1).setImageDrawable(getResources().getDrawable(R.drawable.shape_round_blue,null));
        Handler mHandler = new Handler(Looper.myLooper());
        if(pinPassList.size() == maxCount) {
            mHandler.postDelayed(() -> validatePins(), 200); // 0.5초후
        }
    }///setPins(int pinNumber,int maxCount)



    //핀값 검증
    private void validatePins(){
        String pass = PreferenceManager.getString(PinLoginActivity.this,"pin");
        StringBuffer buffer = new StringBuffer();

        for(int i=0; i<pinPassList.size(); i++){
            pinDots.get(i).setImageDrawable(getResources().getDrawable(R.drawable.shape_round_black,null));
            buffer.append(pinPassList.get(i));
        }

        if(pass.equals(buffer.toString())){
            Intent intent = new Intent(PinLoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            pinLinear.startAnimation(pinDotsAni);
            Log.i(PreferenceManager.TAG,"핀 로그인 입력 오류");
        }

        pinPassList.clear();
    }//validatePins()
}
