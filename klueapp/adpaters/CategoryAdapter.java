package com.volive.klueapp.adpaters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.volive.klueapp.Activities.SubcategoryActivity;
import com.volive.klueapp.Models.Category_Model;
import com.volive.klueapp.R;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    Context context;
    ArrayList<Category_Model> category_list;

//    public CategoryAdapter(Activity activity, ArrayList category_list) {
//        this.context=activity;
//        this.category_list=category_list;
//    }

    public CategoryAdapter(FragmentActivity activity, ArrayList<Category_Model> category_list) {
        this.context = activity;
        this.category_list = category_list;
    }


    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item_view, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, final int position) {
        Glide.with(context).load(category_list.get(position).getCat_image()).into(holder.category_image);
        holder.category_type.setText(category_list.get(position).getCname());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent subcategory_intent = new Intent(context, SubcategoryActivity.class);
                subcategory_intent.putExtra("Position", position + "");
                context.startActivity(subcategory_intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return category_list.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        TextView category_type;
        ImageView category_image;
        CardView card_view;

        public CategoryHolder(View itemView) {
            super(itemView);
            category_type = itemView.findViewById(R.id.category_type);
            category_image = itemView.findViewById(R.id.category_image);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }
}
