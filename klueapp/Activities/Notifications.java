package com.volive.klueapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.volive.klueapp.R;
import com.volive.klueapp.adpaters.NotificationAdapter;

import java.util.ArrayList;

/**
 * Created by 01 on 16-Feb-18.
 */

public class Notifications extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList notify_list_array;
    ImageView backarrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.notifications);

        Toolbar rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ImageView navigation_icon = (ImageView) findViewById(R.id.back_button);
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.notifications);


        recyclerView=findViewById(R.id.rview_notification);
        LinearLayoutManager lManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(lManager);

        NotificationAdapter adapter=new NotificationAdapter(this,notify_list_array);
        recyclerView.setAdapter(adapter);



        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Notifications.this,MainActivity.class);
                startActivity(intent);
                finish();
                onBackPressed();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Notifications.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
