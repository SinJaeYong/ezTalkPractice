package com.example.bizmekatalk.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.bizmekatalk.R;
import com.example.bizmekatalk.items.ProfileItem;

import java.util.List;
import java.util.Vector;

public class ProfileListAdapter extends BaseAdapter {

    private Context context;


    private List<ProfileItem> items = new Vector<ProfileItem>();


    public ProfileListAdapter(Context context) {
        this.context = context;
    }

    public void updateItems(ProfileItem item){
        this.items.add(item);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View itemView, ViewGroup viewGroup) {
        if(itemView == null){

            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView=inflater.inflate(R.layout.profile_item,null);
        }
        final ImageView itemProfileImage = itemView.findViewById(R.id.itemProfileImage);
        TextView itemName = itemView.findViewById(R.id.itemName);
        TextView itemPosition = itemView.findViewById(R.id.itemPosition);
        TextView itemJob = itemView.findViewById(R.id.itemJob);
        String imgUrl = items.get(position).getProfileImageUrl();


        final Handler handler = new Handler(Looper.myLooper());
        Glide.with(context).load(imgUrl).
                listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        handler.post(() -> Glide.with(context).load(R.drawable.user_profile_icon).transform(new CenterCrop()).into(itemProfileImage));
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).
                transform(new CenterCrop()).into(itemProfileImage);



        itemName.setText(items.get(position).getName());
        itemPosition.setText(items.get(position).getPosition());
        itemJob.setText(items.get(position).getJob());

        return itemView;
    }


}
