package com.example.bizmekatalk.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.bizmekatalk.R;
import com.example.bizmekatalk.items.ProfileItem;
import com.example.bizmekatalk.utils.PreferenceManager;

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

        final Context context = viewGroup.getContext();
        View view = itemView;
        final ViewHolder holder;


        if(view == null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.profile_item,viewGroup,false);
            holder = new ViewHolder();
            holder.itemProfileImage = (ImageView)view.findViewById(R.id.itemProfileImage);
            holder.itemName = (TextView)view.findViewById(R.id.itemName);
            holder.itemPosition = (TextView)view.findViewById(R.id.itemPosition);
            holder.itemJob = (TextView)view.findViewById(R.id.itemJob);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }

        String imgUrl = items.get(position).getProfileImageUrl();

        if(holder.itemProfileImage!=null){
            GradientDrawable drawable=(GradientDrawable)context.getDrawable(R.drawable.profile_background_rounding);
            holder.itemProfileImage.setBackground(drawable);
            holder.itemProfileImage.setClipToOutline(true);
        }

        final Handler handler = new Handler(Looper.myLooper());

        if(PreferenceManager.UPLOAD_URL.equals(imgUrl)){
            Glide.with(context).load(R.drawable.user_profile_icon1).apply(RequestOptions.circleCropTransform()).transform(new CenterCrop()).into(holder.itemProfileImage);
        }else {
            Glide.with(context).load(imgUrl).
                    listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            handler.post(() -> Glide.with(context).load(R.drawable.user_profile_icon1).apply(RequestOptions.circleCropTransform()).transform(new CenterCrop()).into(holder.itemProfileImage));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).
                    transform(new CenterCrop()).into(holder.itemProfileImage);
        }

        if(holder.itemName!=null) holder.itemName.setText(items.get(position).getName());
        if(holder.itemPosition!=null) holder.itemPosition.setText(items.get(position).getPosition());
        if(holder.itemJob!=null) holder.itemJob.setText(items.get(position).getJob());

        return view;
    }

    static class ViewHolder{
        ImageView itemProfileImage;
        TextView itemName;
        TextView itemPosition;
        TextView itemJob;
    }


}
