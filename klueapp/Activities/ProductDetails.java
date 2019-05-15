package com.volive.klueapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.volive.klueapp.Fragments.Home;
import com.volive.klueapp.Fragments.TopPicsFragment;
import com.volive.klueapp.R;
import com.volive.klueapp.Utils.API_class;
import com.volive.klueapp.Utils.Constants;
import com.volive.klueapp.Utils.PreferenceUtils;
import com.volive.klueapp.Utils.Retrofit_funtion_class;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetails extends AppCompatActivity {
    Toolbar rl_header;
    ImageView navigation_icon, backarrow, heart, product_img,cart;
    TextView title, item_name, description, item_brand, price_offer_pdt, details, qty, pdt_basic_price,
            increment, decrement, count_item, pdt_price_type, price_type_offer;
    Button addtocart;
    boolean ischecked;
    LinearLayout layout_basic_price;
    String prod_id = "";
    int count = 1;
    PreferenceUtils preferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.product_details);
        preferenceUtils = new PreferenceUtils(this);

        intializeUI();

        product_detail_service();

    }

    private void intializeUI() {
        cart=findViewById(R.id.cart);
        cart.setVisibility(View.GONE);
        description = findViewById(R.id.description);
        heart = findViewById(R.id.heart);
        product_img = findViewById(R.id.image);
        item_name = findViewById(R.id.item_name);
        item_brand = findViewById(R.id.item_brand);
        pdt_basic_price = findViewById(R.id.pdt_basic_price);
        pdt_price_type = findViewById(R.id.pdt_price_type);
        price_offer_pdt = findViewById(R.id.price_offer_pdt);
        price_type_offer = findViewById(R.id.price_type_offer);
        details = findViewById(R.id.details);
        qty = findViewById(R.id.qty);
        increment = findViewById(R.id.increment);
        decrement = findViewById(R.id.decrement);
        count_item = findViewById(R.id.count_item);
        layout_basic_price = findViewById(R.id.basic_price);
        pdt_basic_price.setPaintFlags(pdt_basic_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        pdt_price_type.setPaintFlags(pdt_price_type.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        item_name = findViewById(R.id.item_name);
        rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.product_details);
        backarrow = (ImageView) findViewById(R.id.back_button);
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(ProductDetails.this, MainActivity.class);
                startActivity(intent);*/
                finish();
                onBackPressed();
            }
        });
        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                count_item.setText(count + "");
            }
        });
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1) {
                    count--;
                    count_item.setText(count + "");
                }
            }
        });
        addtocart = (Button) findViewById(R.id.addtocart);
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_to_cart_service();
            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ischecked) {
                    ischecked = true;
                    addToWishlist();
                    heart.setImageResource(R.mipmap.whishlist_hvr);
                } else {
                    ischecked = false;
                    removeFromWishList();
                    heart.setImageResource(R.mipmap.heart);
                }
            }
        });
        count_item.setText(count + "");
    }

    private void removeFromWishList() {
        PreferenceUtils preferenceUtils = new PreferenceUtils(ProductDetails.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.DEL_FAVOURITE_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), prod_id);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ProductDetails.this);
        progressDoalog.setCancelable(false);
        progressDoalog.setMessage("Loading....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.show();

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                progressDoalog.dismiss();
                if (response.isSuccessful()) {
                    String searchResponse = response.body().toString();
                    try {
                        JSONObject responseObject = new JSONObject(searchResponse);
                        int status = responseObject.getInt("status");

                        if (status == 1) {
                            String message = responseObject.getString("message");
                            Toast.makeText(ProductDetails.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
            }
        });
    }

    private void addToWishlist() {
        PreferenceUtils preferenceUtils = new PreferenceUtils(ProductDetails.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.ADD_WHISLIST_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), prod_id);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ProductDetails.this);
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
                            Toast.makeText(ProductDetails.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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

    private void add_to_cart_service() {

        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.ADD_CART_SERVICE1(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), prod_id, preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), count + "");

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ProductDetails.this);
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
                            Toast.makeText(ProductDetails.this, message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProductDetails.this, ShoppingCart.class);
                            startActivity(intent);
                            finish();
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

    public void product_detail_service() {
        if (getIntent() != null) {
            if (getIntent().getStringExtra("Context").equalsIgnoreCase("Home")) {
                prod_id = Home.new_arrival_list.get(Integer.parseInt(getIntent().getStringExtra("Position"))).getProd_id();
            } else if (getIntent().getStringExtra("Context").equalsIgnoreCase("New Arrivals")) {
                prod_id = NewArrivals.new_arrival_list.get(Integer.parseInt(getIntent().getStringExtra("Position"))).getProd_id();
            } else if (getIntent().getStringExtra("Context").equalsIgnoreCase("Anniversary")) {
                prod_id = Anniversary.product_arrayList.get(Integer.parseInt(getIntent().getStringExtra("Position"))).getProd_id();
            } else if (getIntent().getStringExtra("Context").equalsIgnoreCase("Top Pics")) {
                prod_id = TopPicsFragment.top_pics_list.get(Integer.parseInt(getIntent().getStringExtra("Position"))).getProd_id();
            }

            final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
            Call<JsonElement> callRetrofit = null;
            callRetrofit = service.PRODUCT_INFO(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), prod_id, preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));

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
                                String base_path = responseObject.getString("base_path");
                                JSONObject pdt_info_data = responseObject.getJSONObject("data");
                                item_brand.setText(pdt_info_data.getString("brand_name"));
                                item_name.setText(pdt_info_data.getString("pname"));
                                description.setText(pdt_info_data.getString("description"));
                                Glide.with(ProductDetails.this).load(base_path + pdt_info_data.getString("prod_image")).into(product_img);
                                price_offer_pdt.setText(pdt_info_data.getString("price_sar"));
                                pdt_basic_price.setText(pdt_info_data.getString("regular_price_sar"));
                                if (pdt_info_data.getInt("is_wish_list") == 1) {
                                    heart.setImageResource(R.mipmap.whishlist_hvr);
                                } else if (pdt_info_data.getInt("is_wish_list") == 0) {
                                    heart.setImageResource(R.mipmap.heart);
                                }
                            }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }
}
