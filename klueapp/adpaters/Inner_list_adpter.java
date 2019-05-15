package com.volive.klueapp.adpaters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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
import com.volive.klueapp.Activities.GiftsCardsActivity;
import com.volive.klueapp.Models.GiftItemModel;
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

class Inner_list_adpter extends RecyclerView.Adapter<Inner_list_adpter.GIftHolder> {

    public static ArrayList<GiftItemModel> giftListArray;
    int count = 0;
    Context context;
    PreferenceUtils preferenceUtils;
    boolean isPlus;
    Double amount;
    int selectPosition, verticalPosition;


    public Inner_list_adpter(Context context, ArrayList<GiftItemModel> giftItemModels, int position) {
        this.context = context;
        this.giftListArray = giftItemModels;
        this.verticalPosition=position;
        setHasStableIds(false);
    }

    @NonNull
    @Override
    public GIftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gift_item_view, parent, false);
        return new GIftHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GIftHolder holder, final int position) {
        holder.gift_brand_name.setText(giftListArray.get(position).getBrand_name());
        holder.currency_value.setText(giftListArray.get(position).getRegular_price_sar());
        holder.gift_brand_name.setText(giftListArray.get(position).getBrand_name());
        holder.currency_value1.setText(giftListArray.get(position).getPrice_sar());
        Glide.with(context).load(giftListArray.get(position).getProd_image()).into(holder.gift_image);

        holder.currency_type.setPaintFlags(holder.currency_type.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.currency_value.setPaintFlags(holder.currency_value.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                holder.count_item.setText(count + "");
                add_to_gift_service(holder);
                isPlus = true;
                amount = Double.parseDouble(GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(position).getPrice_sar());
                GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(position).setQuantity(String.valueOf(count));
                selectPosition = position;
            }
        });
        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count >0) {
                    count--;
                    holder.count_item.setText(count + "");
                    del_gift_service(holder);
                    isPlus = false;
                    amount = Double.parseDouble(GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(position).getPrice_sar());
                    GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(position).setQuantity(String.valueOf(count));
                    selectPosition = position;
                }
            }
        });
        holder.count_item.setText(count + "");
    }

    @Override
    public int getItemCount() {
        return giftListArray.size();
    }

    public class GIftHolder extends RecyclerView.ViewHolder {
        CardView gift_item_view_layout;
        ImageView gift_image;
        TextView decrement, count_item, increment, gift_brand_name, currency_type, currency_value, currency_value1, currency_type1;

        public GIftHolder(View itemView) {
            super(itemView);
            gift_item_view_layout = itemView.findViewById(R.id.gift_item_view_layout);
            gift_image = itemView.findViewById(R.id.gift_image);
            decrement = itemView.findViewById(R.id.decrement);
            count_item = itemView.findViewById(R.id.count_item);
            increment = itemView.findViewById(R.id.increment);
            gift_brand_name = itemView.findViewById(R.id.gift_brand_name);
            currency_type = itemView.findViewById(R.id.currency_type);
            currency_type1 = itemView.findViewById(R.id.currency_type1);
            currency_value = itemView.findViewById(R.id.currency_value);
            currency_value1 = itemView.findViewById(R.id.currency_value1);
        }
    }

    private void add_to_gift_service(RecyclerView.ViewHolder holder) {
        preferenceUtils=new PreferenceUtils(context);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.ADD_CART_SERVICE1(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(selectPosition).getProd_id(), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), count + "");

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
                        if (status == 1) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            if (context instanceof GiftsCardsActivity) {
                                GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(selectPosition).setQuantity(String.valueOf(count));
                                ((GiftsCardsActivity) context).methodtotalprc(amount, isPlus, GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(selectPosition), GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(selectPosition).getProd_id(), selectPosition);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDoalog.dismiss();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });

    }

    public void del_gift_service(RecyclerView.ViewHolder holder) {

        preferenceUtils = new PreferenceUtils(context);

        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.DECREMENT_CART_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(selectPosition).getProd_id(), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));

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
                        if (status == 1) {
                            String message = responseObject.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            if (context instanceof GiftsCardsActivity) {
                                giftListArray.get(selectPosition).setQuantity(String.valueOf(count));
                                ((GiftsCardsActivity) context).methodtotalprc(amount, isPlus, GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(selectPosition), GiftsCardsActivity.title_models.get(verticalPosition).getGiftItemModels().get(selectPosition).getProd_id(), selectPosition);
                            }
                        }
                    } catch (Exception e) {
//                        e.printStackTrace();
                        Log.e("error", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDoalog.dismiss();
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }

}
