package com.example.bizmekatalk.adapter;

import android.widget.BaseAdapter;

import com.example.bizmekatalk.items.Item;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomAdapter extends BaseAdapter {

    protected List<Item> items = new ArrayList();

    public abstract void setItems(List<Item> items);

    public abstract void addItems(List<Item> items);

    public abstract void clearItems();

}
