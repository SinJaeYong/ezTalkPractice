package com.example.bizmekatalk.items;

import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("userid")
    private String userid;
    @SerializedName("compid")
    private String compid;
    @SerializedName("pwd")
    private String pwd;
    @SerializedName("type")
    private String type;

    public UserInfo(String userid, String compid, String pwd, String type) {
        this.userid = userid;
        this.compid = compid;
        this.pwd = pwd;
        this.type = type;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCompid() {
        return compid;
    }

    public void setCompid(String compid) {
        this.compid = compid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
