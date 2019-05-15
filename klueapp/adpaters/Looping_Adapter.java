package com.volive.klueapp.adpaters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.asksira.loopingviewpager.LoopingViewPager;
import com.bumptech.glide.Glide;
import com.volive.klueapp.Models.Home_banner_model;
import com.volive.klueapp.R;

import java.util.ArrayList;

public class Looping_Adapter extends LoopingPagerAdapter {

    ArrayList<Home_banner_model> adds_models;


    public Looping_Adapter(Context context, ArrayList itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
    }

    @Override
    protected View inflateView(int viewType, int listPosition) {

        return LayoutInflater.from(context).inflate(R.layout.banner_slide_view, null);
    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {
        ImageView image = (ImageView) convertView.findViewById(R.id.banner_img);

        Glide.with(context)
                .load(adds_models.get(listPosition).getBanner_image())
                .into(image);

        ViewPager vp = (ViewPager) convertView;
        vp.addView(convertView, 0);

    }
}

