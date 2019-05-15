package com.volive.klueapp.adpaters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.volive.klueapp.Activities.ProductDetails;
import com.volive.klueapp.Fragments.Whishlist;
import com.volive.klueapp.Models.NewArrivalModel;
import com.volive.klueapp.R;

import java.util.ArrayList;

public class NewArrivalAdapter extends RecyclerView.Adapter<NewArrivalAdapter.Holder> {

    Context context;
    String fragmentName;
    static ArrayList<NewArrivalModel> list;

    public NewArrivalAdapter(FragmentActivity activity, ArrayList<NewArrivalModel> new_list, String fragmentName) {
        this.context = activity;
        this.list = new_list;
        this.fragmentName = fragmentName;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_view, parent, false);
        return new Holder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {

        holder.pdt_price_off.setPaintFlags(holder.pdt_price_off.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.pdt_price_type.setPaintFlags(holder.pdt_price_type.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.pdt_name.setText(list.get(position).getPname());
        holder.price_type.getText().toString().trim();
        holder.pdt_price_type.getText().toString().trim();
        holder.pdt_brand.setText(list.get(position).getBrand_name());
        holder.pdt_price.setText(list.get(position).getPrice_sar());
        holder.pdt_price_off.setText(list.get(position).getRegular_price_sar());
        if (list.get(position).getIs_wish_list() == 1) {
                list.get(position).setChecked(true);
                holder.heart.setImageResource(R.mipmap.whishlist_hvr);
        } else {
            list.get(position).setChecked(false);
            holder.heart.setImageResource(R.mipmap.heart);
        }
        Glide.with(context)
                .load(list.get(position).getProd_image())
                .into(holder.image);

        holder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.get(position).isChecked()) {
                    list.get(position).setChecked(true);
                    list.get(position).setIs_wish_list(1);
                    Whishlist.addToWishList(context, position, fragmentName);
                    holder.heart.setImageResource(R.mipmap.whishlist_hvr);
                    notifyItemChanged(position);
                } else {
                    list.get(position).setChecked(false);
                    list.get(position).setIs_wish_list(0);
                    Whishlist.removeFromWhishList(context, position, fragmentName);
                    holder.heart.setImageResource(R.mipmap.heart);
                    notifyItemChanged(position);
                }
            }
        });

        holder.grid_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pdt_detail_intent = new Intent(context, ProductDetails.class);
                pdt_detail_intent.putExtra("Position", position + "");
                pdt_detail_intent.putExtra("Context", fragmentName);
                context.startActivity(pdt_detail_intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView image, heart;
        TextView pdt_name, pdt_brand, pdt_price, pdt_price_off, price_type, pdt_price_type;
        LinearLayout grid_item;

        public Holder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            price_type = itemView.findViewById(R.id.price_type);
            pdt_price_type = itemView.findViewById(R.id.pdt_price_type);
            heart = itemView.findViewById(R.id.heart);
            pdt_name = itemView.findViewById(R.id.pdt_name);

            pdt_brand = itemView.findViewById(R.id.pdt_brand);
            pdt_price = itemView.findViewById(R.id.pdt_price);
            pdt_price_off = itemView.findViewById(R.id.pdt_price_off);
            grid_item = itemView.findViewById(R.id.grid_item);
        }
    }

}