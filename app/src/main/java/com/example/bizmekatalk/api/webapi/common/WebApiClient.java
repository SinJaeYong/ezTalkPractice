package com.example.bizmekatalk.api.webapi.common;

import com.example.bizmekatalk.api.common.ServerInfo;
import com.example.bizmekatalk.common.PreferenceManager;


import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import retrofit2.Retrofit;

import retrofit2.converter.scalars.ScalarsConverterFactory;

public class WebApiClient {

    private static List<MyRetrofit> retrofitList = new ArrayList<>();

    public static Retrofit getClient(ServerInfo serverInfo){
        return getOrCreateRetrofit(serverInfo);
    }

    public static Retrofit getDomainClient(){
        boolean isSsl = false;
        ServerInfo dao = new ServerInfo(PreferenceManager.getApiServerDomain(),PreferenceManager.getApiServerPort(),isSsl);
        return getOrCreateRetrofit(dao);
    }

    public static Retrofit getDefaultClient(){
        TalkWebConfig config = TalkWebConfig.getInstance();
        boolean wrong = Strings.isNullOrEmpty(config.getIp()) || Strings.isNullOrEmpty(config.getPort());
        if(wrong){
            throw new RuntimeException("API Server IP Error");
        }

        ServerInfo dao = new ServerInfo(config.getIp(), config.getPort(), config.isSsl());
        return getOrCreateRetrofit(dao);
    }

    private static Retrofit getRetrofit(String baseUrl){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        //client(Intercepter)
    }


    private static Retrofit getOrCreateRetrofit(ServerInfo info ){
        for(MyRetrofit client : retrofitList){
            if(client.isEqual(info)){
                return client.retrofit;
            }
        }

        MyRetrofit my = new MyRetrofit(info);
        retrofitList.add(my);
        return my.retrofit;
    }



    private static class MyRetrofit{
        Retrofit retrofit;
        String baseUrl;

        public MyRetrofit(ServerInfo info){
            baseUrl = baseUrl(info);
            retrofit = getRetrofit(baseUrl);
        }

        boolean isEqual(ServerInfo info){
            return this.baseUrl.equals(baseUrl(info));
        }

        private String baseUrl(ServerInfo info){
            return WebApiUtill.getBaseUrl(info) + "/api/";
        }
    }

}
