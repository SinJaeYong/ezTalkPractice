package com.example.bizmekatalk.items;

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


}
