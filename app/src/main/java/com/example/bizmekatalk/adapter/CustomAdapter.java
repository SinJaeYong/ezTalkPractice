package com.example.bizmekatalk.adapter;

import android.util.Log;
import android.widget.BaseAdapter;

import com.example.bizmekatalk.api.common.RequestParams;
import com.example.bizmekatalk.api.webapi.common.WebApiController;
import com.example.bizmekatalk.api.webapi.request.RequestAPI;
import com.example.bizmekatalk.items.Item;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;

public abstract class CustomAdapter extends BaseAdapter {

    @Getter
    @Setter
    protected Map<String,List<JSONObject>> groupedMap = new HashMap<>();

    public LinkedList<String> navi = new LinkedList<>();

    protected List<Item> items = new ArrayList<Item>();

    public abstract void addItems(String result);

    public abstract void updateItems(List<JSONObject> items);


    public abstract void initNavi(String init);





}
