package com.volive.klueapp.adpaters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.volive.klueapp.Activities.Anniversary;
import com.volive.klueapp.Activities.NewArrivals;
import com.volive.klueapp.Activities.ShoppingCart;
import com.volive.klueapp.Fragments.Home;
import com.volive.klueapp.Fragments.Whishlist;
import com.volive.klueapp.Models.CartItemModel;
import com.volive.klueapp.R;
import com.volive.klueapp.Utils.API_class;
import com.volive.klueapp.Utils.Constants;
import com.volive.klueapp.Utils.PreferenceUtils;
import com.volive.klueapp.Utils.Retrofit_funtion_class;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WhisList_Adapter extends RecyclerView.Adapter<WhisList_Adapter.WishList_Holder> {
    Context context;
    ArrayList<CartItemModel> list;
    boolean isChecked;
    String prod_id;
    public WhisList_Adapter(Activity activity, ArrayList list) {
        this.context = activity;
        this.list = list;
    }

    @Override
    public WishList_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.whislist_item_view, parent, false);
        return new WishList_Holder(view);
    }

    @Override
    public void onBindViewHolder(WishList_Holder holder, final int position) {

        prod_id = list.get(position).getProd_id();
        holder.title.setText(list.get(position).getPname());
        holder.brand.setText(list.get(position).getBrand_name());
        holder.pdt_price.setText(list.get(position).getPrice_sar());
        holder.pdt_price_off.setText(list.get(position).getRegular_price_sar());
        Glide.with(context).load(list.get(position).getProd_image()).into(holder.image);

        holder.pdt_price_type.setPaintFlags(holder.pdt_price_type.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.pdt_price_off.setPaintFlags(holder.pdt_price_off.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 Whishlist.removeFromWhishList(context, position, "WishList");
                delFromWhishList(context,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class WishList_Holder extends RecyclerView.ViewHolder {
        ImageView image, cancel_img;
        TextView title, brand, price_type, pdt_price, pdt_price_type, pdt_price_off;

        public WishList_Holder(View itemView) {
            super(itemView);
            cancel_img = itemView.findViewById(R.id.cancel_img);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            brand = itemView.findViewById(R.id.brand);
            price_type = itemView.findViewById(R.id.price_type);
            pdt_price = itemView.findViewById(R.id.pdt_price);
            pdt_price_type = itemView.findViewById(R.id.pdt_price_type);
            pdt_price_off = itemView.findViewById(R.id.pdt_price_off);
        }
    }
    private void delFromWhishList(final Context context, final int position) {
        PreferenceUtils preferenceUtils = new PreferenceUtils(context);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.DEL_FAVOURITE_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language,""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), prod_id);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(context);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDoalog.dismiss();
                System.out.println("----------------------------------------------------");
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));
                System.out.println("----------------------------------------------------");
                if (response.isSuccessful()) {
                    String searchResponse = response.body().toString();
                    try {
                        JSONObject responseObject = new JSONObject(searchResponse);
                        int status = responseObject.getInt("status");
                        String message = responseObject.getString("message");
                        if (status==1) {
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,list.size());
                            notifyDataSetChanged();
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());

                    }
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }
}
