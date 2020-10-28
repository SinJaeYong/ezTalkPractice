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
import com.example.bizmekatalk.activity.Bizmeka;
import com.example.bizmekatalk.activity.UserProfileActivity;
import com.example.bizmekatalk.common.PreferenceManager;
import com.example.bizmekatalk.fragment.sublayout.SubNaviLayout;
import com.example.bizmekatalk.items.DeptItem;
import com.example.bizmekatalk.items.Item;
import com.example.bizmekatalk.items.NaviItem;
import com.example.bizmekatalk.items.UserItem;

import java.util.ArrayList;
import java.util.List;

public class OrganAdapter extends CustomAdapter {


    private Context context;

    private List<Item> deptLeafList;
    private List<Item> userLeafList;

    public OrganAdapter(Context context){
        this.context = context;
        resetItemLeafList();
    }

    @Override
    public void setItems(List items) {
        if(items != null){
            this.items = new ArrayList<>(items);
        }
    }
    @Override
    public void addItems( List items ) {
        if(items != null){
            this.items.addAll(items);
        }
    }
    @Override
    public void clearItems(){ this.items.clear(); }

    public void updateItems(List...itemsArr) {
        clearItems();
        if(itemsArr != null){
            for(List items : itemsArr){
                addItems( items );
            }
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
            itemView.setTag( initHolder(itemView, holder) );
        } else {
            holder = (Holder) itemView.getTag();
        }

        //UserItem Or DeptItem 분기점
        if("UserItem".equals(items.get(position).getType())){
            setUserItemHolder(position, holder);
        } else {
            setDeptItemHolder(position, holder);
        }

        return itemView;
    }





    private void setDeptItemHolder(int position, Holder holder) {
        if( holder.llUserItem != null ) holder.llUserItem.setVisibility(View.GONE);
        if( holder.tvDeptName != null ) holder.tvDeptName.setText(((DeptItem)items.get(position)).getDeptName());
        if( holder.llDeptItem != null ) {
            holder.llDeptItem.setVisibility(View.VISIBLE);
            holder.llDeptItem.setOnClickListener(v -> {
                String deptId = ((DeptItem) items.get(position)).getDeptId();
                String deptName = ((DeptItem) items.get(position)).getDeptName();

                Bizmeka.navi.add( new NaviItem(deptId,deptName) );
                resetItemLeafList();
                int naviPosition = Bizmeka.navi.size();
                setDeptNavigationView(naviPosition, deptName);
                setNumberOfUsers(userLeafList);
                setUserDeptName( deptName );
                updateItems(userLeafList, deptLeafList);

            });

        }



    }


    private void setUserItemHolder(int position, Holder holder) {
        if ( holder.llDeptItem != null) holder.llDeptItem.setVisibility(View.GONE);
        if ( holder.llUserItem != null) holder.llUserItem.setVisibility(View.VISIBLE);
        if ( holder.tvUserDeptName != null) holder.tvUserDeptName.setText(((UserItem)items.get(position)).getDeptName());
        if ( holder.tvUserName != null) holder.tvUserName.setText(((UserItem)items.get(position)).getName());
        if ( holder.ivProfileImage != null){
            String imgUrl = ((UserItem)items.get(position)).getProfileImage();
            setProfileImg(holder.ivProfileImage,imgUrl,context);
        }
        if ( holder.llUserItem != null) {
            holder.llUserItem.setOnClickListener(v -> {
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra("parcel",((UserItem)items.get(position)));
                context.startActivity(intent);
            });
        }
    }


    private Holder initHolder(View itemView, Holder holder) {
        holder.llUserItem = itemView.findViewById(R.id.llUserItem);
        holder.tvUserName = itemView.findViewById(R.id.tvUserName);
        holder.tvUserDeptName= itemView.findViewById(R.id.tvUserDeptName);
        holder.ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
        holder.llDeptItem = itemView.findViewById(R.id.llDeptItem);
        holder.tvDeptName = itemView.findViewById(R.id.tvDeptName);
        holder.ivIsLeaf = itemView.findViewById(R.id.ivIsLeaf);
        return holder;
    }


    private void setDeptNavigationView(int position, String deptName) {
        LinearLayout llDeptNavi = ((Activity)context).findViewById(R.id.llDeptNavi);
        SubNaviLayout childLayout = new SubNaviLayout(context);
        childLayout.setOnClickListener(v1 -> {
            clearNavi( position, llDeptNavi );
            updateItems(userLeafList, deptLeafList);
            setNumberOfUsers(userLeafList);
        });
        TextView tvSubDeptName = childLayout.findViewById(R.id.tvSubDeptName);
        if( tvSubDeptName != null ) tvSubDeptName.setText( deptName );
        if( llDeptNavi != null) llDeptNavi.addView( childLayout );
    }


    private void setProfileImg(ImageView iv, String imgUrl, Context context){

        //이미지 아웃라인 적용
        if(iv!=null){
            GradientDrawable drawable=(GradientDrawable)context.getDrawable(R.drawable.profile_background_rounding);
            iv.setBackground(drawable);
            iv.setClipToOutline(true);
        }


        //이미지 표시
        if( PreferenceManager.getUploadUrl().equals(imgUrl) || imgUrl == null || "".equals(imgUrl) ){
            Glide.with(context).load(R.drawable.no_image).apply(RequestOptions.circleCropTransform()).transform(new CenterCrop()).into(iv);
        }else {
            Glide.with(context).load(imgUrl).
                    listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            new Handler(Looper.myLooper()).post(() -> Glide.with(context).load(R.drawable.no_image).apply(RequestOptions.circleCropTransform()).transform(new CenterCrop()).into(iv));
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


    private void setNumberOfUsers(List userList) {
        TextView tvTotalMember = ((Activity)context).findViewById(R.id.tvOrganTotalMember);
        if (userList != null) {
            tvTotalMember.setText( String.valueOf( userList.size() ));
        } else {
            tvTotalMember.setText("0");
        }
    }


    private void resetItemLeafList(){
        deptLeafList = Bizmeka.deptMap.get( Bizmeka.navi.getLast().getDeptId() );
        userLeafList = Bizmeka.userMap.get( Bizmeka.navi.getLast().getDeptId() );
    }


    private void setUserDeptName(String userDeptName) {
        if(Bizmeka.userMap.get( Bizmeka.navi.getLast().getDeptId() ) != null){
            Bizmeka.userMap.get( Bizmeka.navi.getLast().getDeptId() ).stream().forEach(item->((UserItem)item).setDeptName( userDeptName ));
        }
    }


    private void clearNavi(int startNum ,LinearLayout ll) {
        if(Bizmeka.navi.size()>1){
            int endNum = Bizmeka.navi.size();
            ll.removeViews(startNum,  endNum - startNum);
            for(int i = 0 ; i <  endNum - startNum ; i++){
                Bizmeka.navi.removeLast();
            }
        }
        resetItemLeafList();
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
