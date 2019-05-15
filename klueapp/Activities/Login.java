package com.volive.klueapp.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.R;
import com.volive.klueapp.ShippingMethodsActivity;
import com.volive.klueapp.Utils.API_class;
import com.volive.klueapp.Utils.Constants;
import com.volive.klueapp.Utils.PreferenceUtils;
import com.volive.klueapp.Utils.Retrofit_funtion_class;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends BaseActivity implements View.OnClickListener {

    EditText email, pwd, editText;
    LinearLayout forgot_password;
    ImageView back;
    public static LinearLayout linearLayout,lin_layout;
    Button signin;
    Context context;
    TextView textView, txt_ok;

    String st_email, st_password, token = "sdff";
    PreferenceUtils preferenceUtils;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        context = this;
        preferenceUtils = new PreferenceUtils(this);
        initaializeUI();

       /* email.setText("chiru@mail.com");
        pwd.setText("12345");*/
       email.setText("volive@gmail.com");
       pwd.setText("123");
    }

    private void initaializeUI() {
        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.password);
        forgot_password = (LinearLayout) findViewById(R.id.forgot_password);
        back = (ImageView) findViewById(R.id.backarrow);
        signin = (Button) findViewById(R.id.signinbtn);
        linearLayout = (LinearLayout) findViewById(R.id.linearlogin);
        lin_layout=findViewById(R.id.lin_layout);
        back.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
        signin.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, OnBoardingScreen.class);
                startActivity(intent);
                finish();
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        final Intent intent;
        switch (v.getId()) {
            case R.id.signinbtn:
                if (checkConnection()) {
                    st_email = email.getText().toString().trim();
                    st_password = pwd.getText().toString().trim();

                    if (!st_email.isEmpty() && !st_password.isEmpty()) {
                        loginService();

//                    doAndroidNetworking();
                    } else {
                        Toast.makeText(Login.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(Login.this, "Please check Internet conncetion.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearlogin:
                intent = new Intent(Login.this, Register.class);
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                startActivity(intent);
                finish();
                break;
            case R.id.forgot_password:
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.forgot_password_dialog, null);
                dialogBuilder.setView(dialogView);
                textView = dialogView.findViewById(R.id.tv_number);
                editText = dialogView.findViewById(R.id.et_number);
                txt_ok = dialogView.findViewById(R.id.ok);
                final AlertDialog alertDialog = dialogBuilder.create();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                Window window = alertDialog.getWindow();
                layoutParams.copyFrom(window.getAttributes());
                // This makes the dialog take up the full width
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(layoutParams);
                txt_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!editText.getText().toString().isEmpty()) {
                           st_email= editText.getText().toString().trim();
                            forgotPasswordService();
//                            Toast.makeText(Login.this, R.string.pwd_send_to_mobile_successfully, Toast.LENGTH_LONG).show();
                            alertDialog.dismiss();

                        } else {
                            Toast.makeText(Login.this, R.string.enter_your_mobile_num, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertDialog.show();
                break;
        }
    }

    public void loginService() {
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.USER_LOGIN(Constants.API_KEY, st_email, st_password, preferenceUtils.getStringFromPreference(PreferenceUtils.Language,""), Constants.DIVICE_TYPE,token);

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
                    Log.d("Login", "response  >>" + searchResponse.toString());
                    try {
                        JSONObject lObj = new JSONObject(searchResponse);
                        int status = lObj.getInt("status");
                        String message = lObj.getString("message");
                        if (status == 1) {
                            JSONObject jsonObject = lObj.getJSONObject("data");
                            String user_id = jsonObject.getString("user_id");
                            String fname = jsonObject.getString("fname");
                            String email = jsonObject.getString("email");
                            String password = jsonObject.getString("password");
                            String mobile = jsonObject.getString("mobile");
                            String image = jsonObject.getString("image");
                            String base_path = lObj.getString("base_path");

                            preferenceUtils.saveString(PreferenceUtils.User_name, fname);
                            preferenceUtils.saveString(PreferenceUtils.USER_ID, user_id);
                            preferenceUtils.saveString(PreferenceUtils.Email, email);
                            preferenceUtils.saveString(PreferenceUtils.Password, password);
                            preferenceUtils.saveString(PreferenceUtils.Mobile, mobile);
                            preferenceUtils.saveString(PreferenceUtils.IMAGE, image);
                            preferenceUtils.saveString(PreferenceUtils.BASE_PATH, base_path);

                            preferenceUtils.saveBoolean(PreferenceUtils.LOG_OUT, false);

                            startActivity(new Intent(context, MainActivity.class));
                            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                            finish();

                           /* Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            final Timer t = new Timer();
                            t.schedule(new TimerTask() {
                                public void run() {
                                    if (user_type.equals("1")) {

                                        startActivity(new Intent(context, MainActivity.class));
                                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                                        finish();
                                    } else {
                                        startActivity(new Intent(context, MainActivity.class));
                                        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                                        finish();
                                    }


                                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                                }
                            }, 1000); // after 2 second (or 2000 miliseconds), the task will be active. */


                        } else {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Login.this, OnBoardingScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void forgotPasswordService(){
        preferenceUtils = new PreferenceUtils(Login.this);
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.FORGOT_PWD_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""),st_email);
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
                            Toast.makeText(Login.this,message,Toast.LENGTH_SHORT).show();

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