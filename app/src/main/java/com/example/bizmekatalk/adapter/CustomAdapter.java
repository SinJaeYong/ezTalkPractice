package com.example.bizmekatalk.adapter;

import android.util.Log;
import android.widget.BaseAdapter;

import com.example.bizmekatalk.api.common.RequestParams;
import com.example.bizmekatalk.api.webapi.common.WebApiController;
import com.example.bizmekatalk.api.webapi.request.RequestAPI;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import retrofit2.Call;

public abstract class CustomAdapter<T> extends BaseAdapter {

    protected List<T> items = new ArrayList<T>();

    public abstract void addItems(String result);

    public abstract void updateItems(List<JSONObject> items);






}
