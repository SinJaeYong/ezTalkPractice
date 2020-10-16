package com.example.bizmekatalk.api.common;

import com.example.bizmekatalk.common.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestParamBuilder {
    private ApiPath path;
    private JSONObject bodyJson = new JSONObject();
    private Map<String,String> headerMap = new HashMap<String,String>();
    private int method;

    public RequestParamBuilder() {

        method = PreferenceManager.getHttpMethodPost();

        JSONObject keyJson = new JSONObject();

        try {
            keyJson.put("userid",PreferenceManager.getString(PreferenceManager.getUserId()));
            keyJson.put("compid",PreferenceManager.getString(PreferenceManager.getCompId()));
            keyJson.put("ltoken",PreferenceManager.getString(PreferenceManager.getlToken()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        headerMap.put("Content-Type","application/json;odata.metadata=full");
        headerMap.put("Expect","100-continue");
        headerMap.put("key",keyJson.toString());

    }


    public RequestParamBuilder addHeader(String key, String value){
        headerMap.put(key,value);
        return this;
    }


    public RequestParamBuilder setHeaderMap(Map<String,String> headerMap){

        return this;
    }


    public RequestParamBuilder setPath(ApiPath path){
        this.path = path;
        return this;
    }


    public <T>RequestParamBuilder setBodyJson(String name,T value){
        try {
            bodyJson.put(name,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }


    public RequestParamBuilder setBodyJson(String name,String value){
        try {
            bodyJson.put(name,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }


    public RequestParamBuilder setBodyJson(String name,int value){
        try {
            bodyJson.put(name,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }


    public RequestParamBuilder setMethod(int method){
        this.method = method;
        return  this;
    }

    public RequestParamBuilder getAllDeptInfoParam(){
        setPath(new ApiPath("OrgDeptInfo","GetAllDeptInfo"));
        setBodyJson("compid", PreferenceManager.getString(PreferenceManager.getCompId()));
        setBodyJson("lan",1);
        return this;
    }

    public RequestParamBuilder getAllUserInfoParam(){
        setPath(new ApiPath("OrgUserInfo","GetAllUserInfo"));
        setBodyJson("compid", PreferenceManager.getString(PreferenceManager.getCompId()));
        setBodyJson("lan",1);
        return this;
    }


    public RequestParams build(){
        RequestParams params = new RequestParams(path,headerMap,bodyJson,method);
        return params;
    }



}
