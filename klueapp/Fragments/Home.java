package com.volive.klueapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.rd.PageIndicatorView;
import com.volive.klueapp.Activities.MainActivity;
import com.volive.klueapp.Activities.NewArrivals;
import com.volive.klueapp.Models.Ads_Model;
import com.volive.klueapp.Models.Category_Model;
import com.volive.klueapp.Models.Home_banner_model;
import com.volive.klueapp.Models.NewArrivalModel;
import com.volive.klueapp.R;
import com.volive.klueapp.Utils.API_class;
import com.volive.klueapp.Utils.ConnectivityReceiver;
import com.volive.klueapp.Utils.Constants;
import com.volive.klueapp.Utils.CustomViewPager;
import com.volive.klueapp.Utils.PreferenceUtils;
import com.volive.klueapp.Utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.AddsImageSliderAdapter;
import com.volive.klueapp.adpaters.CatListAdapter;
import com.volive.klueapp.adpaters.NewArrivalAdapter;
import com.volive.klueapp.adpaters.SlidingImage_Adapter;
import com.volive.klueapp.adpaters.nav_menu_item_adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.volive.klueapp.Activities.MainActivity.viewPager;

public class Home extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    public static ArrayList<Category_Model> category_list;
    public static ArrayList<NewArrivalModel> new_arrival_list;
    private static int currentPage = 0;
    private static int currentPage_add = 0;
    CustomViewPager customViewPager, customViewPager1;
    FragmentManager mFragmentManager;
    LinearLayout serach_layout;
    Button btn_viewAll;
    TextView edit_search_items;
    CatListAdapter adapter;
    NewArrivalAdapter arrivalAdapter;
    PageIndicatorView indicator, indicator_adds;
    RecyclerView recyclerView, recyclerView1;
    ArrayList<Ads_Model> ads_array;
    ArrayList<Home_banner_model> images_array;
    LinearLayout all_categories_layout;
    PreferenceUtils preferenceUtils;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home, container, false);

        Home home = new Home();

        preferenceUtils = new PreferenceUtils(getActivity());
        customViewPager = new CustomViewPager(getActivity());

        inializeUI(view);
        home_fragment_service();
        return view;
    }

    private void inializeUI(View view) {
//        indicator=view.findViewById(R.id.indicator);
        edit_search_items = view.findViewById(R.id.search_items);
        mFragmentManager = getActivity().getSupportFragmentManager();
        serach_layout = view.findViewById(R.id.serach_layout);
        serach_layout.clearFocus();
        serach_layout.setActivated(true);
//        doSearch();
        //view All --New Arrivals
        btn_viewAll = view.findViewById(R.id.view_all);
        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewArrivals.class);
                intent.putExtra("arraivals", new_arrival_list);
                getActivity().startActivity(intent);
            }
        });
        all_categories_layout = view.findViewById(R.id.all_categories_layout);
        all_categories_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });
        // RecyclerView Categories
        recyclerView = view.findViewById(R.id.rview_cat_list);
        LinearLayoutManager lManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(lManager);
        // RecyclerView New Arrivals
        recyclerView1 = view.findViewById(R.id.rview_new_arrivals);
        GridLayoutManager gManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView1.setLayoutManager(gManager);
        recyclerView1.setNestedScrollingEnabled(false);
        customViewPager = view.findViewById(R.id.pager);
        customViewPager1 = view.findViewById(R.id.pager_adds);
       /* ViewGroup.LayoutParams params=recyclerView1.getLayoutParams();
        params.height=360;
        recyclerView1.setLayoutParams(params);*/

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int position = findFirstVisibleItemPosition(recyclerView, recyclerView.getLayoutManager());

                if (position == 0) {
                    all_categories_layout.setVisibility(View.VISIBLE);
                } else if (position == 2)
                    all_categories_layout.setVisibility(View.GONE);
            }
        });
    }

    public int findFirstVisibleItemPosition(RecyclerView recyclerV, RecyclerView.LayoutManager layoutM) {
        final View child = findOneVisibleChild(0, layoutM.getChildCount(), false, true, layoutM);
        return child == null ? RecyclerView.NO_POSITION : recyclerV.getChildAdapterPosition(child);
    }

    View findOneVisibleChild(int fromIndex, int toIndex, boolean completelyVisible,
                             boolean acceptPartiallyVisible, RecyclerView.LayoutManager layoutManager) {
        OrientationHelper helper;
        if (layoutManager.canScrollVertically()) {
            helper = OrientationHelper.createVerticalHelper(layoutManager);
        } else {
            helper = OrientationHelper.createHorizontalHelper(layoutManager);
        }

        final int start = helper.getStartAfterPadding();
        final int end = helper.getEndAfterPadding();
        final int next = toIndex > fromIndex ? 1 : -1;
        View partiallyVisible = null;
        for (int i = fromIndex; i != toIndex; i += next) {
            final View child = layoutManager.getChildAt(i);
            final int childStart = helper.getDecoratedStart(child);
            final int childEnd = helper.getDecoratedEnd(child);
            if (childStart < end && childEnd > start) {
                if (completelyVisible) {
                    if (childStart >= start && childEnd <= end) {
                        return child;
                    } else if (acceptPartiallyVisible && partiallyVisible == null) {
                        partiallyVisible = child;
                    }
                } else {
                    return child;
                }
            }
        }
        return partiallyVisible;
    }


    public void home_fragment_service() {
        new_arrival_list = new ArrayList();
        category_list = new ArrayList<>();
        images_array = new ArrayList<>();
        ads_array = new ArrayList<>();

        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;

        callRetrofit = service.HOME_FRAGMENT_SERVICE(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language, ""), preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID, ""));

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(getActivity());
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
                        JSONObject lObj = new JSONObject(searchResponse);
                        int status = lObj.getInt("status");
                        String message = lObj.getString("message");

                        if (status == 1) {
                            JSONObject data_Object = lObj.getJSONObject("data");
                            String base_path = lObj.getString("base_path");
                            JSONArray cat_array = data_Object.getJSONArray("categories");

                            for (int i = 0; i < cat_array.length(); i++) {
                                JSONObject cat_object = cat_array.getJSONObject(i);
                                Category_Model category_model = new Category_Model();
                                category_model.setCat_id(cat_object.getString("cat_id"));
                                category_model.setCname(cat_object.getString("cname"));
                                category_model.setCat_image(base_path + cat_object.getString("cat_image"));

                                category_list.add(category_model);
                            }

                            if (category_list == null && category_list.size() == 0) {
                                Toast.makeText(getActivity(), "No Items", Toast.LENGTH_LONG).show();
                            } else {
                                Log.e("catagery list",category_list.toString());
                                System.out.println("catasd"+ category_list.toString());
                                adapter = new CatListAdapter(getActivity(), category_list);
                                recyclerView.setAdapter(adapter);
                            }
                            nav_menu_item_adapter adapter = new nav_menu_item_adapter(getActivity(), category_list);
                            MainActivity.navi_menu.setAdapter(adapter);
                            MainActivity.navi_menu.setNestedScrollingEnabled(false);

                            JSONArray pdt_array = data_Object.getJSONArray("products");
                            for (int k = 0; k < pdt_array.length(); k++) {
                                JSONObject pdt_object = pdt_array.getJSONObject(k);
                                NewArrivalModel newArrivalModel = new NewArrivalModel();
                                newArrivalModel.setProd_id(pdt_object.getString("prod_id"));
                                newArrivalModel.setPname(pdt_object.getString("pname"));
                                newArrivalModel.setBrand_name(pdt_object.getString("brand_name"));
                                newArrivalModel.setProd_image(base_path + pdt_object.getString("prod_image"));
                                newArrivalModel.setPrice_sar(pdt_object.getString("price_sar"));
                                newArrivalModel.setRegular_price_sar(pdt_object.getString("regular_price_sar"));
                                newArrivalModel.setIs_wish_list(Integer.parseInt(pdt_object.getString("is_wish_list")));
                                new_arrival_list.add(newArrivalModel);
                            }
                            if (new_arrival_list == null && new_arrival_list.size() == 0) {
                                Toast.makeText(getActivity(), "No Items", Toast.LENGTH_LONG).show();
                            } else {
                                arrivalAdapter = new NewArrivalAdapter(getActivity(), new_arrival_list, "Home");
                                recyclerView1.setAdapter(arrivalAdapter);
                            }
                            JSONArray banner_array = data_Object.getJSONArray("banner");

                            for (int j = 0; j < banner_array.length(); j++) {
                                JSONObject banner_object = banner_array.getJSONObject(j);
                                Home_banner_model home_banner_model = new Home_banner_model();
                                home_banner_model.setBanner_id(banner_object.getString("banner_id"));
                                home_banner_model.setBanner_image(base_path + banner_object.getString("banner_image"));
                                home_banner_model.setBanner_link(banner_object.getString("banner_link"));
                                home_banner_model.setStatus(banner_object.getString("status"));
                                images_array.add(home_banner_model);
                            }

                            /*mPager.setAdapter(new ImageSliderAdapter(getActivity(), images_array, true));
                            setIndicator(indicator, mPager);*/
//                            customViewPager.setAdapter(new SlidingImage_Adapter(getActivity(),images_array,true));
                            customViewPager.setAdapter(new SlidingImage_Adapter(getActivity(), images_array));

                            home_banner_Ads();
//                            indicator.setViewPager(mPager);

                            JSONArray ads_Array = data_Object.getJSONArray("ads");

                            for (int l = 0; l < ads_Array.length(); l++) {
                                JSONObject ads_jsonObject = ads_Array.getJSONObject(l);

                                Ads_Model ads_model = new Ads_Model();
                                ads_model.setAds_id(ads_jsonObject.getString("id"));
                                ads_model.setImage(base_path + ads_jsonObject.getString("image"));

                                ads_array.add(ads_model);
                            }
                            customViewPager1.setAdapter(new AddsImageSliderAdapter(getActivity(), ads_array, true));
                            home_Ads();

//                            setIndicator(indicator_adds, mPager_adds);
//                            indicator_adds.setViewPager(customViewPager1);
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
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

    public void home_banner_Ads() {
//        customViewPager.setDurationScroll(2000);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == images_array.size()) {
                    currentPage = 0;
                }
//                                    mPager.setDurationScroll(2000);
                customViewPager.setCurrentItem(currentPage++, true);

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);
    }

    public void home_Ads() {
//        customViewPager1.setDurationScroll(1000);
        final Handler handler = new Handler();
        final Runnable Adds_Update = new Runnable() {
            public void run() {
                if (currentPage_add == ads_array.size()) {
                    currentPage_add = 0;
                }
                customViewPager1.setCurrentItem(currentPage_add++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Adds_Update);
            }
        }, 3000, 3000);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Toast.makeText(getActivity(), isConnected + "", Toast.LENGTH_SHORT).show();
    }

}
