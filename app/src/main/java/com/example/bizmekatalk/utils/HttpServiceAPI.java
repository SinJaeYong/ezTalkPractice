package com.example.bizmekatalk.utils;

import com.example.bizmekatalk.items.Post;
import com.example.bizmekatalk.items.ProfileItem;
import com.example.bizmekatalk.items.UserInfo;
import com.example.bizmekatalk.items.UserResponse;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HttpServiceAPI {

    @FormUrlEncoded
    @POST("Authentication/Login")
    Call<String> callAPI(@Field("userid") String userid
            , @Field("compid") String compid
            , @Field("pwd") String pwd
            , @Field("type") String type
    );

    @FormUrlEncoded
    @Headers({
            "Content-Type: application/json;charset=utf-8"
    })
    @POST("Authentication/Login")
    Call<String> callAPI(@FieldMap HashMap<String,String> fieldMap);


    @POST("Authentication/Login")
    @Headers({
            "Accept:application/json, text/plain, */*",
            "Content-Type: application/json;charset=utf-8"
    })
    Call<String> callAPI(@Body JsonObject jsonObject);

    @POST("Authentication/Login")
    @Headers({
            "Content-Type: application/json;charset=utf-8"
    })
    Call<String> callAPI(@Body JSONObject jsonObj);

    /*
    @POST("api/Authentication/Login")
    Call<String> createPost(@Body UserInfo userInfo);

     */

    @POST("Authentication/Login")
    @Headers({
            "Accept:application/json, text/plain, */*",
            "Content-Type: application/json;charset=utf-8"
    })
    Call<UserResponse> callAPI(@Body UserInfo userInfo);

    @POST("Authentication/Login")
    @Headers({
            "Accept:application/json, text/plain, */*",
            "Content-Type: application/json;charset=utf-8"
    })
    Call<ResponseBody> callAPI(@Body RequestBody requestBody);
    /*
    @POST("api/Authentication/Login")
    Call<String> createPost(@Body RequestBody requestBody);
     */
    @POST("{path}")
    Call<String> callAPI(@Path("path") String path, @Body String jsonStr);

    @POST("OrgUserInfo/GetAllUserInfo")
    Call<String> callGetAllUserInfoAPI(@Body String jsonStr);

    @FormUrlEncoded
    @POST("/posts")
    Call<Post> postData(@FieldMap HashMap<String,Object> param);



}
