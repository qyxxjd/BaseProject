package com.classic.simple.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.classic.adapter.interfaces.ImageLoad;

public class GlideImageLoad implements ImageLoad {

    @Override public void load(
            @NonNull Context context, @NonNull ImageView imageView, @NonNull String imageUrl) {
        Glide.with(context).load(imageUrl).centerCrop().crossFade().into(imageView);
    }
}
