package com.example.bizmekatalk.utils;


import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequest {

    private String url;
    private Map<String,String> headerMap;
    private JSONObject bodyJson;
    private int method;

    private Response result;


    public HttpRequest(String url, Map<String, String> headerMap, JSONObject bodyJson, int method) {
        this.url = url;
        this.headerMap = headerMap;
        this.bodyJson = bodyJson;
        this.method = method;
    }

    //request post방식으로 보내기
    public Response post() throws IOException {
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(5, TimeUnit.MINUTES).
                writeTimeout(5,TimeUnit.MINUTES).
                readTimeout(5,TimeUnit.MINUTES).
                build();
        //addInterceptor(new LoggingInterceptor())
        RequestBody body = RequestBody.create(JSON,bodyJson.toString());
        Request request = new Request.Builder().url(url).headers(Headers.of(headerMap)).post(body).build();
        Log.i(PreferenceManager.TAG,"<requestHeader>\r\n"+request.headers().toString());
        Response response = client.newCall(request).execute();
        Log.i(PreferenceManager.TAG,"<responseHeader>\r\n"+response.headers().toString());
        return response;
    }





    public class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            Log.i(PreferenceManager.TAG,String.format("LoggingInterceptor Sending Request %s on  %s , %n, %s",
                    request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            Log.i(PreferenceManager.TAG,String.format(" LoggingInterceptor Received Response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    }//LoggingInterceptor
}
