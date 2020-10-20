package com.example.bizmekatalk.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.bizmekatalk.R;
import com.example.bizmekatalk.activity.BizmekaApp;
import com.example.bizmekatalk.common.PreferenceManager;
import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.items.Item;
import com.example.bizmekatalk.items.UserItem;

import java.util.ArrayList;
import java.util.List;

public class OrganAdapter extends CustomAdapter {


    private Context context;

    private List<Item> deptUserList;
    private List<Item> deptList;

    public OrganAdapter(Context context){
        this.context = context;
        deptUserList = BizmekaApp.userMap.get(BizmekaApp.navi.getLast());
        deptList = BizmekaApp.deptMap.get(BizmekaApp.navi.getLast());
    }


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
    public void clearData(){
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
            holder.tvUserDeptName.setText(((UserItem)items.get(position)).getDeptName());
            holder.tvUserName.setText(((UserItem)items.get(position)).getName());
            String imgUrl = PreferenceManager.getUploadUrl() + ((UserItem)items.get(position)).getProfileImage();
            setProfileImg(holder,imgUrl);

        } else {
            holder.llUserItem.setVisibility(View.GONE);
            holder.llDeptItem.setVisibility(View.VISIBLE);
            if (holder.tvDeptName != null) {
                holder.tvDeptName.setText(((DeptItem)items.get(position)).getDeptName());
            }
            //Dept에 OnClickListener
            holder.llDeptItem.setOnClickListener(v -> {

                BizmekaApp.navi.add(((DeptItem) items.get(position)).getDeptId());
                BizmekaApp.COMPNAME = ((DeptItem) items.get(position)).getDeptName();
                TextView tvOrganTotalMember = ((Activity)context).findViewById(R.id.tvOrganTotalMember);

                if(BizmekaApp.userMap.get(BizmekaApp.navi.getLast())!=null){
                    tvOrganTotalMember.setText(String.valueOf(BizmekaApp.userMap.get(BizmekaApp.navi.getLast()).size()));
                } else {
                    tvOrganTotalMember.setText("0");
                }

                List<Item> items = BizmekaApp.userMap.get(BizmekaApp.navi.getLast());
                if(items != null){
                    for(Item item : items){
                        ((UserItem)item).setDeptName(BizmekaApp.COMPNAME);
                    }
                }
                clearData();
                addData(BizmekaApp.userMap.get(BizmekaApp.navi.getLast()));
                addData(BizmekaApp.deptMap.get(BizmekaApp.navi.getLast()));
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

    private void setProfileImg(Holder holder, String imgUrl){

        //Log.i("jay.OrganAdapter","imgUrl : "+imgUrl);
        if(holder.ivProfileImage!=null){
            GradientDrawable drawable=(GradientDrawable)context.getDrawable(R.drawable.profile_background_rounding);
            holder.ivProfileImage.setBackground(drawable);
            holder.ivProfileImage.setClipToOutline(true);
        }

        final Handler handler = new Handler(Looper.myLooper());

        if(PreferenceManager.getUploadUrl().equals(imgUrl)){
            Glide.with(context).load(R.drawable.user_profile_icon1).apply(RequestOptions.circleCropTransform()).transform(new CenterCrop()).into(holder.ivProfileImage);
        }else {
            Glide.with(context).load(imgUrl).
                    listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            handler.post(() -> Glide.with(context).load(R.drawable.user_profile_icon1).apply(RequestOptions.circleCropTransform()).transform(new CenterCrop()).into(holder.ivProfileImage));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).
                    transform(new CenterCrop()).into(holder.ivProfileImage);
        }
    }

}