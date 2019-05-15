package com.volive.klueapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.volive.klueapp.Activities.ShoppingCart;
import com.volive.klueapp.Models.ShippingMethodModel;
import com.volive.klueapp.Utils.MyAddressHelper;
import com.volive.klueapp.adpaters.AddressAdapter;
import com.volive.klueapp.adpaters.ShoppingCartAdapter;

import java.util.ArrayList;

class ShippingMethodsAdpter extends RecyclerView.Adapter<ShippingMethodsAdpter.Holder> {
    Context context;
    ArrayList<ShippingMethodModel> modelArrayList;
    public  static int shippingMethodPosition = 1;


    public ShippingMethodsAdpter(ShippingMethodsActivity shippingMethodsActivity, ArrayList<ShippingMethodModel> methodModels) {
        this.context = shippingMethodsActivity;
        this.modelArrayList = methodModels;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shi_method_item_view, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        holder.method_name.setText(modelArrayList.get(position).getShipping_name());
        holder.shipping_charge.setText(modelArrayList.get(position).getPrice_sar());
        holder.select_shipping_method.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    shippingMethodPosition=position;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        CheckBox select_shipping_method;
        TextView method_name, shipping_charge_type, shipping_charge;
        public Holder(View itemView) {
            super(itemView);
            select_shipping_method = itemView.findViewById(R.id.select_method);
            method_name = itemView.findViewById(R.id.method_name);
            shipping_charge_type = itemView.findViewById(R.id.shipping_charge_type);
            shipping_charge = itemView.findViewById(R.id.shipping_charge);
        }
    }
}
