package com.example.bizmekatalk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.activity.BizmekaApp;
import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.items.Item;

import java.util.ArrayList;
import java.util.List;

public class OrganAdapter extends CustomAdapter {

    @Override
    public void setData(List<Item> items) {
        if(items != null){
            this.items = new ArrayList<>(items);
        }
    }

    @Override
    public void addData(List<Item> items) {
        if(items != null){
            this.items.addAll(items);
        }
    }
    @Override
    public void clear(){
        this.items.clear();
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


        Holder holder = new Holder();
        if (itemView == null) {

            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.organ_item, viewGroup, false);

            holder.llUserItem = itemView.findViewById(R.id.llUserItem);
            holder.tvUserName = itemView.findViewById(R.id.tvUserName);
            holder.tvUserDeptName= itemView.findViewById(R.id.tvUserDeptName);
            holder.ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            holder.llDeptItem = itemView.findViewById(R.id.llDeptItem);
            holder.tvDeptName = itemView.findViewById(R.id.tvDeptName);
            holder.ivIsLeaf = itemView.findViewById(R.id.ivIsLeaf);
            itemView.setTag(holder);
        } else {
            holder = (Holder) itemView.getTag();
        }

        /// 여기서부터 분기처리
        //Log.i("jay.DeptListAdapter","typename : "+items.get(position).getType());
        //Log.i("jay.OrganAdapter","items : "+(items.get(position)).toString());
        if("UserItem".equals(items.get(position).getType())){
            holder.llDeptItem.setVisibility(View.GONE);
            holder.llUserItem.setVisibility(View.VISIBLE);

        } else {
            holder.llUserItem.setVisibility(View.GONE);
            holder.llDeptItem.setVisibility(View.VISIBLE);
            if (holder.tvDeptName != null) {
                holder.tvDeptName.setText(((DeptItem)items.get(position)).getDeptName());
            }
            //Dept에 OnClickListener
            holder.llDeptItem.setOnClickListener(v -> {
                String parentId = ((DeptItem) items.get(position)).getDeptId();
                if( !BizmekaApp.navi.getLast().equals(parentId) ){
                    BizmekaApp.navi.add(parentId);
                }
                clear();
                addData(BizmekaApp.userMap.get(parentId));
                addData(BizmekaApp.deptMap.get(parentId));
                notifyDataSetChanged();
            });
        }

        return itemView;
    }



    private class Holder {
        //userItem 정
        public LinearLayout llUserItem;
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvUserDeptName;
        public TextView tvPosition;
        public TextView tvCondition;
        public TextView tvJob;

        //deptItem 정
        public LinearLayout llDeptItem;
        public TextView tvDeptName;
        public ImageView ivIsLeaf;

    }

    private class UserInfoHolder {

    }

}
