package com.volive.klueapp.adpaters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.volive.klueapp.Activities.Brand;
import com.volive.klueapp.Models.BrandModel;
import com.volive.klueapp.R;

import java.util.ArrayList;

public class BrandItemAdapter extends RecyclerView.Adapter<BrandItemAdapter.BrandHolder> {
    Context context;
    ArrayList<BrandModel> brand_list;
    public static int brandPosition;

    public BrandItemAdapter(Brand brand, ArrayList<BrandModel> brand_list_array) {
        this.context=brand;
        this.brand_list=brand_list_array;
    }


    @NonNull
    @Override
    public BrandHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.brand_item_view,parent,false);

        return new BrandHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandHolder holder, final int position) {
        holder.txt_name.setText(brand_list.get(position).getName());
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    brandPosition=position;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return brand_list.size();
    }

    public class BrandHolder extends RecyclerView.ViewHolder{
        LinearLayout layout;
        TextView txt_name;
        CheckBox checkbox;
        public BrandHolder(View itemView) {
            super(itemView);
            txt_name=itemView.findViewById(R.id.brand_name);
            layout=itemView.findViewById(R.id.layout_brand);
            checkbox=itemView.findViewById(R.id.brand_select_checkbox);
        }
    }
}
