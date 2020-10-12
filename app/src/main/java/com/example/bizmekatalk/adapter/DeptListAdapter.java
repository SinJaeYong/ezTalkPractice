package com.example.bizmekatalk.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bizmekatalk.R;
import com.example.bizmekatalk.api.common.ApiPath;
import com.example.bizmekatalk.api.common.RequestParamBuilder;
import com.example.bizmekatalk.api.common.RequestParams;
import com.example.bizmekatalk.crypto.AES256Cipher;
import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

public class DeptListAdapter extends CustomAdapter<DeptItem> {

    @Getter
    @Setter
    private Map<String,List<JSONObject>> map = new HashMap<String,List<JSONObject>>();

    public LinkedList<String> navi = new LinkedList<String>();

    @Override
    public void addItems(String result) {

        // 이 아래 전부 Fragment

    }

    @Override
    public void updateItems(List<JSONObject> items) {
        List<DeptItem> deptItems = convertToItemList(items);
        this.items = deptItems;
    }

    private List<DeptItem> convertToItemList(List<JSONObject> items) {
        return items.stream().map(jsonObject -> {
                DeptItem item = new DeptItem();
                try {
                    item.setDeptId(jsonObject.get("deptid").toString());
                    item.setDeptName(jsonObject.get("deptname").toString());
                    item.setIsLeaf(jsonObject.get("isleaf").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return item;
            }).collect(Collectors.toList());
    }

    public void initNavi(String init){
        navi.clear();
        navi.push(init);
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
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public View getView(int position, View itemView, ViewGroup viewGroup) {


        if(DeptItem.class.isInstance(items.get(position))){
            DeptHolder deptHolder = new DeptHolder();
            Log.i("jay.DeptListAdapter","들어왔다");
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
                deptHolder.tvDeptName.setText(items.get(position).getDeptName());
            }

            if (deptHolder.ivIsLeaf != null) {
                if ("Y".equals(items.get(position).getIsLeaf())){
                    deptHolder.ivIsLeaf.setVisibility(View.VISIBLE);
                    itemView.setOnClickListener(v -> {
                        navi.add(items.get(position).getDeptId());
                        Log.i("jay.navi","navi.gitList : "+navi.getLast());
                        updateItems(map.get(items.get(position).getDeptId()));
                        notifyDataSetChanged();
                    });
                } else{
                    ///부서원 정보 출력
                    deptHolder.ivIsLeaf.setVisibility(View.INVISIBLE);
                    itemView.setOnClickListener(v -> {

                    });
                }
            }
        } else {
            UserInfoHolder userInfoHolder = new UserInfoHolder();
            if (itemView == null) {
                LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                itemView = inflater.inflate(R.layout.profile_item, viewGroup, false);
                userInfoHolder.ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
                userInfoHolder.tvName = (TextView) itemView.findViewById(R.id.tvName);
                userInfoHolder.tvJob = (TextView) itemView.findViewById(R.id.tvJob);
                userInfoHolder.tvCondition = (TextView) itemView.findViewById(R.id.tvCondition);
                userInfoHolder.tvPosition = (TextView) itemView.findViewById(R.id.tvPosition);
                itemView.setTag(userInfoHolder);
            } else {
                userInfoHolder = (UserInfoHolder) itemView.getTag();
            }

            if(userInfoHolder.tvName!=null){

            }
        }



        return itemView;
    }

   private RequestParams setRequestParams(String parentDeptId) {
        return new RequestParamBuilder().
                setPath(new ApiPath("OrgDeptInfo","GetSubDeptInfo")).
                setBodyJson("compid", PreferenceManager.getString(PreferenceManager.getCompId())).
                setBodyJson("parentdeptid", AES256Cipher.stringToEncryptStringWithJava(parentDeptId)).
                setBodyJson("lan",1).
                build();
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
