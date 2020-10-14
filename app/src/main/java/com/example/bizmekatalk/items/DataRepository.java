package com.example.bizmekatalk.items;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class DataRepository {

    private static DataRepository uniqueInstance = new DataRepository();

    public static String COMPID = "kaoni";
    public static String COMPNAME = "";

    @Getter
    private LinkedList<String> navi = new LinkedList<String>();

    private Map<String, List<Item>> deptMap;
    private List<Item> userList;


    public synchronized Map<String, List<Item>> getDeptMap() {
        if(deptMap==null){
            try {
                Log.i("jay.DataRepository","wait");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.i("jay.DataRepository","escape wait");
        return deptMap;
    }

    public synchronized void setDeptMap(Map<String, List<Item>> deptMap) {
        this.deptMap = deptMap;
        Log.i("jay.DataRepository","notify");
        notify();
    }

    public List<Item> getUserList() {
        return userList;
    }

    public void setUserList(List<Item> userList) {
        this.userList = userList;
    }

    public static DataRepository getInstance(){
        return uniqueInstance;
    }
}
