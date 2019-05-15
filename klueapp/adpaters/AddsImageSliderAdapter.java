package com.volive.klueapp.adpaters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.volive.klueapp.Models.Ads_Model;
import com.volive.klueapp.Models.Home_banner_model;
import com.volive.klueapp.R;

import java.util.ArrayList;

public class AddsImageSliderAdapter extends PagerAdapter {

    private ArrayList<Ads_Model> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    boolean isInfinite;

    public AddsImageSliderAdapter(Context context, ArrayList<Ads_Model> IMAGES, boolean isInfinite) {
        this.context = context;
        this.IMAGES = IMAGES;
        this.isInfinite = isInfinite;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.ads_slide_view, view, false);

        ImageView image = (ImageView) imageLayout.findViewById(R.id.ads_img);

        Glide.with(context)
                .load(IMAGES.get(position).getImage())
                .into(image);
        ViewPager viewPager = (ViewPager) view;
        viewPager.addView(imageLayout, 0);
        return imageLayout;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}

/*
package com.volive.klueapp.adpaters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.bumptech.glide.Glide;
import com.volive.klueapp.Models.Ads_Model;
import com.volive.klueapp.R;

import java.util.ArrayList;

public class AddsImageSliderAdapter extends LoopingPagerAdapter<Ads_Model>{
//    Context context;
    LayoutInflater layoutInflater;
//
//    ArrayList<Ads_Model> ads_array;
    private static final int VIEW_TYPE_NORMAL = 100;

    public AddsImageSliderAdapter(Context context, ArrayList<Ads_Model> itemList, boolean infinite) {
        super(context, itemList, infinite);
    }


    @Override
    protected int getItemViewType(int listPosition) {
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    protected View inflateView(int viewType, int listPosition) {
        if (viewType == VIEW_TYPE_NORMAL) {
            View view = LayoutInflater.from(context).inflate(R.layout.ads_slide_view, null);
            return view;
        } else
            return null;
    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {
        try {
            ImageView image = (ImageView) convertView.findViewById(R.id.ads_img);

            Glide.with(context)
                    .load(itemList.get(listPosition).getImage())
                    .into(image);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    */
/*@Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.banner_slide_view, null);

        ImageView image = (ImageView) view.findViewById(R.id.banner_img);

        Glide.with(context)
                .load(ads_array.get(position).getImage())
                .into(image);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

    @Override
    public int getRealCount() {
        return ads_array.size();

    }

    @Override
    public int getCount() {
        return ads_array.size();
    }*//*

}*/
