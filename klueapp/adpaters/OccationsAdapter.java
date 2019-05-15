package com.volive.klueapp.adpaters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.volive.klueapp.Activities.Anniversary;
import com.volive.klueapp.Activities.SubcategoryActivity;
import com.volive.klueapp.Models.OccationsModel;
import com.volive.klueapp.R;

import java.util.ArrayList;

public class OccationsAdapter extends RecyclerView.Adapter<OccationsAdapter.OccationsHolder> {

    Context context;
   ArrayList<OccationsModel> occations_list;

    public OccationsAdapter(FragmentActivity activity, ArrayList<OccationsModel> occationsModels_list) {
        this.context=activity;
        this.occations_list=occationsModels_list;
    }


    @NonNull
    @Override
    public OccationsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.category_item_view,parent,false);

        return new OccationsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OccationsHolder holder, final int position) {

        Glide.with(context)
                .load(occations_list.get(position).getSub_cat_image())
                .into(holder.category_image);
        holder.category_type.setText(occations_list.get(position).getSub_cat_name());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sub_cat_pdt_intent = new Intent(context, Anniversary.class);
                sub_cat_pdt_intent.putExtra("Position", position+"");
                sub_cat_pdt_intent.putExtra("Context","OccationsAdapter");
                context.startActivity(sub_cat_pdt_intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return occations_list.size();
    }

    public class OccationsHolder extends RecyclerView.ViewHolder{
        TextView category_type;
        ImageView category_image;
        CardView cardView;
        public OccationsHolder(View itemView) {
            super(itemView);
            category_type=itemView.findViewById(R.id.category_type);
            category_image=itemView.findViewById(R.id.category_image);
            cardView=itemView.findViewById(R.id.card_view);
        }
    }
}
