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

public class BizmekaApp extends Application {

    private static Context mContext;


    public static String COMPID = "kaoni";
    public static String COMPNAME = "";

    public static LinkedList<String> navi = new LinkedList<>();
    public static List<Item> deptList = new ArrayList<>();
    public static List<Item> userList = new ArrayList<>();
    public static Map<String, List<Item>> deptMap;
    public static Map<String, List<Item>> userMap;

    public void onCreate(){
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getAppContext(){
        return mContext;
    }

}
