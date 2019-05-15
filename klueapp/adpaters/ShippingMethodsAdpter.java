package com.volive.klueapp.adpaters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.volive.klueapp.Models.ShippingMethodModel;
import com.volive.klueapp.R;
import com.volive.klueapp.Utils.PreferenceUtils;

import java.util.ArrayList;

class ShippingMethodsAdpter extends RecyclerView.Adapter<ShippingMethodsAdpter.GIftHolder> {

    ArrayList<ShippingMethodModel> shippingMethodModels;
    Context context;
    PreferenceUtils preferenceUtils;


    public ShippingMethodsAdpter(Context context, ArrayList<ShippingMethodModel> shippingMethodModels) {
        this.context = context;
        this.shippingMethodModels = shippingMethodModels;
    }

    @NonNull
    @Override
    public GIftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shi_method_item_view, parent, false);
        return new GIftHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GIftHolder holder, final int position) {
        holder.method_name.setText(shippingMethodModels.get(position).getShipping_name());
        holder.shipping_charge.setText(shippingMethodModels.get(position).getPrice_sar());

    }

    @Override
    public int getItemCount() {
        return shippingMethodModels.size();
    }

    public class GIftHolder extends RecyclerView.ViewHolder {
        CheckBox select_shipping_method;
        TextView method_name, shipping_charge_type, shipping_charge;

        public GIftHolder(View itemView) {
            super(itemView);

            select_shipping_method = itemView.findViewById(R.id.select_method);
            method_name = itemView.findViewById(R.id.method_name);
            shipping_charge_type = itemView.findViewById(R.id.shipping_charge_type);
            shipping_charge = itemView.findViewById(R.id.shipping_charge);

        }
    }

}
