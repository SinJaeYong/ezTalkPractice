package com.example.bizmekatalk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BaseInterpolator;
import android.view.animation.CycleInterpolator;
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
        setContentView(R.layout.activity_pin_login);
        getSupportActionBar().hide();
        pinGrid = findViewById(R.id.pinGrid);
        pinLinear = findViewById(R.id.pinLinear);
        pinDotsAni = new TranslateAnimation(-20.0f,20.0f,0.0f,0.0f);
        pinDotsAni.setDuration(50);
        pinDotsAni.setRepeatMode(Animation.REVERSE);
        pinDotsAni.setRepeatCount(4);

        if(pinLinear!=null){
            for(int i=0; i<4; i++){
                pinDots.add((ImageView)pinLinear.findViewWithTag("pinDot"+String.valueOf(i)));
            }
        }else {
            Log.i("jay","pinDots 생성 오류");
        }

        if(pinGrid != null){
            for(int i = 0; i<10 ; i++){
                TextView pin = pinGrid.findViewWithTag("pin"+String.valueOf(i));
                final int pinNumber = i;
                pin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setPins(pinNumber,PreferenceManager.PIN_MAX_COUNT);
                    }
                });
                pinBtns.add(pin);
            }
        }else {
            Log.i("jay","pinGrid 생성 오류");
        }



        pinRemove = findViewById(R.id.pinRemove);
        if(pinRemove != null){
            pinRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(pinPassList.size()>0){
                        pinPassList.removeLast();
                        pinDots.get(pinPassList.size()).setImageDrawable(getResources().getDrawable(R.drawable.shape_round_black,null));
                    }
                }
            });
        }else{
            Log.i("jay","pinRemove 오류");
        }



        pinLogin = findViewById(R.id.pinLogin);
        if(pinLogin != null){
            pinLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PreferenceManager.clear(PinLoginActivity.this);
                    Intent intent = new Intent(PinLoginActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }else {
            Log.i("jay","핀로그인에서 로그인 이동 오류");
        }

        BaseInterpolator baseInterpolator = new BaseInterpolator() {
            @Override
            public float getInterpolation(float v) {
                return 0;
            }
        };
        CycleInterpolator cycleInterpolator = new CycleInterpolator(12);
        cycleInterpolator.getInterpolation(100);
    }//onCreate


    private void setPins(int pinNumber,int maxCount) {
        pinPassList.addLast(pinNumber);
        pinDots.get(pinPassList.size()-1).setImageDrawable(getResources().getDrawable(R.drawable.shape_round_blue,null));
        Handler mHandler = new Handler(Looper.myLooper());
        if(pinPassList.size()==maxCount) {
            mHandler.postDelayed(new Runnable()  {
                public void run() {
                    validatePins();
                }
            }, 200); // 0.5초후
        }
    }///setPins(int pinNumber,int maxCount)



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
            Log.i("jay","핀 로그인 입력 오류");
        }

        pinPassList.clear();
    }//validatePins()
}
