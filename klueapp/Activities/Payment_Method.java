package com.volive.klueapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.volive.klueapp.R;
import com.volive.klueapp.ShippingMethodsActivity;

/**
 * Created by 01 on 2/20/2018.
 */

public class Payment_Method extends AppCompatActivity {

    FrameLayout simpleFrameLayout;
    TabLayout tabLayout;
    Button payment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.payment_method);

        initializeUI();
    }

    private void initializeUI() {
        Toolbar rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ImageView navigation_icon = (ImageView) findViewById(R.id.back_button);
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.select_payment_mode);

        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText(R.string.debit_card);
        tabLayout.addTab(firstTab);
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText(R.string.credit_card);
        tabLayout.addTab(secondTab);
        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText(R.string.cod);
        tabLayout.addTab(thirdTab);
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment_Method.this, ShippingMethodsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        payment = (Button) findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment_Method.this, PaymentSuccessful.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
