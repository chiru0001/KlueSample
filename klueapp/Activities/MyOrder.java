package com.volive.klueapp.Activities;


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
import com.volive.klueapp.Models.OrdersModel;
import com.volive.klueapp.R;
import com.volive.klueapp.Utils.API_class;
import com.volive.klueapp.Utils.Constants;
import com.volive.klueapp.Utils.PreferenceUtils;
import com.volive.klueapp.Utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.MyOrder_Adapter;
import com.volive.klueapp.adpaters.SubProListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyOrder extends AppCompatActivity {
    Toolbar rl_header;
    ImageView navigation_icon;
    TextView title;
    PreferenceUtils preferenceUtils;
    RecyclerView recyclerView;
    public  static ArrayList<OrdersModel> myOrders_list;
    MyOrder_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.myorder);
        initializeUI();
        myOrdersService();

    }

    private void initializeUI() {
        rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.my_orders));
        recyclerView = (RecyclerView) findViewById(R.id.rview_myorders_list);
        LinearLayoutManager lManager = new LinearLayoutManager(MyOrder.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lManager);
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyOrder.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MyOrder.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void myOrdersService(){
        preferenceUtils=new PreferenceUtils(MyOrder.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.MY_ORDERS_SERVICE(Constants.API_KEY,preferenceUtils.getStringFromPreference(PreferenceUtils.Language,""),preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,""));

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

                        if (status==1){
                            JSONArray myOrderArray=responseObject.getJSONArray("data");
                            myOrders_list = new ArrayList();
                            for (int i=0;i<myOrderArray.length();i++){
                                JSONObject orderObject=myOrderArray.getJSONObject(i);
                                OrdersModel ordersModel=new OrdersModel();
                                ordersModel.setOrder_id(orderObject.getString("order_id"));
                                ordersModel.setNo_of_items(orderObject.getString("no_of_items"));
                                ordersModel.setSub_total(orderObject.getString("sub_total"));
                                ordersModel.setOrder_date(orderObject.getString("order_date"));
                                ordersModel.setEstimate_date(orderObject.getString("estimate_date"));
                                myOrders_list.add(ordersModel);
                            }
                            setUprecyclerView();
                        }

                    }catch (Exception e){
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
        if (myOrders_list == null && myOrders_list.size() == 0) {
            Toast.makeText(this, "No Items", Toast.LENGTH_LONG).show();
        } else {
            adapter = new MyOrder_Adapter(MyOrder.this, myOrders_list,"my_orders");
            recyclerView.setAdapter(adapter);
        }
    }
}
