package com.volive.klueapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.JsonElement;
import com.volive.klueapp.Fragments.Home;
import com.volive.klueapp.Models.OrdersModel;
import com.volive.klueapp.R;
import com.volive.klueapp.Utils.API_class;
import com.volive.klueapp.Utils.Constants;
import com.volive.klueapp.Utils.PreferenceUtils;
import com.volive.klueapp.Utils.Retrofit_funtion_class;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Filters extends AppCompatActivity {

    Toolbar rl_header;
    ImageView navigation_icon;
    TextView title;
    CrystalRangeSeekbar seekbar;
    Button button;
    LinearLayout brand_pg;
    PreferenceUtils preferenceUtils;
    String lowPrice="100";
    String highPrice="10000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.filter_screen);
        initalizeUI();
        initalizeValues();
//        filtersService();
    }

    private void initalizeUI() {
        rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.filters);
        button = (Button) findViewById(R.id.filter_apply);
        brand_pg = (LinearLayout) findViewById(R.id.brand_pg);
        seekbar =findViewById(R.id.rangeSeekbar);


    }

    private void initalizeValues() {
        seekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                Toast.makeText(getApplicationContext(), minValue + "-" + maxValue, Toast.LENGTH_SHORT).show();
            }
        });

        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Filters.this, Anniversary.class);
                startActivity(intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Filters.this, Anniversary.class);
                startActivity(intent);
                finish();
            }
        });

        brand_pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Filters.this, Brand.class);
                startActivity(intent);
                finish();
            }
        });
    }

   /* public void filtersService(){

        preferenceUtils=new PreferenceUtils(Filters.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.FILTERS_SERVICE(Constants.API_KEY,preferenceUtils.getStringFromPreference(PreferenceUtils.Language,""),catId,brandId,lowPrice,highPrice);

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
    }*/
}
