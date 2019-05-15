package com.volive.klueapp.Activities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.volive.klueapp.Models.CityModel;
import com.volive.klueapp.R;
import com.volive.klueapp.Utils.API_class;
import com.volive.klueapp.Utils.Constants;
import com.volive.klueapp.Utils.PreferenceUtils;
import com.volive.klueapp.Utils.Retrofit_funtion_class;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OnBoardingScreen extends AppCompatActivity implements View.OnClickListener {


    TextView language,select_city;
    TextView guest;
    Button register;
    Button signin;
    String strSelectLanguage = "";
    String languageToLoad;
    List<String> cities_list;
    PreferenceUtils preferenceUtils;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        loadLocale();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.onboardingscreen);

        preferenceUtils = new PreferenceUtils(this);
        strSelectLanguage = preferenceUtils.getStringFromPreference(PreferenceUtils.Language, "");
        initializeUI();
        initializeValues();
    }

    private void initializeUI() {

        guest = findViewById(R.id.guest);
        register = findViewById(R.id.register);
        signin = findViewById(R.id.signin);
        language = findViewById(R.id.language);
        select_city=findViewById(R.id.select_city);
    }

    private void initializeValues() {
        guest.setOnClickListener(this);
        register.setOnClickListener(this);
        signin.setOnClickListener(this);
        language.setOnClickListener(this);
        select_city.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.guest:
                intent = new Intent(OnBoardingScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.register:
                intent = new Intent(OnBoardingScreen.this, Register.class);
                startActivity(intent);
                finish();
                break;
            case R.id.signin:
                intent = new Intent(OnBoardingScreen.this, Login.class);
                startActivity(intent);
                finish();
                break;
            case R.id.language:
                showLang();
                break;
            case R.id.select_city:
                getcityService();
                break;
        }
    }

    public void showLang() {
        final Dialog dialogLanguage = new Dialog(OnBoardingScreen.this);
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLanguage.setContentView(R.layout.dailog_select_language);
        dialogLanguage.setTitle(getResources().getString(R.string.change_language));
        dialogLanguage.setCanceledOnTouchOutside(true);
        dialogLanguage.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialogLanguage.getWindow();
        layoutParams.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);

        final RadioButton rbEnglish = (RadioButton) dialogLanguage.findViewById(R.id.rbEnglish);
        final RadioButton rbArbic = (RadioButton) dialogLanguage.findViewById(R.id.rbArbic);
        Button btnSubmit = (Button) dialogLanguage.findViewById(R.id.btnapply);
        Button btnCancel = (Button) dialogLanguage.findViewById(R.id.btncancel);

        if (strSelectLanguage.equalsIgnoreCase("en")) {
            rbEnglish.setChecked(true);

        } else if (strSelectLanguage.equalsIgnoreCase("ar")) {
            rbArbic.setChecked(true);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rbEnglish.isChecked()) {
                    languageToLoad = "en"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                    preferenceUtils.setLanguage("en");

                    dialogLanguage.dismiss();
                    Intent intent = new Intent(OnBoardingScreen.this, OnBoardingScreen.class);
                    startActivity(intent);
                    finish();
                } else if (rbArbic.isChecked()) {
                    languageToLoad = "ar"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                    preferenceUtils.setLanguage("ar");

                    dialogLanguage.dismiss();
                    Intent intent = new Intent(OnBoardingScreen.this, OnBoardingScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLanguage.dismiss();
            }
        });
        dialogLanguage.show();

    }

    public void getcityService() {
        preferenceUtils = new PreferenceUtils(OnBoardingScreen.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.CITI_LIST_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""));

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
                            JSONArray jsonArray=responseObject.getJSONArray("data");
                            cities_list=new ArrayList<>();
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                cities_list.add(jsonObject.getString("city_name"));
                            }
                            showCityDialog();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
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

    private void showCityDialog() {
        try {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_alert_list_dialog, null);
            dialogBuilder.setView(dialogView);
            List<String> list = new ArrayList<>();

            TextView title = (TextView) dialogView.findViewById(R.id.customDialogTitle);

            final ListView cityListView = (ListView) dialogView.findViewById(R.id.cityList);
            final AlertDialog alertDialog = dialogBuilder.create();
            try {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, cities_list);
                // Assign adapter to ListView
                cityListView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // ListView Item Click Listener
            cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String itemValue = (String) cityListView.getItemAtPosition(position);
                    select_city.setText(itemValue);
                    preferenceUtils.saveString(PreferenceUtils.City, itemValue);
                    alertDialog.dismiss();

                }

            });
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}