package com.example.bizmekatalk.items;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeptItem implements Item{
    private String deptName;
    private String isLeaf;
    private String deptId;
    private String parentDeptId;

    public DeptItem(){};

    public DeptItem(JSONObject deptData){
        try {
            if (deptData.has("deptid")) {
                deptId = deptData.getString("deptid");
                deptName = deptData.getString("deptname");
                isLeaf = deptData.getString("isleaf");
                parentDeptId = deptData.getString("parentdeptid");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
