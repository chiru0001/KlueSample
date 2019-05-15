package com.volive.klueapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.volive.klueapp.Fragments.Home;
import com.volive.klueapp.Models.NewArrivalModel;
import com.volive.klueapp.R;
import com.volive.klueapp.adpaters.NewArrivalAdapter;

import java.util.ArrayList;

public class NewArrivals extends BaseActivity {

    RecyclerView recyclerView;
  public  static  ArrayList<NewArrivalModel> new_arrival_list;
    ImageView back_button, cart;
    TextView tv_title;
    Toolbar rl_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_new_arrivals);
        initializeUI();
        if (checkConnection()) {
            if (getIntent().getExtras() != null) {
                new_arrival_list = (ArrayList<NewArrivalModel>) getIntent().getSerializableExtra("arraivals");
            }
            NewArrivalAdapter arrivalAdapter = new NewArrivalAdapter(this, new_arrival_list, "New Arrivals");
            recyclerView.setAdapter(arrivalAdapter);
        }else
            Toast.makeText(NewArrivals.this, "Please check Internet conncetion.", Toast.LENGTH_SHORT).show();

    }

    private void initializeUI() {
        rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.new_arrivals);
        recyclerView = findViewById(R.id.rview_new_arrivals);
        GridLayoutManager gManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gManager);
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewArrivals.this, MainActivity.class);
                startActivity(intent);
                finish();
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NewArrivals.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
