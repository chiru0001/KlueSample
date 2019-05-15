package com.volive.klueapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.Models.GiftItemModel;
import com.volive.klueapp.Models.Title_model;
import com.volive.klueapp.R;
import com.volive.klueapp.ShippingMethodsActivity;
import com.volive.klueapp.Utils.API_class;
import com.volive.klueapp.Utils.Constants;
import com.volive.klueapp.Utils.PreferenceUtils;
import com.volive.klueapp.Utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.GiftsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiftsCardsActivity extends BaseActivity implements View.OnClickListener {

    TextView title, tv_gift_card_name, tv_subTotal,
            tv_price_type, total_price,tv_skip;
    RecyclerView rview_gifts_list_items;
    TextView buy_gift_items;
    Toolbar rl_header;
    ImageView navigation_icon;
    LinearLayoutManager layoutManager;
    ArrayList<GiftItemModel> rview_gifts_list = null;
    ArrayList<String> titles_list;
   public static ArrayList<Title_model> title_models;

    PreferenceUtils preferenceUtils;
    GiftsAdapter giftAdapter;
    String titleValue = "";
    List<GiftItemModel> savedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gifts_cards);
        preferenceUtils = new PreferenceUtils(this);
        initializeUI();
        initializeValues();
        giftCardsService();
    }

    private void setUprecyclerView() {
        if (rview_gifts_list != null && rview_gifts_list.size() == 0) {
            Toast.makeText(this, "No Items", Toast.LENGTH_LONG).show();
        } else {
            giftAdapter = new GiftsAdapter(this, title_models);
            rview_gifts_list_items.setAdapter(giftAdapter);
        }
    }

    private void giftCardsService() {
        title_models = new ArrayList<>();
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.GIFT_PRODUCTS_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""));

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(this);
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
                        if (status == 1) {
                            JSONObject data_Object = responseObject.getJSONObject("data");
                            String base_path = responseObject.getString("base_path");

                            Iterator<String> iter = data_Object.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                try {
                                    titles_list = new ArrayList<>();
                                    rview_gifts_list = new ArrayList();
                                    JSONArray gift_items_array = data_Object.getJSONArray(key);
                                    titles_list.add(key);
                                    Title_model title_model = new Title_model();
                                    title_model.setTitles(key);
                                    for (int i = 0; i < gift_items_array.length(); i++) {
                                        JSONObject sub_cat_pdt_object = gift_items_array.getJSONObject(i);
                                        GiftItemModel giftItemModel = new GiftItemModel();
                                        giftItemModel.setProd_id(sub_cat_pdt_object.getString("prod_id"));
                                        giftItemModel.setSub_cat_name(sub_cat_pdt_object.getString("sub_cat_name"));
                                        giftItemModel.setVendor_id(sub_cat_pdt_object.getString("vendor_id"));
                                        giftItemModel.setBrand_name(sub_cat_pdt_object.getString("brand_name"));
                                        giftItemModel.setPname(sub_cat_pdt_object.getString("pname"));
                                        giftItemModel.setProd_image(base_path + sub_cat_pdt_object.getString("prod_image"));
                                        giftItemModel.setRegular_price_sar(sub_cat_pdt_object.getString("regular_price_sar"));
                                        giftItemModel.setPrice_sar(sub_cat_pdt_object.getString("price_sar"));
                                        giftItemModel.setQuantity("0");
                                        rview_gifts_list.add(giftItemModel);
                                        if (!titleValue.equals(key)) {
                                            title_model.setTitleName(sub_cat_pdt_object.getString("sub_cat_name"));
                                            titleValue = key;
                                        }

                                    }
                                    title_model.setGiftItemModels(rview_gifts_list);
                                    title_models.add(title_model);
                                    setUprecyclerView();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });
    }

    private void initializeUI() {
        rl_header = (Toolbar) findViewById(R.id.header);
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText("Gift Preparation");
        buy_gift_items = findViewById(R.id.buy_gift_items);
        //TextViews
        tv_gift_card_name = findViewById(R.id.tv_gift_card_name);
        tv_subTotal = findViewById(R.id.tv_subTotal);
        tv_price_type = findViewById(R.id.tv_price_type);
        total_price = findViewById(R.id.total_price);
        tv_skip=findViewById(R.id.tv_skip);
        tv_skip.setVisibility(View.VISIBLE);
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GiftsCardsActivity.this, OrderSummary.class);
                startActivity(intent);
                finish();
            }
        });

        //Recycler Views
        rview_gifts_list_items = findViewById(R.id.rview_gifts_list_items);
    }

    private void initializeValues() {
        savedList = new ArrayList<>();
        //Grid Layout Manager
        layoutManager = new LinearLayoutManager(GiftsCardsActivity.this, LinearLayoutManager.VERTICAL, false);
        rview_gifts_list_items.setLayoutManager(layoutManager);

        buy_gift_items.setOnClickListener(this);
        navigation_icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.buy_gift_items:
                intent = new Intent(GiftsCardsActivity.this, OrderSummary.class);
                startActivity(intent);
                finish();
                break;
            case R.id.back_button:
                intent = new Intent(GiftsCardsActivity.this, ShoppingCart.class);
                startActivity(intent);
                finish();
                onBackPressed();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GiftsCardsActivity.this, ShoppingCart.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void methodtotalprc(Double value, boolean sts, GiftItemModel giftItemModel, String prod_id, int selectPosition) {
        if (sts) {
            try {
                Double getprc = Double.parseDouble(total_price.getText().toString()) + value;
                total_price.setText(String.valueOf(getprc));
                StringBuilder stringBuilder = new StringBuilder();
                if (savedList.size() > 0) {
                    for (int i = 0; i < savedList.size(); i++) {
                        stringBuilder.append(savedList.get(i).getProd_id());
                    }
                    if (stringBuilder.toString().contains(prod_id)) {
                        savedList.remove(giftItemModel);
                        savedList.add(giftItemModel);
                    } else {
                        savedList.add(giftItemModel);
                    }
                } else {
                    savedList.add(giftItemModel);
                }
            } catch ( Exception e){
                e.printStackTrace();
            }
        } else {
            try {
                Double getprc = Double.parseDouble(total_price.getText().toString()) - value;
                total_price.setText(String.valueOf(getprc));
                StringBuilder stringBuilder = new StringBuilder();
                if (!rview_gifts_list.get(selectPosition).getQuantity().equalsIgnoreCase("0")) {
                    if (savedList.size() > 0) {
                        for (int i = 0; i < savedList.size(); i++) {
                            stringBuilder.append(savedList.get(i).getProd_id());
                        }
                        if (stringBuilder.toString().contains(prod_id)) {
                            savedList.remove(giftItemModel);
                            savedList.add(giftItemModel);
                        } else {
                            savedList.add(giftItemModel);
                        }
                    } else {
                        savedList.add(giftItemModel);
                    }
                } else
                    savedList.remove(giftItemModel);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
