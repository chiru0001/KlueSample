package com.volive.klueapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.volive.klueapp.R;
import com.volive.klueapp.Utils.ConnectivityReceiver;
import com.volive.klueapp.app.MyApplication;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.volive.klueapp.Activities.AddNewAddress.lin_lay;
import static com.volive.klueapp.Activities.Login.lin_layout;
import static com.volive.klueapp.Activities.MainActivity.mDrawerLayout;

public class BaseActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    Context context;
    static SweetAlertDialog sweetAlertDialog;
//    Toolbar rl_header;
   /* ImageView navigation_icon;
    ImageView notifi, cart;
    TextView title, logout, help_center;
    ListView navi_menu;
    DrawerLayout mDrawerLayout;
    NavigationView nav_view;
*/


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    /*    rl_header = (Toolbar) findViewById(R.id.header);
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        navigation_icon = (ImageView) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.tv_title);
        cart = (ImageView) findViewById(R.id.cart);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShoppingCart.class);
                startActivity(intent);
                finish();
            }
        });

        if (context instanceof MainActivity)
            title.setText(getResources().getString(R.string.home));
        if (context instanceof Anniversary)
            title.setText(getResources().getString(R.string.anniversary));
        cart.setVisibility(View.VISIBLE);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_view = (NavigationView) findViewById(R.id.nav_view);

        navigation_icon.setImageResource(R.drawable.menu_icon);
        navigation_icon.getLayoutParams().height = 70;
        navigation_icon.getLayoutParams().width = 85;

        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });*/
    }

    public void message(String message) {
        Snackbar snackbar = Snackbar
                .make(lin_lay, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawermenu, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    public boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }

    public static void showDialog(Context context, int type, String title, String message, String confirmText, SweetAlertDialog.OnSweetClickListener listener) {
        sweetAlertDialog = new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText(confirmText)
                .showCancelButton(false)
                .setConfirmClickListener(listener);
        sweetAlertDialog.show();
    }

    /*// Showing the status in Snackbar
    public void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "No internet Connction.Plz Try again later...";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar.make(lin_layout, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }*/

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener((ConnectivityReceiver.ConnectivityReceiverListener) context);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected){
            showDialog(this, SweetAlertDialog.WARNING_TYPE, "", "Please make sure that Internet is connected.", "OK!",
                    new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            if (checkConnection())
                                recreate();
                        }
                    });
        } else {
            if (sweetAlertDialog!=null && sweetAlertDialog.isShowing())
                sweetAlertDialog.dismissWithAnimation();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_item_home:
                if (context instanceof MainActivity) {
                    MainActivity.changeFragment(0);
                }else{
                    intent=new Intent(context,MainActivity.class);
                    intent.putExtra("Option","true");
                    intent.putExtra("Position","0");
                    startActivity(intent);
                }
                return true;
            case R.id.nav_item_myorder:
                intent = new Intent(this, MyOrder.class);
                startActivity(intent);
                return true;
            case R.id.nav_item_whislist:
                if (context instanceof MainActivity) {
                    MainActivity.changeFragment(3);
                }else{
                    intent=new Intent(context,MainActivity.class);
                    intent.putExtra("Option","true");
                    intent.putExtra("Position","3");
                    startActivity(intent);
                }
                return true;
            case R.id.nav_item_notifications:
                intent = new Intent(this, Notifications.class);
                startActivity(intent);
                return true;
            case R.id.nav_item_cart:
                intent = new Intent(this, ShoppingCart.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
