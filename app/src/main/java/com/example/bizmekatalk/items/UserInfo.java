package com.example.bizmekatalk.items;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class UserInfo {
    @SerializedName("userid")
    @Getter @Setter private String userid;
    @SerializedName("compid")
    @Getter @Setter private String compid;
    @SerializedName("pwd")
    @Getter @Setter private String pwd;
    @SerializedName("type")
    @Getter @Setter private String type;

}
