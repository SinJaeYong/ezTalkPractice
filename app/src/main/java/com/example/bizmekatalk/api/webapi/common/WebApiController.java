package com.example.bizmekatalk.api.webapi.common;


import com.example.bizmekatalk.api.common.ApiResult;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WebApiController<T> implements Callback<T> {

    protected Consumer<ApiResult<T,String>> callback;

    public void request(Call<T> call, Consumer<ApiResult<T,String>> callback){
        this.callback = callback;
        call.enqueue(this);
    }

    private void callBack(ApiResult<T,String> result){
        if(callback == null) return;
        callback.accept(result);
    }

    protected Map<String,String> getHeaders(){
        Map<String,String> map = new HashMap<>();
        map.put("Content-Type", "application/json;charset=utf-8;odata.metadata=full" );
        map.put("key", WebApiUtill.getKeyHeader());
        return map;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(response.isSuccessful()) {
            T t = response.body();
            callBack(ApiResult.onSuccess(t));
        } else {
            callBack(ApiResult.onError(response.errorBody().toString()));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        t.printStackTrace();
        String err = ( call.request() != null) ? call.request().toString() : "Unknown Error.";
        callBack(ApiResult.onError(err));
    }
}
