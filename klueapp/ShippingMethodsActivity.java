package com.volive.klueapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.volive.klueapp.Activities.OrderSummary;
import com.volive.klueapp.Activities.Payment_Method;
import com.volive.klueapp.Activities.ShoppingCart;
import com.volive.klueapp.Models.CartItemModel;
import com.volive.klueapp.Models.ShippingMethodModel;
import com.volive.klueapp.Utils.API_class;
import com.volive.klueapp.Utils.Constants;
import com.volive.klueapp.Utils.MyAddressHelper;
import com.volive.klueapp.Utils.PreferenceUtils;
import com.volive.klueapp.Utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.AddressAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShippingMethodsActivity extends AppCompatActivity {
    public static ArrayList<ShippingMethodModel> methodModels;
    Toolbar rl_header;
    ImageView navigation_icon;
    TextView title, process;
    RecyclerView shipping_method_list;
    ShippingMethodsAdpter adapter;
    PreferenceUtils preferenceUtils;
    String address_id,ship_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shipping_methods);
        initializeUI();
        myOrdersService();


    }

    private void initializeUI() {

        rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText("Shipping Methods");
        process = findViewById(R.id.process);
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShippingMethodsActivity.this, OrderSummary.class);
                startActivity(intent);
                finish();
                onBackPressed();
            }
        });
        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShippingMethodsAdpter.shippingMethodPosition != -1) {
                    address_id = MyAddressHelper.address_list_array.get(AddressAdapter.addressPosition).getShipping_id();
                    ship_id = ShippingMethodsActivity.methodModels.get(ShippingMethodsAdpter.shippingMethodPosition).getId();
                    bookingOrdersService();
                } else {
                    Toast.makeText(ShippingMethodsActivity.this, getResources().getString(R.string.please_select_delivery), Toast.LENGTH_SHORT).show();
                }
            }
        });
        shipping_method_list = findViewById(R.id.shipping_method_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShippingMethodsActivity.this, LinearLayoutManager.VERTICAL, false);
        shipping_method_list.setLayoutManager(layoutManager);
    }

    public void myOrdersService() {
        preferenceUtils = new PreferenceUtils(ShippingMethodsActivity.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.SHIPPING_METHODS_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""));

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
                            JSONArray shiping_method_Array = responseObject.getJSONArray("data");
                            methodModels = new ArrayList();
                            for (int i = 0; i < shiping_method_Array.length(); i++) {
                                JSONObject orderObject = shiping_method_Array.getJSONObject(i);
                                ShippingMethodModel shippingMethodModel = new ShippingMethodModel();
                                shippingMethodModel.setId(orderObject.getString("id"));
                                shippingMethodModel.setShipping_name(orderObject.getString("shipping_name"));
                                shippingMethodModel.setPrice_sar(orderObject.getString("price_sar"));

                                methodModels.add(shippingMethodModel);
                            }
                            setUprecyclerView();
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

    private void setUprecyclerView() {
        if (methodModels == null && methodModels.size() == 0) {
            Toast.makeText(this, "No Items", Toast.LENGTH_LONG).show();
        } else {
            adapter = new ShippingMethodsAdpter(ShippingMethodsActivity.this, methodModels);
            shipping_method_list.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ShippingMethodsActivity.this, OrderSummary.class);
        startActivity(intent);
    }

    public void bookingOrdersService(){
        preferenceUtils = new PreferenceUtils(ShippingMethodsActivity.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.BOOKING_ORDERS_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""),preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,""),address_id,ship_id);
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
                           String message=responseObject.getString("message");
                           Toast.makeText(ShippingMethodsActivity.this,message,Toast.LENGTH_SHORT);
                            Intent intent = new Intent(ShippingMethodsActivity.this, Payment_Method.class);
                            startActivity(intent);
                            finish();
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
}