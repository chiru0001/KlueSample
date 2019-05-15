package com.volive.klueapp.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.Models.SubProductListModel;
import com.volive.klueapp.R;
import com.volive.klueapp.Utils.API_class;
import com.volive.klueapp.Utils.Constants;
import com.volive.klueapp.Utils.PreferenceUtils;
import com.volive.klueapp.Utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.SubProListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Anniversary extends BaseActivity {

    public static ArrayList<SubProductListModel> product_arrayList;
    RecyclerView recyclerView;
    GridLayoutManager manager;
    PopupWindow window;
    Toolbar toolbar;
    ImageView navigation_icon;
    TextView tv_title;
    SubProListAdapter proListAdapter;
    PreferenceUtils preferenceUtils;
    LinearLayout fltr_screen, sortbtn;
    RadioButton rbPopularity, rb_price_low_to_high, rb_price_high_to_low, rb_new_arrivals;
    CardView cardViewSubmit, cardViewCancel;

    String title_key = null;
    int stats;
    String min_price,max_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.anniversary);
        preferenceUtils = new PreferenceUtils(Anniversary.this);
        Intent intent = getIntent();
        title_key = intent.getStringExtra("key");
        stats = intent.getIntExtra("status",0);

        preferenceUtils.saveString(PreferenceUtils.MAX_PRICE,max_price);
        preferenceUtils.saveString(PreferenceUtils.MINIMUM_PRICE,min_price);

        initializeUI();
        if (checkConnection()) {
            checkValuesAndCallApi();
        } else
            Toast.makeText(Anniversary.this, "Please check Internet conncetion.", Toast.LENGTH_SHORT).show();

    }

    private void initializeUI() {
        toolbar = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.sub_category));
        navigation_icon = findViewById(R.id.back_button);
        sortbtn = findViewById(R.id.sortbtn);
        fltr_screen = (LinearLayout) findViewById(R.id.fltr_screen);
        fltr_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Anniversary.this, Filters.class);
                startActivity(intent);
                finish();
            }
        });
        sortbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogCurrency = new Dialog(Anniversary.this);
                dialogCurrency.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogCurrency.setContentView(R.layout.dailog_sort_items);
                dialogCurrency.setCanceledOnTouchOutside(true);
                dialogCurrency.setCancelable(true);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                Window window = dialogCurrency.getWindow();
                layoutParams.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(layoutParams);
                rbPopularity = (RadioButton) dialogCurrency.findViewById(R.id.rbPopularity);
                rb_price_low_to_high = (RadioButton) dialogCurrency.findViewById(R.id.rb_price_low_to_high);
                rb_price_high_to_low = (RadioButton) dialogCurrency.findViewById(R.id.rb_price_high_to_low);
                rb_new_arrivals = (RadioButton) dialogCurrency.findViewById(R.id.rb_new_arrivals);
                cardViewSubmit = dialogCurrency.findViewById(R.id.btnapply);
                cardViewCancel = dialogCurrency.findViewById(R.id.btncancel);

                cardViewSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialogCurrency.dismiss();

                    }
                });

                cardViewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogCurrency.dismiss();
                    }
                });
                dialogCurrency.show();
                /*LayoutInflater inflater = (LayoutInflater) Anniversary.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.dailog_sort_items, null);
                window = new PopupWindow(layout, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
                window.showAtLocation(layout, Gravity.CENTER, 0, 0);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        window.dismiss();
                    }
                });*/
            }
        });
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Anniversary.this, MainActivity.class);
                startActivity(intent);*/
                finish();
                onBackPressed();

            }
        });
        recyclerView = findViewById(R.id.rview_anniversary);
        manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        /*sortbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = getLayoutInflater().inflate(R.layout.popupsort, null);
                window = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
                window.showAtLocation(view, Gravity.CENTER, 0, 0);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        window.dismiss();
                    }
                });
            }
        });

        fltr_screen = findViewById(R.id.fltr_screen);
        fltr_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Anniversary.this, Filters.class);
                startActivity(intent);
                finish();
            }
        });*/

    }

    private void setUprecyclerView() {
        if (product_arrayList == null && product_arrayList.size() == 0) {
            Toast.makeText(this, "No Items", Toast.LENGTH_LONG).show();
        } else {
            proListAdapter = new SubProListAdapter(Anniversary.this, product_arrayList, "Anniversary");
            recyclerView.setAdapter(proListAdapter);
        }
    }

    private void checkValuesAndCallApi() {
        String brandId = Brand.brand_list_array.get(Integer.parseInt(getIntent().getStringExtra("Position"))).getId();

        if (getIntent() != null && getIntent().getStringExtra("Position") != null && !getIntent().getStringExtra("Position").toString().equalsIgnoreCase(null)) {
            String sub_cat_id = SubcategoryActivity.subcategoryList.get(Integer.parseInt(getIntent().getStringExtra("Position"))).getSub_cat_id();
            tv_title.setText(SubcategoryActivity.subcategoryList.get(Integer.parseInt(getIntent().getStringExtra("Position"))).getSub_cat_name());

            final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
            Call<JsonElement> callRetrofit = null;
            if (stats==1) {
                callRetrofit = service.FILTERS_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), sub_cat_id,brandId,min_price,max_price);

            }else {
                callRetrofit = service.SUB_PRODUCT_LIST_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), sub_cat_id, preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));

            }
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
                                JSONArray subcat_product_array = responseObject.getJSONArray("data");
                                product_arrayList = new ArrayList();
                                for (int i = 0; i < subcat_product_array.length(); i++) {
                                    JSONObject sub_cat_pdt_object = subcat_product_array.getJSONObject(i);
                                    SubProductListModel subProductListModel = new SubProductListModel();
                                    subProductListModel.setProd_id(sub_cat_pdt_object.getString("prod_id"));
                                    subProductListModel.setPname(sub_cat_pdt_object.getString("pname"));
                                    subProductListModel.setBrand_name(sub_cat_pdt_object.getString("brand_name"));
                                    subProductListModel.setProd_image(base_path + sub_cat_pdt_object.getString("prod_image"));
                                    subProductListModel.setRegular_price_sar(sub_cat_pdt_object.getString("regular_price_sar"));
                                    subProductListModel.setPrice_sar(sub_cat_pdt_object.getString("price_sar"));
                                    subProductListModel.setIs_wish_list(Integer.parseInt(sub_cat_pdt_object.getString("is_wish_list")));
//                                    subProductListModel.setChecked(false);
                                    product_arrayList.add(subProductListModel);

                                }
                                setUprecyclerView();
                            } else
                                Toast.makeText(Anniversary.this, responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
//                            e.printStackTrace();
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
