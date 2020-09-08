package com.example.bizmekatalk.utils;


import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//리트로핏
public class HttpRequest<T> {

    private String url;
    private Map<String,String> headerMap=new HashMap<String,String>();
    private JSONObject bodyJson;
    private int method;

    public HttpRequest(String url, JSONObject bodyJson, int method) {
        this.url = url;
        this.bodyJson = bodyJson;
        this.method = method;
    }

    public HttpRequest(String url, Map<String, String> headerMap, JSONObject bodyJson, int method) {
        this(url,bodyJson,method);
        this.headerMap = headerMap;
    }

    //request post방식으로 보내기
    public Call<T> getCall(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .headers(Headers.of(headerMap))
                    .build();

            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PreferenceManager.BASE_API_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory( GsonConverterFactory.create())
                .client(client)
                .build();
        HttpServiceAPI httpService = retrofit.create(HttpServiceAPI.class);

      //  Call<T> call = (Call<T>) httpService.callLoginAPI(bodyJson.toString());
      //  Log.i("jay.LoginActivity","retrofit request() : "+call.request().toString());
        return null;
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
