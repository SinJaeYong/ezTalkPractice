package com.example.bizmekatalk.API;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.bizmekatalk.items.RequestParams;
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


    public <T> Optional<Call<T>> getCall(RequestParams params) {

        Map<String,String> optHeaderMap = Optional.ofNullable(params.getHeaderMap()).orElseGet(HashMap<String,String>::new);
        JSONObject optBodyJson = Optional.ofNullable(params.getBodyJson()).orElseGet(JSONObject::new);
        //target version
        //Optional

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

        Call<T> call = (Call<T>) httpService.callAPI(params.getPath(),optBodyJson.toString());
        Log.i("jay.LoginActivity","retrofit request() : "+call.request().toString());
        Optional<Call<T>> optCall = Optional.ofNullable(call);
        return optCall;
    }
}
