package com.example.bizmekatalk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    private LinkedList<NaviItem> navi;
    private List<Item> deptList;
    private List<Item> userList;

    public OrganAdapter(Context context){
        this.context = context;
        initData();
    }

    @Override
    public void setItems(List<Item> items) {
        if(items != null){
            this.items = new ArrayList<>(items);
        }
    }
    @Override
    public void addItems(List<Item> items) {
        if(items != null){
            this.items.addAll(items);
        }
    }
    @Override
    public void clearItems(){
        this.items.clear();
    }

    public void updateItems(List<Item>...items) {
        clearItems();
        for(List<Item> itemList : items){
            addItems( itemList );
        }
        notifyDataSetChanged();
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
            initHolder(itemView, holder);
        } else {
            holder = (Holder) itemView.getTag();
        }

        //UserItem Or DeptItem 분기점
        if("UserItem".equals(items.get(position).getType())){
            //null체크 필요
            setUserItemHolder(position, holder);
        } else {
            //null 체크 필요
            setDeptItemHolder(position, holder);
        }
        return itemView;
    }





    private void setDeptItemHolder(int position, Holder holder) {
        holder.llUserItem.setVisibility(View.GONE);
        holder.llDeptItem.setVisibility(View.VISIBLE);
        holder.tvDeptName.setText(((DeptItem)items.get(position)).getDeptName());
        //Dept에 OnClickListener
        holder.llDeptItem.setOnClickListener(v -> {

            String deptId = ((DeptItem) items.get(position)).getDeptId();
            String deptName = ((DeptItem) items.get(position)).getDeptName();


            navi.add( new NaviItem(deptId,deptName) );
            resetLists();
            int naviPosition = navi.size();
            setDeptNavigationView(naviPosition, deptName);
            setNumberOfUsers( userList );
            setUserDeptName( deptName );
            updateItems( userList, deptList );

        });
    }


    private void setUserItemHolder(int position, Holder holder) {
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
    }


    private void initHolder(View itemView, Holder holder) {
        holder.llUserItem = itemView.findViewById(R.id.llUserItem);
        holder.tvUserName = itemView.findViewById(R.id.tvUserName);
        holder.tvUserDeptName= itemView.findViewById(R.id.tvUserDeptName);
        holder.ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
        holder.llDeptItem = itemView.findViewById(R.id.llDeptItem);
        holder.tvDeptName = itemView.findViewById(R.id.tvDeptName);
        holder.ivIsLeaf = itemView.findViewById(R.id.ivIsLeaf);
        itemView.setTag(holder);
    }


    private void setDeptNavigationView(int position, String deptName) {
        LinearLayout llDeptNavi = ((Activity)context).findViewById(R.id.llDeptNavi);
        SubNaviLayout childLayout = new SubNaviLayout(context);
        childLayout.setOnClickListener(v1 -> {
            clearNavi( position, llDeptNavi );
            updateItems( userList, deptList );
            setNumberOfUsers( userList );
        });
        TextView tvSubDeptName = childLayout.findViewById(R.id.tvSubDeptName);
        tvSubDeptName.setText( deptName );
        llDeptNavi.addView( childLayout );
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


    private void setNumberOfUsers(List<Item> userList) {
        TextView tvTotalMember = ((Activity)context).findViewById(R.id.tvOrganTotalMember);
        if (userList != null) {
            tvTotalMember.setText( String.valueOf( userList.size() ));
        } else {
            tvTotalMember.setText("0");
        }
    }


    private void initData(){
        navi = BizmekaApp.navi;
        resetLists();
        //setUserDeptName( navi.getLast().getDeptName() );
    }


    private void resetLists(){
        deptList = BizmekaApp.deptMap.get( navi.getLast().getDeptId() );
        userList = BizmekaApp.userMap.get( navi.getLast().getDeptId() );
    }


    private void setUserDeptName(String userDeptName) {
        if(userList != null){
            userList.stream().forEach(item->((UserItem)item).setDeptName( userDeptName ));
        }
    }


    private void clearNavi(int startNum ,LinearLayout ll) {
        if(navi.size()>1){
            int endNum = navi.size();
            ll.removeViews(startNum,  endNum - startNum);
            for(int i = 0 ; i <  endNum - startNum ; i++){
                navi.removeLast();
            }
            resetLists();
        }
    }


    private class Holder {
        //userItem 정보
        public LinearLayout llUserItem;
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvUserDeptName;

        //deptItem 정보
        public LinearLayout llDeptItem;
        public TextView tvDeptName;
        public ImageView ivIsLeaf;

    }
}
