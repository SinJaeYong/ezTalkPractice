package com.example.bizmekatalk.items;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bizmekatalk.common.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserItem implements Item, Parcelable {
    private String userId;
    private String deptId;
    private String name;
    private String position;
    private String deptName;
    private String profileImage;
    private String email;
    private String mobile;
    private String tel;
    private String status;
    private String message;
    private String orderBy;

    public UserItem(){ }

    public UserItem(JSONObject userData){
        try {
            if (userData.has("userid")) {
                userId = userData.getString("userid");
                deptId = userData.getString("deptid");
                name = userData.getString("name");
                position = userData.getString("position");
                deptName = "";
                profileImage = PreferenceManager.getUploadUrl() + userData.getString("profileimage");
                email = userData.getString("email");
                mobile = userData.getString("mobile");
                tel = userData.getString("tel");
                status = userData.getString("status");
                message = userData.getString("message");
                orderBy = userData.getString("orderby");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    protected UserItem(Parcel in) {
        userId = in.readString();
        deptId = in.readString();
        name = in.readString();
        position = in.readString();
        deptName = in.readString();
        profileImage = in.readString();
        email = in.readString();
        mobile = in.readString();
        tel = in.readString();
        status = in.readString();
        message = in.readString();
        orderBy = in.readString();
    }

    public static final Creator<UserItem> CREATOR = new Creator<UserItem>() {
        @Override
        public UserItem createFromParcel(Parcel in) {
            return new UserItem(in);
        }

        @Override
        public UserItem[] newArray(int size) {
            return new UserItem[size];
        }
    };

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(deptId);
        dest.writeString(name);
        dest.writeString(position);
        dest.writeString(deptName);
        dest.writeString(profileImage);
        dest.writeString(email);
        dest.writeString(mobile);
        dest.writeString(tel);
        dest.writeString(status);
        dest.writeString(message);
        dest.writeString(orderBy);
    }
}
