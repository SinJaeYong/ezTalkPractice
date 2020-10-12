package com.example.bizmekatalk.adapter;

import android.util.Log;
import android.widget.BaseAdapter;

import com.example.bizmekatalk.api.common.RequestParams;
import com.example.bizmekatalk.api.webapi.common.WebApiController;
import com.example.bizmekatalk.api.webapi.request.RequestAPI;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import retrofit2.Call;

public abstract class CustomAdapter<T> extends BaseAdapter {

    protected List<T> items = new ArrayList<T>();

    public abstract void addItems(String result);

    public abstract void clearItems();

    public abstract void resetItems(String result);

    public abstract void updateItems(List<T> items);

    public void updateAdapter(@Nullable RequestParams params, String mode) {
        //Request 동작
        if("clear".equals(mode)){
            clearItems();
            notifyDataSetChanged();
            return;
        }
        Log.i("jay.CustomAdapter","asdfadfs");

    }





}
