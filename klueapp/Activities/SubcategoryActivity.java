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
import com.volive.klueapp.Fragments.Home;
import com.volive.klueapp.Models.OccationsModel;
import com.volive.klueapp.R;
import com.volive.klueapp.Utils.API_class;
import com.volive.klueapp.Utils.Constants;
import com.volive.klueapp.Utils.PreferenceUtils;
import com.volive.klueapp.Utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.OccationsAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SubcategoryActivity extends BaseActivity {

    ImageView backarrow, filter, cart, navigation_icon;
    TextView tv_title;
    Toolbar toolbar;
    RecyclerView subcat_recyclerView;
   public static  ArrayList<OccationsModel> subcategoryList;
   PreferenceUtils preferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_subcategory);
        preferenceUtils=new PreferenceUtils(SubcategoryActivity.this);

        initializeUI();
        if (checkConnection()) {
            checkValuesAndCallApi();
        }else
            Toast.makeText(SubcategoryActivity.this, "Please check Internet conncetion.", Toast.LENGTH_SHORT).show();

    }

    private void initializeUI() {
        toolbar = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        tv_title = findViewById(R.id.tv_title);
        navigation_icon = findViewById(R.id.back_button);
        subcat_recyclerView = findViewById(R.id.rview_flowers);
        LinearLayoutManager lManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        subcat_recyclerView.setLayoutManager(lManager);
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(SubcategoryActivity.this, MainActivity.class);
                startActivity(intent);*/
                finish();
                onBackPressed();

            }
        });
    }

    private void setUprecyclerView() {

        if (subcategoryList!= null && subcategoryList.size() == 0){
            Toast.makeText(this, "No Items", Toast.LENGTH_LONG).show();
        } else {
            OccationsAdapter occationsAdapter = new OccationsAdapter(this, subcategoryList);
            subcat_recyclerView.setAdapter(occationsAdapter);
        }
    }

    private void checkValuesAndCallApi() {
        if (getIntent()!=null) {
            tv_title.setText(Home.category_list.get(Integer.parseInt(getIntent().getStringExtra("Position"))).getCname());
            String catId = Home.category_list.get(Integer.parseInt(getIntent().getStringExtra("Position"))).getCat_id();

            final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
            Call<JsonElement> callRetrofit = null;

            callRetrofit = service.SUB_CAT_SERVICE(Constants.API_KEY,preferenceUtils.getStringFromPreference(PreferenceUtils.Language,""), catId);

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
                                JSONArray subcatArray = responseObject.getJSONArray("data");
                                subcategoryList = new ArrayList<>();
                                for (int i = 0; i < subcatArray.length(); i++) {
                                    JSONObject subcatObject = subcatArray.getJSONObject(i);
                                    OccationsModel occationsModel = new OccationsModel();
                                    occationsModel.setSub_cat_id(subcatObject.getString("sub_cat_id"));
                                    occationsModel.setSub_cat_name(subcatObject.getString("sub_cat_name"));
                                    occationsModel.setSub_cat_image(base_path + subcatObject.getString("sub_cat_image"));
                                    subcategoryList.add(occationsModel);
                                }
                            }
                            setUprecyclerView();
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
        finish();

    }
}
