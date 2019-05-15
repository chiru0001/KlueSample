package com.volive.klueapp.adpaters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.volive.klueapp.Activities.MainActivity;
import com.volive.klueapp.Activities.SubcategoryActivity;
import com.volive.klueapp.Models.Category_Model;
import com.volive.klueapp.R;

import java.util.ArrayList;


public class CatListAdapter extends RecyclerView.Adapter<CatListAdapter.Holder> {

    Context context;
    ArrayList<Category_Model> cat_list;
    RecyclerView.LayoutManager layoutManager;

    public CatListAdapter(FragmentActivity activity, ArrayList<Category_Model> category_list) {
        this.cat_list = category_list;
        this.context = activity;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rview_cat_item_view, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        holder.cat_title.setText(cat_list.get(position).getCname());

        Glide.with(context)
                .load(cat_list.get(position).getCat_image())
                .into(holder.cat_image);
        // layoutManager is your recycler view's layout manager

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (context instanceof MainActivity) {
                    Intent sub_cat_intent = new Intent(context, SubcategoryActivity.class);
                    sub_cat_intent.putExtra("Position", position + "");
                    context.startActivity(sub_cat_intent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return cat_list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView cat_image;
        TextView cat_title;

        public Holder(final View itemView) {
            super(itemView);

            cat_image = itemView.findViewById(R.id.cat_image);
            cat_title = itemView.findViewById(R.id.cat_title);

        }

    }
}
