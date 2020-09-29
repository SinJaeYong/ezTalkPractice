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

    private Map<String,String> headerMap = new HashMap<String,String>();
    private JSONObject bodyJson = new JSONObject();
    private String body;
    private ApiPath path;

    public void getAPIInfo(Consumer<ApiResult<String,String>> callback){

    }

    public <T> Optional<Call<T>> getCall(RequestParams params) {

        setParams(params);

        return Optional.ofNullable(getCall(createHttpService()));
    }


    private void setParams(RequestParams params) {
        headerMap = params.getHeaderMap();
        bodyJson = params.getBodyJson();
        body = bodyJson.toString();
        path = params.getPath();
    }


    private HttpServiceAPI createHttpService() {
        HttpServiceAPI httpService = new Retrofit.Builder()
                .baseUrl(PreferenceManager.getApiUrl())
                .addConverterFactory( ScalarsConverterFactory.create() )
                .build()
                .create( HttpServiceAPI.class );
        return httpService;
    }


    private <T>Call<T> getCall(HttpServiceAPI httpService) {
        Iterator<String> iterator = path.getPathList().iterator();
        switch ( iterator.next() ){
            case "Authentication" : return (Call<T>)httpService.authenticationAPI( iterator.next(), body.toString() );
            case "OrgUserInfo" : return (Call<T>) httpService.orgUserInfoAPI( iterator.next(), headerMap, body.toString() );
            case "OrgDeptInfo" : return (Call<T>) httpService.orgDeptInfoAPI( iterator.next(), headerMap, body.toString() );
            default: throw new RuntimeException( "No Such request type.");
        }
    }
}
