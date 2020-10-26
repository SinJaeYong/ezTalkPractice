package com.example.bizmekatalk.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.bizmekatalk.R;
import com.example.bizmekatalk.common.PreferenceManager;
import com.example.bizmekatalk.databinding.UserProfileActivityBinding;
import com.example.bizmekatalk.items.UserItem;

public class UserProfileActivity extends AppCompatActivity {
    private UserProfileActivityBinding binding;
    boolean bookMark = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserProfileActivityBinding.inflate(getLayoutInflater());

        UserItem item = getIntent().getParcelableExtra("parcel");

        binding.tvName.setText(item.getName());
        binding.tvDeptName.setText(item.getDeptName());
        binding.tvEmail.setText(item.getEmail());
        binding.tvMessage.setText(item.getMessage());
        binding.tvPosition.setText(item.getPosition());
        binding.tvMobile.setText(item.getMobile());
        binding.tvTel.setText(item.getTel());
        setProfileImg(binding.ivProfileImage,item.getProfileImage(),this);
        binding.llBack.setOnClickListener(v -> {
            finish();
        });
        binding.llBookMark.setOnClickListener(v -> {
            if(bookMark){
                binding.ivBookMark.setImageResource(R.drawable.n_btn_info_bookmark);
                bookMark = false;
            } else {
                binding.ivBookMark.setImageResource(R.drawable.n_btn_info_bookmark_on);
                bookMark = true;
            }

        });

        setContentView(binding.getRoot());
        getSupportActionBar().hide();

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
}
