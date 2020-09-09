package com.example.bizmekatalk.items;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

public @Data class Post {
    private int userId;
    private int id;
    private String title;
    private String body;
}
