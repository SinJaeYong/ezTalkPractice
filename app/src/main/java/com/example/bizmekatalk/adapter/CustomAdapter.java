package com.example.bizmekatalk.adapter;

import android.widget.BaseAdapter;

import com.example.bizmekatalk.items.Item;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomAdapter extends BaseAdapter {


    protected List<Item> items = new ArrayList<Item>();

    public abstract void setData(List<Item> items);



}
