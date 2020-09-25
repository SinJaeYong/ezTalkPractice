package com.example.bizmekatalk.api.webapi.request;

import com.example.bizmekatalk.api.common.ApiResult;
import com.example.bizmekatalk.api.webapi.common.HttpServiceAPI;
import com.example.bizmekatalk.api.webapi.common.WebApiClient;
import com.example.bizmekatalk.api.webapi.common.WebApiController;

import java.util.function.Consumer;

import retrofit2.Retrofit;


public class NewRequest extends WebApiController {
    /*
    public HttpServiceAPI getApi(){
        Retrofit retrofit = WebApiClient.getDomainClient();
        return retrofit.create(HttpServiceAPI.class);
    }

    public void getAPIInfo(Consumer<ApiResult<String,String>> callback){
        request(
                getApi().getAPIInfo(getHeaders()),callback
        );
    }

    public void checkedIPPermit(String body, Consumer<ApiResult<String,String>> callback){
        request(
                getApi().checkedIPPermit(getHeaders(), body),callback
        );
    }

     */
}
