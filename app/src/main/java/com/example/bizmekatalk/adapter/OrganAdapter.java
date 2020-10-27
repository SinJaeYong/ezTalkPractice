package com.example.bizmekatalk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
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
import com.example.bizmekatalk.activity.LoginActivity;
import com.example.bizmekatalk.activity.PinLoginActivity;
import com.example.bizmekatalk.activity.UserProfileActivity;
import com.example.bizmekatalk.common.PreferenceManager;
import com.example.bizmekatalk.fragment.sublayout.SubNaviLayout;
import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.items.Item;
import com.example.bizmekatalk.items.NaviItem;
import com.example.bizmekatalk.items.UserItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OrganAdapter extends CustomAdapter {


    private Context context;


    public OrganAdapter(Context context){
        this.context = context;
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
        //Log.i("jay.OrganAdapter","items : "+(items.get(position)).toString());
        if("UserItem".equals(items.get(position).getType())){
            //null체크
            holder.llDeptItem.setVisibility(View.GONE);
            holder.llUserItem.setVisibility(View.VISIBLE);
            holder.tvUserDeptName.setText(((UserItem)items.get(position)).getDeptName());
            holder.tvUserName.setText(((UserItem)items.get(position)).getName());
            String imgUrl = ((UserItem)items.get(position)).getProfileImage();
            setProfileImg(holder.ivProfileImage,imgUrl,context);

            holder.llUserItem.setOnClickListener(v -> {
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra("parcel",((UserItem)items.get(position)));
                context.startActivity(intent);
            });

        } else {
            //null 체크
            holder.llUserItem.setVisibility(View.GONE);
            holder.llDeptItem.setVisibility(View.VISIBLE);
            holder.tvDeptName.setText(((DeptItem)items.get(position)).getDeptName());
            //Dept에 OnClickListener
            holder.llDeptItem.setOnClickListener(v -> {

                String deptId = ((DeptItem) items.get(position)).getDeptId();
                String deptName = ((DeptItem) items.get(position)).getDeptName();
                LinkedList<NaviItem> navi = BizmekaApp.navi;
                Map<String, List<Item>> deptMap = BizmekaApp.deptMap;
                Map<String, List<Item>> userMap = BizmekaApp.userMap;

                navi.add( new NaviItem(deptId,deptName) );

                //네비게이션
                LinearLayout llDeptNavi = ((Activity)context).findViewById(R.id.llDeptNavi);


                SubNaviLayout childLayout = new SubNaviLayout(context);

                childLayout.setTag( navi.size() );
                int naviSize = navi.size();
                childLayout.setOnClickListener(v1 -> {
                    int naviMaxSize = navi.size();
                    llDeptNavi.removeViews(naviSize, naviMaxSize - naviSize);
                    for(int i = 0 ; i < naviMaxSize - naviSize ; i++){
                        navi.removeLast();
                    }
                    updateTotalMember( userMap.get( navi.getLast().getDeptId() ) );
                    clearData();
                    addData( userMap.get( navi.getLast().getDeptId() ) );
                    addData( deptMap.get( navi.getLast().getDeptId() ) );



                    notifyDataSetChanged();

                });

                TextView tvSubDeptName = childLayout.findViewById(R.id.tvSubDeptName);
                tvSubDeptName.setText( deptName );
                llDeptNavi.addView( childLayout );


                TextView tvOrganTotalMember = ((Activity)context).findViewById(R.id.tvOrganTotalMember);

                if( userMap.get( navi.getLast().getDeptId() ) != null ){
                    tvOrganTotalMember.setText( String.valueOf( userMap.get( navi.getLast().getDeptId() ).size() ) );
                } else {
                    tvOrganTotalMember.setText("0");
                }

                List<Item> items = userMap.get( navi.getLast().getDeptId() );
                if(items != null){
                    for(Item item : items){
                        ((UserItem)item).setDeptName( deptName );
                    }
                }
                clearData();
                addData( userMap.get( navi.getLast().getDeptId() ) );
                addData( deptMap.get( navi.getLast().getDeptId() ) );
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

    private void setProfileImg(ImageView iv, String imgUrl, Context context){

        if(iv!=null){
            GradientDrawable drawable=(GradientDrawable)context.getDrawable(R.drawable.profile_background_rounding);
            iv.setBackground(drawable);
            iv.setClipToOutline(true);
        }

        final Handler handler = new Handler(Looper.myLooper());

        if(PreferenceManager.getUploadUrl().equals(imgUrl)){
            Glide.with(context).load(R.drawable.no_image).apply(RequestOptions.circleCropTransform()).transform(new CenterCrop()).into(iv);
        }else {
            Glide.with(context).load(imgUrl).
                    listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            handler.post(() -> Glide.with(context).load(R.drawable.no_image).apply(RequestOptions.circleCropTransform()).transform(new CenterCrop()).into(iv));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).
                    transform(new CenterCrop()).into(iv);
        }
    }

    private void updateTotalMember(List<Item> members) {
        TextView tvTotalMember = ((Activity)context).findViewById(R.id.tvOrganTotalMember);
        if (members != null) {
            tvTotalMember.setText( String.valueOf( members.size() ));
        } else {
            tvTotalMember.setText("0");
        }
    }

}
