package com.example.bizmekatalk.api.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthInfo {
    private String userid;
    private String compid;
    private String ltoken;

    private static final AuthInfo _instance = new AuthInfo();

    public static AuthInfo getInstance(){ return _instance; }

    private AuthInfo() { }

    public void setInfo(String userid,String compid,String ltoken){
        setUserid(userid);
        setCompid(compid);
        setLtoken(ltoken);
    }
}
