package com.example.bizmekatalk.api.common;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResult<T, E> {
    private boolean success;
    private int code;
    private T data;
    private E error;

    public static <T,E> ApiResult<T,E> onError(E error){
        ApiResult<T,E> result = new ApiResult<>();
        result.success = false;
        result.error = error;
        return result;
    }

    public static<T,E> ApiResult<T,E> onError(E error, int code){
        ApiResult<T,E> result = ApiResult.onError(error);
        result.code = code;
        return result;
    }

    public static ApiResult<String,String> onUnknownError(){
        return ApiResult.onError("Unknown Error",500);
    }

    public static<T,E> ApiResult<T,E> onSuccess(T data){
        ApiResult<T,E> result = new ApiResult<>();
        result.data = data;
        result.success = true;
        return result;
    }

    public static<T,E> ApiResult<T,E> onSuccess(T data,int code){
        ApiResult<T,E> result = ApiResult.onSuccess(data);
        result.code = code;
        return result;
    }

    private <T,E> ApiResult(){}

}
