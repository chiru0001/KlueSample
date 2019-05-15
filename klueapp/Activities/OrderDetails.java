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

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.volive.klueapp.Models.OrderDetailModel;
import com.volive.klueapp.R;
import com.volive.klueapp.Utils.API_class;
import com.volive.klueapp.Utils.Constants;
import com.volive.klueapp.Utils.PreferenceUtils;
import com.volive.klueapp.Utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.MyOrder_Adapter;
import com.volive.klueapp.adpaters.OrderDetailAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderDetails extends BaseActivity {
    public static ArrayList<OrderDetailModel> orderDetailModels;
    Toolbar rl_header;
    ImageView navigation_icon, order_img;
    PreferenceUtils preferenceUtils;
    String st_order_id;
    RecyclerView rview_order_detail_list;
    String  order_dat,estimated_dat;

    TextView title, order_date, order_id,  order_status,estimate_date,username,address,landmark,
            city,email,mobile_num,price_type,pdt_value,net_price,payment_type,total_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.orderdetails);
        preferenceUtils = new PreferenceUtils(OrderDetails.this);

        Intent intent = getIntent();
        st_order_id = intent.getStringExtra("order_id");
        order_dat = intent.getStringExtra("order_date");
        estimated_dat = intent.getStringExtra("estimate_date");
//        st_order_id = intent.getStringExtra("my_orders");
        //st_order_id=MyOrder.myOrders_list.get(MyOrder_Adapter.orderPosition).getOrder_id();

        initializeUI();
        if (checkConnection()) {
            orderDetailService();
        } else
            Toast.makeText(OrderDetails.this, "Please check Internet conncetion.", Toast.LENGTH_SHORT).show();
    }

    private void initializeUI() {
        rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.order_details);

        order_date = findViewById(R.id.order_date);
        order_date .setText(order_dat);

        estimate_date = findViewById(R.id.estimate_date);
        estimate_date.setText(estimated_dat);
        order_id = findViewById(R.id.order_id_detail);
        order_id.setText(st_order_id);

        order_img=findViewById(R.id.order_img);
        order_status = findViewById(R.id.order_status);
        username = findViewById(R.id.username);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        landmark = findViewById(R.id.landmark);
        email = findViewById(R.id.email);
        mobile_num = findViewById(R.id.mobile_number);
        price_type = findViewById(R.id.price_type);
        pdt_value = findViewById(R.id.pdt_value);
        payment_type = findViewById(R.id.payment_type);
        net_price = findViewById(R.id.net_price);
        total_price = findViewById(R.id.total_price);

        rview_order_detail_list = findViewById(R.id.rview_order_detail_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderDetails.this, LinearLayoutManager.VERTICAL, false);
        rview_order_detail_list.setLayoutManager(layoutManager);
       /* rview_order_detail_list.setHasFixedSize(true);
        rview_order_detail_list.setNestedScrollingEnabled(false);
*/
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetails.this, MyOrder.class);
                startActivity(intent);
                finish();
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrderDetails.this, MyOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void orderDetailService() {
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.MY_ORDER_DETAILS_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""), st_order_id);

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
                        String base_path = responseObject.getString("base_path");
                        String sub_total = responseObject.getString("sub_total");
                        total_price.setText(sub_total);
                        net_price.setText(sub_total);
                        pdt_value.setText(sub_total);

                        if (status == 1) {
                            JSONArray jsonArray = responseObject.getJSONArray("data");
                            orderDetailModels = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                OrderDetailModel detailModel = new OrderDetailModel();
                                detailModel.setProd_id(jsonObject.getString("prod_id"));
                                detailModel.setProduct_qty(jsonObject.getString("product_qty"));
                                detailModel.setPrice(jsonObject.getString("price"));
                                detailModel.setBrand_name(jsonObject.getString("brand_name"));
                                detailModel.setProd_name(jsonObject.getString("prod_name"));
                                detailModel.setProd_image(base_path + jsonObject.getString("prod_image"));
                                orderDetailModels.add(detailModel);
                            }
                            setUprecyclerView();
                            JSONObject shipping_info_data = responseObject.getJSONObject("shipping_details");
                            username.setText(shipping_info_data.getString("username"));
                            address.setText(shipping_info_data.getString("address"));
                            landmark.setText(shipping_info_data.getString("landmark"));
                            city.setText(shipping_info_data.getString("city"));
                            email.setText(shipping_info_data.getString("email"));
                            mobile_num.setText(shipping_info_data.getString("mobile"));
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
        if (orderDetailModels == null && orderDetailModels.size() == 0) {
            Toast.makeText(this, "No Items", Toast.LENGTH_LONG).show();
        } else {
            OrderDetailAdapter adapter = new OrderDetailAdapter(OrderDetails.this, orderDetailModels, "my_orders");
            rview_order_detail_list.setAdapter(adapter);
        }
    }
}

