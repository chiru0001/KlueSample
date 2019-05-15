package com.volive.klueapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.volive.klueapp.Models.OccationsModel;
import com.volive.klueapp.R;
import com.volive.klueapp.Utils.MyAddressHelper;
import com.volive.klueapp.adpaters.OccationsAdapter;

import java.util.ArrayList;


public class MyAddressActivity extends AppCompatActivity {

    Toolbar rl_header;
    ImageView navigation_icon;
    TextView title;
    RecyclerView recyclerView;
    LinearLayout addnewaddress;
    MyAddressHelper addressHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_address);
        intializeUI();

        addressHelper=new MyAddressHelper(MyAddressActivity.this);
        recyclerView = findViewById(R.id.rview_myAddress);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        addressHelper.getShippingAddressService(MyAddressActivity.this, recyclerView);

      /*  if (checkConnection()) {
            addressHelper.getShippingAddressService(MyAddressActivity.this, recyclerView);
        }else
            Toast.makeText(MyAddressActivity.this, "Please check Internet conncetion.", Toast.LENGTH_SHORT).show();
*/
    }

    private void intializeUI() {
        rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.my_address));

        addnewaddress=findViewById(R.id.addnewaddress);
        addnewaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyAddressActivity.this,AddNewAddress.class);
                startActivity(intent);
                finish();
            }
        });


        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAddressActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MyAddressActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(intent);
        finish();
    }
}