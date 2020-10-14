package com.example.bizmekatalk.activity;

import android.app.Application;
import android.content.Context;

import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.items.Item;
import com.example.bizmekatalk.items.UserItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class BizmekaApp extends Application {

    private static Context mContext;


    //임시 저장소


    public void onCreate(){
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getAppContext(){
        return mContext;
    }

}
