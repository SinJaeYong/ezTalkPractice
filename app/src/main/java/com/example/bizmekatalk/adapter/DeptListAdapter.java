package com.example.bizmekatalk.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.holder.ProfileViewHolder;

import java.util.ArrayList;
import java.util.List;

public class DeptListAdapter extends BaseAdapter {

    private Context context;

    private List<String> items = new ArrayList<String>();

    public DeptListAdapter(Context context) { this.context = context; }

    public void updateItems(String item) { this.items.add(item); }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup viewGroup) {

        final Context context = viewGroup.getContext();
        View view = itemView;
        final Holder holder;

        if( view == null ){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.dept_item,viewGroup,false);
            holder = new Holder();
            holder.itemDeptName = (TextView) view.findViewById(R.id.itemDeptName);
            view.setTag(holder);
        } else {
            holder = (Holder)view.getTag();
        }

        if( holder.itemDeptName != null ) {
            holder.itemDeptName.setText(items.get(position));
        }

        return view;
    }

    private class Holder {
        public TextView itemDeptName;
    }

}
