package com.example.bizmekatalk.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.util.Log;

import com.example.bizmekatalk.R;

import com.example.bizmekatalk.fragment.ChatFragment;
import com.example.bizmekatalk.fragment.GroupFragment;
import com.example.bizmekatalk.fragment.OrganFragment;
import com.example.bizmekatalk.fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class MainActivity extends AppCompatActivity{

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private GroupFragment groupFragment = new GroupFragment();
    private ChatFragment chatFragment = new ChatFragment();
    private OrganFragment organFragment = new OrganFragment();
    private SettingFragment settingFragment = new SettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        getSupportActionBar().hide();

        configureBottomNavigation();
    }




    private void configureBottomNavigation() {
        setBottomNavigationViewListener(configureBottomNavigationLayout());
    }


    private BottomNavigationView configureBottomNavigationLayout() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, organFragment).commitAllowingStateLoss();
        if( bottomNavigationView != null ){
            bottomNavigationView.setSelectedItemId(R.id.navigation_organ);
        }
        return bottomNavigationView;
    }


    private void setBottomNavigationViewListener(BottomNavigationView bottomNavigationView) {
        if( bottomNavigationView != null){
            bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
                FragmentTransaction trans = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_group: {
                        trans.replace(R.id.frame_layout, groupFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_chat: {
                        trans.replace(R.id.frame_layout, chatFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_organ: {
                        Log.i("MainActivity.jay","organ");
                        trans.replace(R.id.frame_layout, organFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_setting: {
                        trans.replace(R.id.frame_layout, settingFragment).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            });
        }
    }
}