package com.example.bizmekatalk.items;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("authvalue")
    private String authvalue;
    @SerializedName("failcnt")
    private String failcnt;
    @SerializedName("ltoken")
    private String ltoken;

    public String getAuthvalue() {
        return authvalue;
    }

    public void setAuthvalue(String authvalue) {
        this.authvalue = authvalue;
    }

    public String getFailcnt() {
        return failcnt;
    }

    public void setFailcnt(String failcnt) {
        this.failcnt = failcnt;
    }

    public String getLtoken() {
        return ltoken;
    }

    public void setLtoken(String ltoken) {
        this.ltoken = ltoken;
    }
}
