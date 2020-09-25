package com.example.bizmekatalk.items;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfo {
    @SerializedName("userid")
    private String userid;
    @SerializedName("compid")
    private String compid;
    @SerializedName("pwd")
    private String pwd;
    @SerializedName("type")
    private String type;

}
