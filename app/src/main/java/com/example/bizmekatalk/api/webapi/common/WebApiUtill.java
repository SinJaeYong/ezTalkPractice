package com.example.bizmekatalk.api.webapi.common;

import com.example.bizmekatalk.api.common.AuthInfo;
import com.example.bizmekatalk.api.common.ServerInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class WebApiUtill {

    public static String getBaseUrl(ServerInfo info){
        TalkWebConfig config = TalkWebConfig.getInstance();
        return config.httpScheme() + info.getIp() + ":" + info.getPort();
    }

    public static String getKeyHeader(){
        AuthInfo info = AuthInfo.getInstance();
        try {
            JSONObject js = new JSONObject();
            js.put("userid",info.getUserid());
            js.put("compid",info.getCompid());
            js.put("ltoken",info.getLtoken());
            return js.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }
}
