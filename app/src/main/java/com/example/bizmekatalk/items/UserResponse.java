package com.example.bizmekatalk.items;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class UserResponse {
    @SerializedName("authvalue")
    @Getter @Setter private String authvalue;
    @SerializedName("failcnt")
    @Getter @Setter private String failcnt;
    @SerializedName("ltoken")
    @Getter @Setter private String ltoken;
}
