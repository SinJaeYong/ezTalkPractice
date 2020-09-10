package com.example.bizmekatalk.items;

import org.json.JSONObject;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class RequestParams {
    @Getter @Setter private String path;
    @Getter @Setter private Map<String,String> headerMap;
    @Getter @Setter private JSONObject bodyJson;
    @Getter @Setter private int method;
}
