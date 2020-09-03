package com.example.bizmekatalk.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.utils.PreferenceManager;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        getSupportActionBar().hide();
        /*
        ImageView imageView = findViewById(R.id.loading);
        Animation ani = AnimationUtils.loadAnimation(this, R.anim.appear_logo);
        imageView.startAnimation(ani);
         */
        Handler handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Intent intent;
                if("".equals(PreferenceManager.getString(Splash.this,"pin"))){
                    intent = new Intent(Splash.this, LoginActivity.class);
                }else{
                    intent = new Intent(Splash.this,PinLoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        };
        if(handler != null){
            handler.sendEmptyMessageDelayed(0,500);
        }else{
            Log.i("jay","Splash 오류");
        }
    }
}
