package com.example.bizmekatalk.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.activity.BizmekaApp;
import com.example.bizmekatalk.items.DataRepository;
import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.items.Item;

import java.util.List;

public class DeptListAdapter extends CustomAdapter {


    @Override
    public void updateItems(List<Item> items) {
        this.items = items;
    }

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

        DeptHolder deptHolder = new DeptHolder();
        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.dept_item, viewGroup, false);
            deptHolder.tvDeptName = (TextView) itemView.findViewById(R.id.tvDeptName);
            deptHolder.ivIsLeaf = (ImageView) itemView.findViewById(R.id.ivIsLeaf);
            itemView.setTag(deptHolder);
        } else {
            deptHolder = (DeptHolder) itemView.getTag();
        }
        if (deptHolder.tvDeptName != null) {
            deptHolder.tvDeptName.setText(((DeptItem)items.get(position)).getDeptName());
        }

        //OnclickListener
        if (deptHolder.ivIsLeaf != null) {
            if ("Y".equals(((DeptItem)items.get(position)).getIsLeaf())){
                deptHolder.ivIsLeaf.setVisibility(View.VISIBLE);
                itemView.setOnClickListener(v -> {
                    String parentId = ((DeptItem) items.get(position)).getDeptId();
                    DataRepository.getInstance().getNavi().add(parentId);
                    Log.i("jay.navi","navi.gitList : "+DataRepository.getInstance().getNavi().getLast());
                    updateItems(DataRepository.getInstance().getDeptMap().get(parentId));
                    notifyDataSetChanged();
                });
            } else{
                ///부서원 정보 출력
                deptHolder.ivIsLeaf.setVisibility(View.INVISIBLE);
                itemView.setOnClickListener(v -> {

                });
            }
        }

        return itemView;
    }



    private class DeptHolder {
        public TextView tvDeptName;
        public ImageView ivIsLeaf;
    }

    private class UserInfoHolder {
        public ImageView ivProfileImage;
        public TextView tvName;
        public TextView tvPosition;
        public TextView tvCondition;
        public TextView tvJob;
    }

}
