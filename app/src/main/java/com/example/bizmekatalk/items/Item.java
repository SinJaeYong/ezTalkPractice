package com.example.bizmekatalk.items;

public abstract class Item {

    public String getType() {
        return this.getClass().getSimpleName();
    }

}
