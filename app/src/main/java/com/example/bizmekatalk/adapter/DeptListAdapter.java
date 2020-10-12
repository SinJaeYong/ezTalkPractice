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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class DeptListAdapter extends CustomAdapter<JsonObject> {

    @Getter
    @Setter
    private Map<String,List<JSONObject>> map = new HashMap<String,List<JSONObject>>();

    public String preDeptId = "";

    @Override
    public void addItems(String result) {

        // 이 아래 전부 Fragment

    }

    @Override
    public void clearItems() {
        this.items.clear();
    }

    @Override
    public void resetItems(String result) {
        this.items.clear();
        addItems(result);
    }

    @Override
    public void updateItems(List<JsonObject> items) {

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

        final Context context = viewGroup.getContext();
        View view = itemView;
        final Holder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dept_item, viewGroup, false);
            holder = new Holder();
            holder.tvDeptName = (TextView) view.findViewById(R.id.tvDeptName);
            holder.ivIsLeaf = (ImageView) view.findViewById(R.id.ivIsLeaf);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        if (holder.tvDeptName != null) {
            holder.tvDeptName.setText(items.get(position).getDeptName());
        }
        if (holder.ivIsLeaf != null) {
            if ("Y".equals(items.get(position).getIsLeaf())){
                holder.ivIsLeaf.setVisibility(View.VISIBLE);
                view.setOnClickListener(v -> {
                    Log.i("jay.DeptListAdapter","deptId : "+ items.get(position).getDeptId());
                    updateAdapter(setRequestParams(items.get(position).getDeptId()),"reset");
                });
            }


        }

        return view;
    }

    private RequestParams setRequestParams(String parentDeptId) {
        return new RequestParamBuilder().
                setPath(new ApiPath("OrgDeptInfo","GetSubDeptInfo")).
                setBodyJson("compid", PreferenceManager.getString(PreferenceManager.getCompId())).
                setBodyJson("parentdeptid", AES256Cipher.stringToEncryptStringWithJava(parentDeptId)).
                setBodyJson("lan",1).
                build();
    }

    private class Holder {
        public TextView tvDeptName;
        public ImageView ivIsLeaf;
    }

}
