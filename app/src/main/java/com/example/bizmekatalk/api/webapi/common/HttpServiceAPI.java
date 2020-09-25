package com.example.bizmekatalk.api.webapi.common;



import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HttpServiceAPI {


    @POST("Authentication/{path}")
    Call<String> authenticationAPI(@Path("path") String path, @Body String jsonStr);


    @POST("OrgDeptInfo/{path}")
    Call<String> orgDeptInfoAPI(@Path("path") String path, @HeaderMap Map<String,String> headers, @Body String jsonStr);

    @POST("OrgUserInfo/{path}")
    Call<String> orgUserInfoAPI(@Path("path") String path,@HeaderMap Map<String,String> headers, @Body String jsonStr);



}
