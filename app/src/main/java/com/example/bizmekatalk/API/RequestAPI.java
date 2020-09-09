package com.example.bizmekatalk.API;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.bizmekatalk.utils.HttpServiceAPI;
import com.example.bizmekatalk.utils.PreferenceManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RequestAPI {


    public <T> Call<T> getCall(String path, @Nullable Map<String, String> headerMap, @Nullable JSONObject bodyJson, int method) {

        Map<String,String> optHeaderMap = Optional.ofNullable(headerMap).orElse(new HashMap<String, String>());
        JSONObject optBodyJson = Optional.ofNullable(bodyJson).orElse(new JSONObject());

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .headers(Headers.of(optHeaderMap))
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

        Call<T> call = (Call<T>) httpService.callAPI(path,optBodyJson.toString());
        Log.i("jay.LoginActivity","retrofit request() : "+call.request().toString());
        return call;
    }
}
