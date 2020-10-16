package com.example.bizmekatalk.items;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserItem extends Item{
    private String userId;
    private String deptId;
    private String name;
    private String position;
    private String deptName;
    private String profileImage;
    public UserItem(){

    }

    public UserItem(JSONObject userData){
        try {
            if (userData.has("userid")) {
                userId = userData.getString("userid");
                deptId = userData.getString("deptid");
                name = userData.getString("name");
                position = userData.getString("position");
                deptName = "";
                profileImage = userData.getString("profileimage");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
