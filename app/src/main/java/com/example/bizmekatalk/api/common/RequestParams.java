package com.example.bizmekatalk.api.common;

import com.example.bizmekatalk.api.common.ApiPath;

import org.json.JSONObject;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestParams {
    private ApiPath path;
    private Map<String,String> headerMap;
    private JSONObject bodyJson;
    private int method;
}
