package com.volive.klueapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.volive.klueapp.Fragments.CategoryScreen;
import com.volive.klueapp.Fragments.Home;
import com.volive.klueapp.Fragments.MyAccount;
import com.volive.klueapp.Fragments.TopPicsFragment;
import com.volive.klueapp.Fragments.Whishlist;
import com.volive.klueapp.R;
import com.volive.klueapp.Utils.BottomNavigationViewHelper;
import com.volive.klueapp.Utils.PreferenceUtils;
import com.volive.klueapp.adpaters.ViewPagerAdapter;

import java.util.Locale;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static ViewPager viewPager;
    public static TextView title;
    public static Toolbar rl_header;
    public static DrawerLayout mDrawerLayout;
    public static RecyclerView navi_menu;
    NavigationView nav_view;
    MenuItem prevMenuItem;
    ImageView navigation_icon, nav_pro_image;
    TextView logout, help_center, nav_pro_name, language_change,Currency_changer;
    String user_id;
    BottomNavigationView bottomNavigationView;
    LinearLayout navmyorder, navmyaddress, navmywishlist, headerView;
    PreferenceUtils preferenceUtils;
    String strSelectLanguage = "";
    String strCurrency="";
    String languageToLoad;
    String currencyToLoad;

    public static void changeFragment(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        preferenceUtils = new PreferenceUtils(this);
        user_id = preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, "");
        initializeUI();
        initializeValues();
        navi_menu.setOnClickListener(this);
        nav_pro_image = findViewById(R.id.nav_profile_image);
        nav_pro_name = findViewById(R.id.nav_profile_name);

        try {
            if (!user_id.equalsIgnoreCase("")) {
                if (!preferenceUtils.getStringFromPreference(PreferenceUtils.BASE_PATH, "").equalsIgnoreCase("") && !preferenceUtils.getStringFromPreference(PreferenceUtils.IMAGE, "").equalsIgnoreCase("")) {
                    Glide.with(context)
                            .load(preferenceUtils.getStringFromPreference(PreferenceUtils.BASE_PATH, "") + preferenceUtils.getStringFromPreference(PreferenceUtils.IMAGE, ""))
                            .into(nav_pro_image);
                }
                nav_pro_name.setText(preferenceUtils.getStringFromPreference(PreferenceUtils.User_name, ""));
            } else {
//                        nav_pro_name.setText(R.string.guest);
            }
        } catch (Exception e) {
            e.printStackTrace();
            nav_pro_image.setImageResource(R.drawable.profile_image_default);
            nav_pro_name.setText("Guest");
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btn_home:
                        rl_header.setVisibility(View.VISIBLE);
                        title.setText(getResources().getString(R.string.home));
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.btn_top_pics:
                        rl_header.setVisibility(View.VISIBLE);
                        title.setText(getResources().getString(R.string.top_pics));
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.btn_catogerys:
                        rl_header.setVisibility(View.VISIBLE);
                        title.setText(getResources().getString(R.string.categories));
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.btn_wishlist:
                        rl_header.setVisibility(View.VISIBLE);
                        title.setText(getResources().getString(R.string.whislist));
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.btn_account:
                        rl_header.setVisibility(View.GONE);
                        title.setText(getResources().getString(R.string.account));
                        viewPager.setCurrentItem(4);
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    rl_header.setVisibility(View.VISIBLE);
                    title.setText(getResources().getString(R.string.home));
                    viewPager.setCurrentItem(0);
                } else if (position == 1) {
                    rl_header.setVisibility(View.VISIBLE);
                    title.setText(getResources().getString(R.string.top_pics));
                    viewPager.setCurrentItem(1);
                } else if (position == 2) {
                    rl_header.setVisibility(View.VISIBLE);
                    title.setText(getResources().getString(R.string.categories));
                    viewPager.setCurrentItem(2);
                } else if (position == 3) {
                    rl_header.setVisibility(View.VISIBLE);
                    title.setText(getResources().getString(R.string.whislist));
                    viewPager.setCurrentItem(3);
                } else if (position == 4) {
                    rl_header.setVisibility(View.GONE);
                    title.setText(getResources().getString(R.string.account));
                    viewPager.setCurrentItem(4);
                } else if (position == 5) {
                    rl_header.setVisibility(View.VISIBLE);
                }

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d(getString(R.string.page), getString(R.string.on_page_selected) + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initializeUI() {
        logout = findViewById(R.id.logout);
        help_center = findViewById(R.id.help_center);
        rl_header = (Toolbar) findViewById(R.id.header);
        navigation_icon = (ImageView) findViewById(R.id.back_button);
        title = (TextView) findViewById(R.id.tv_title);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        navigation_icon.setImageResource(R.drawable.menu_icon);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        navi_menu = (RecyclerView) findViewById(R.id.menu_items);
        navmyorder = findViewById(R.id.navmyorder);
        navmyaddress = findViewById(R.id.navmyaddress);
        navmywishlist = findViewById(R.id.navmywishlist);
        headerView = findViewById(R.id.headerView);
        language_change = findViewById(R.id.language_change);
        Currency_changer=findViewById(R.id.Currency_changer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        navi_menu.setLayoutManager(layoutManager);
    }

    private void initializeValues() {
        setSupportActionBar(rl_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        title.setText(getResources().getString(R.string.home));
        navigation_icon.getLayoutParams().height = 70;
        navigation_icon.getLayoutParams().width = 70;
        navigation_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });
        setupViewPager(viewPager);
        logout.setOnClickListener(this);
        help_center.setOnClickListener(this);
        navmyorder.setOnClickListener(this);
        navmyaddress.setOnClickListener(this);
        navmywishlist.setOnClickListener(this);
        headerView.setOnClickListener(this);
        language_change.setOnClickListener(this);
        Currency_changer.setOnClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Home home = new Home();
        TopPicsFragment topPicsFragment = new TopPicsFragment();
        CategoryScreen categoryScreen = new CategoryScreen();
        Whishlist whishlist = new Whishlist();
        MyAccount myAccount = new MyAccount();

        adapter.addFragment(home);
        adapter.addFragment(topPicsFragment);
        adapter.addFragment(categoryScreen);
        adapter.addFragment(whishlist);
        adapter.addFragment(myAccount);

        viewPager.setAdapter(adapter);

        if (getIntent().getExtras() != null && getIntent().getStringExtra("Action") != null && getIntent().getStringExtra("Action").equalsIgnoreCase("reset password")) {
            rl_header.setVisibility(View.GONE);
            title.setText(getResources().getString(R.string.account));
            viewPager.setCurrentItem(4);
            bottomNavigationView.getMenu().getItem(4).setChecked(true);
            prevMenuItem = bottomNavigationView.getMenu().getItem(4);
        } else if (getIntent().getExtras() != null && getIntent().getStringExtra("Option") != null && getIntent().getStringExtra("Option").equalsIgnoreCase("true")) {
            if (getIntent().getStringExtra("Position").equalsIgnoreCase("0")){
                rl_header.setVisibility(View.VISIBLE);
                title.setText(getResources().getString(R.string.home));
                viewPager.setCurrentItem(0);
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(0);
            } else if (getIntent().getStringExtra("Position").equalsIgnoreCase("3")){
                rl_header.setVisibility(View.VISIBLE);
                title.setText(getResources().getString(R.string.whislist));
                viewPager.setCurrentItem(3);
                bottomNavigationView.getMenu().getItem(3).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(3);
            }
        } else {
            rl_header.setVisibility(View.VISIBLE);
            title.setText(getResources().getString(R.string.home));
            viewPager.setCurrentItem(0);
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
            prevMenuItem = bottomNavigationView.getMenu().getItem(0);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.logout:
                preferenceUtils.saveBoolean(PreferenceUtils.LOG_OUT,true);
                preferenceUtils.logOut();
                String languageToLoad = "ar"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                preferenceUtils.setLanguage("ar");
                intent = new Intent(context, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                ((Activity) context).finish();


//                preferenceUtils.setLanguage("ar");
                break;
            case R.id.help_center:
              /*  intent = new Intent(MainActivity.this, OnBoardingScreen.class);
                startActivity(intent);
                finish();*/
                break;
            case R.id.navmyorder:
                intent = new Intent(MainActivity.this, MyOrder.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navmyaddress:
                intent = new Intent(MainActivity.this, MyAddressActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.navmywishlist:
                mDrawerLayout.closeDrawers();
                viewPager.setCurrentItem(3);
                break;

            case R.id.headerView:
                mDrawerLayout.closeDrawers();
                viewPager.setCurrentItem(4);
                break;
            case R.id.language_change:
                showLang();
                break;
            case R.id.Currency_changer:
                showCurrency();
                break;
        }
    }

    public void showLang() {
        final Dialog dialogLanguage = new Dialog(MainActivity.this);
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLanguage.setContentView(R.layout.dailog_select_language);
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
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
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
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
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

    public void showCurrency() {
        final Dialog dialogCurrency = new Dialog(MainActivity.this);
        dialogCurrency.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCurrency.setContentView(R.layout.dailog_select_currency);
        dialogCurrency.setCanceledOnTouchOutside(true);
        dialogCurrency.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialogCurrency.getWindow();
        layoutParams.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);

        final RadioButton rbSAR = (RadioButton) dialogCurrency.findViewById(R.id.rbSAR);
        final RadioButton rbUSD = (RadioButton) dialogCurrency.findViewById(R.id.rbUSD);
        final RadioButton rbKWD = (RadioButton) dialogCurrency.findViewById(R.id.rbKWD);
        final RadioButton rbAED= (RadioButton) dialogCurrency.findViewById(R.id.rbAED);
        Button btnSubmit =  dialogCurrency.findViewById(R.id.btnapply);
        Button btnCancel =  dialogCurrency.findViewById(R.id.btncancel);

        if (strCurrency.equalsIgnoreCase("0")) {
            rbSAR.setChecked(true);

        } else if (strCurrency.equalsIgnoreCase("1")) {
            rbAED.setChecked(true);
        }else if (strCurrency.equalsIgnoreCase("2")) {
            rbKWD.setChecked(true);
        }else if (strCurrency.equalsIgnoreCase("3")) {
            rbUSD.setChecked(true);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbSAR.isChecked()) {
                    languageToLoad = "0"; // your language
                    Locale locale = new Locale(currencyToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                    preferenceUtils.setCurrency("0");

                    dialogCurrency.dismiss();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (rbAED.isChecked()) {
                    languageToLoad = "1"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                    preferenceUtils.setLanguage("1");

                    dialogCurrency.dismiss();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if (rbKWD.isChecked()) {
                    languageToLoad = "2"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                    preferenceUtils.setLanguage("2");

                    dialogCurrency.dismiss();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if (rbUSD.isChecked()) {
                    languageToLoad = "3"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config,
                            getBaseContext().getResources().getDisplayMetrics());
                    preferenceUtils.setLanguage("3");

                    dialogCurrency.dismiss();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCurrency.dismiss();
            }
        });
        dialogCurrency.show();
    }
}