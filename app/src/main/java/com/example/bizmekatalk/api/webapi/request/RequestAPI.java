package com.example.bizmekatalk.api.webapi.request;

import com.example.bizmekatalk.api.common.ApiPath;
import com.example.bizmekatalk.api.common.RequestParams;
import com.example.bizmekatalk.api.webapi.common.HttpServiceAPI;
import com.example.bizmekatalk.utils.PreferenceManager;
import com.example.bizmekatalk.api.common.ApiResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RequestAPI {

    private HttpServiceAPI httpService;

    public void getAPIInfo(Consumer<ApiResult<String,String>> callback){

    }

    public <T> Optional<Call<T>> getCall(RequestParams params) {


        Map<String,String> headerMap = Optional.ofNullable(params.getHeaderMap()).orElseGet(HashMap<String,String>::new);
        JSONObject optBodyJson = Optional.ofNullable(params.getBodyJson()).orElseGet(JSONObject::new);
        //target version
        //Optional

        /*
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .headers(Headers.of(headerMap))
                    .build();

            return chain.proceed(request);
        });
        OkHttpClient client = httpClient.build();
         */

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PreferenceManager.API_URL)
                .addConverterFactory( ScalarsConverterFactory.create() )
                .build();


        httpService = retrofit.create( HttpServiceAPI.class );
        Call<T> call = getCall(params.getPath(),headerMap,optBodyJson.toString());
        Optional<Call<T>> optCall = Optional.ofNullable(call);
        return optCall;
    }

    private <T>Call<T> getCall( ApiPath path, Map<String,String> headerMap, String body ) {
        Iterator<String> iterator = path.getPathList().iterator();
        switch ( iterator.next() ){
            case "Authentication" : return (Call<T>)httpService.authenticationAPI( iterator.next(),body );
            case "OrgUserInfo" : return (Call<T>) httpService.orgUserInfoAPI( iterator.next(),headerMap,body );
            case "OrgDeptInfo" : return (Call<T>) httpService.orgDeptInfoAPI( iterator.next(),headerMap,body );
            default: throw new RuntimeException( "No Such request type.");
        }
    }
}
