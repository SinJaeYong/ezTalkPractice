package com.example.bizmekatalk.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.bizmekatalk.api.common.ApiPath;
import com.example.bizmekatalk.api.common.RequestParamBuilder;
import com.example.bizmekatalk.api.webapi.common.WebApiController;
import com.example.bizmekatalk.api.webapi.request.RequestAPI;
import com.example.bizmekatalk.R;
import com.example.bizmekatalk.adapter.ProfileListAdapter;
import com.example.bizmekatalk.fragment.ChatFragment;
import com.example.bizmekatalk.fragment.GroupFragment;
import com.example.bizmekatalk.fragment.OrganFragment;
import com.example.bizmekatalk.fragment.SettingFragment;
import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.items.Item;
import com.example.bizmekatalk.items.UserItem;
import com.example.bizmekatalk.api.common.RequestParams;
import com.example.bizmekatalk.common.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity{

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private GroupFragment groupFragment = new GroupFragment();
    private ChatFragment chatFragment = new ChatFragment();
    private OrganFragment organFragment = new OrganFragment();
    private SettingFragment settingFragment = new SettingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BizmekaApp.deptMap = deptListToMap(BizmekaApp.deptList);
        BizmekaApp.userMap = userListToMap(BizmekaApp.userList);

        setContentView(R.layout.main_activity);

        getSupportActionBar().hide();

        configureBottomNavigation();
    }



    private Map<String, List<Item>> userListToMap (List<Item> itemList){
        return itemList.stream().collect(Collectors.groupingBy(item -> ((UserItem)item).getDeptId()));
    }

    private Map<String, List<Item>> deptListToMap (List<Item> itemList){
        return itemList.stream().collect(Collectors.groupingBy(item -> ((DeptItem)item).getParentDeptId()));
    }

    private void configureBottomNavigation() {
        setBottomNavigationViewListener(configureBottomNavigationLayout());
    }

    private BottomNavigationView configureBottomNavigationLayout() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, organFragment).commitAllowingStateLoss();
        bottomNavigationView.setSelectedItemId(R.id.navigation_organ);
        return bottomNavigationView;
    }

    private void setBottomNavigationViewListener(BottomNavigationView bottomNavigationView) {
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