package com.volive.klueapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.volive.klueapp.R;
import com.volive.klueapp.ShippingMethodsActivity;
import com.volive.klueapp.Utils.MyAddressHelper;
import com.volive.klueapp.Utils.PreferenceUtils;
import com.volive.klueapp.adpaters.AddressAdapter;
import com.volive.klueapp.adpaters.OrderDetailAdapter;
import com.volive.klueapp.adpaters.ShoppingCartAdapter;

public class OrderSummary extends BaseActivity {

    RecyclerView recyclerView;
    LinearLayout addnewaddress;
    Button button;
    Toolbar rl_header;
    ImageView navigation_icon;
    TextView title;
    PreferenceUtils preferenceUtils;
    MyAddressHelper addressHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.order_summary);
        initializeUI();
        addressHelper = new MyAddressHelper(OrderSummary.this);
        recyclerView = (RecyclerView) findViewById(R.id.rview_address_list);
        LinearLayoutManager lManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lManager);
        if (checkConnection()) {
            addressHelper.getShippingAddressService(OrderSummary.this, recyclerView);
        }else
            Toast.makeText(OrderSummary.this, "Please check Internet conncetion.", Toast.LENGTH_SHORT).show();

    }

    private void initializeUI() {
        rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.order_summary);


        addnewaddress = (LinearLayout) findViewById(R.id.addnewaddress);
        button = (Button) findViewById(R.id.proceed_pay);
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSummary.this, GiftsCardsActivity.class);
                startActivity(intent);
                finish();
                onBackPressed();
            }
        });
        addnewaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderSummary.this, AddNewAddress.class);
                startActivity(intent);
                finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AddressAdapter.addressPosition!=-1) {
                    Intent intent = new Intent(OrderSummary.this, ShippingMethodsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(OrderSummary.this, getResources().getString(R.string.please_select_address), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrderSummary.this, GiftsCardsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
