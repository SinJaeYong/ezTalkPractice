package com.example.bizmekatalk.items;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserItem implements Item{
    private String profileImageUrl;
    private String name;
    private String position;
    private String deptName;

}
