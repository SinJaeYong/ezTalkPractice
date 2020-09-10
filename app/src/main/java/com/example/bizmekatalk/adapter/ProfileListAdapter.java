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

    private ImageView itemProfileImage;
    private TextView itemName;
    private TextView itemPosition;
    private TextView itemJob;
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
            holder.itemProfileImage = view.findViewById(R.id.itemProfileImage);
            holder.itemName = view.findViewById(R.id.itemName);
            holder.itemPosition = view.findViewById(R.id.itemPosition);
            holder.itemJob = view.findViewById(R.id.itemJob);
            itemProfileImage = itemView.findViewById(R.id.itemProfileImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemPosition = itemView.findViewById(R.id.itemPosition);
            itemJob = itemView.findViewById(R.id.itemJob);
        }

        String imgUrl = items.get(position).getProfileImageUrl();

        if(itemProfileImage!=null){
            GradientDrawable drawable=(GradientDrawable)context.getDrawable(R.drawable.profile_background_rounding);
            itemProfileImage.setBackground(drawable);
            itemProfileImage.setClipToOutline(true);
        }

        final Handler handler = new Handler(Looper.myLooper());
        if(PreferenceManager.UPLOAD_URL.equals(imgUrl)){
            Glide.with(context).load(R.drawable.user_profile_icon1).apply(RequestOptions.circleCropTransform()).transform(new CenterCrop()).into(itemProfileImage);
        }else {
            Glide.with(context).load(imgUrl).
                    listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            handler.post(() -> Glide.with(context).load(R.drawable.user_profile_icon1).apply(RequestOptions.circleCropTransform()).transform(new CenterCrop()).into(itemProfileImage));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).
                    transform(new CenterCrop()).into(itemProfileImage);
        }

        if(itemName!=null) itemName.setText(items.get(position).getName());
        if(itemPosition!=null) itemPosition.setText(items.get(position).getPosition());
        if(itemJob!=null) itemJob.setText(items.get(position).getJob());

        return itemView;
    }

    static class ViewHolder{
        ImageView itemProfileImage;
        TextView itemName;
        TextView itemPosition;
        TextView itemJob;
    }


}
