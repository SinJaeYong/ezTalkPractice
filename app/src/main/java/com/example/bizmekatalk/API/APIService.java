package com.example.bizmekatalk.API;

import androidx.annotation.Nullable;

import com.example.bizmekatalk.utils.PreferenceManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public abstract class APIService {

    protected String path1;
    public APIService(String path1) {
        this.path1 = path1;
    }
    public abstract  <T> Call<T> getCall(String path2, @Nullable Map<String, String> headerMap, @Nullable JSONObject bodyJson, int method);
}
