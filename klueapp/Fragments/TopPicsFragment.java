package com.volive.klueapp.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.Models.NewArrivalModel;
import com.volive.klueapp.R;
import com.volive.klueapp.Utils.API_class;
import com.volive.klueapp.Utils.Constants;
import com.volive.klueapp.Utils.PreferenceUtils;
import com.volive.klueapp.Utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.CategoryAdapter;
import com.volive.klueapp.adpaters.NewArrivalAdapter;
import com.volive.klueapp.adpaters.TopPics_Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TopPicsFragment extends Fragment {


    RecyclerView recyclerView;
    GridLayoutManager manager;
    public static ArrayList<NewArrivalModel> top_pics_list;
    PreferenceUtils preferenceUtils;
    static TopPics_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_top_pics,container,false);

        recyclerView=view.findViewById(R.id.rview_top_pics);
        preferenceUtils=new PreferenceUtils(getActivity());
        manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        hot_pics_service();

        return  view;
    }

    public void hot_pics_service(){
        top_pics_list=new ArrayList<>();
        final API_class service = Retrofit_funtion_class.getClient().create(API_class.class);
        Call<JsonElement> callRetrofit = null;
        callRetrofit = service.HOT_PICS_LIST(Constants.API_KEY, preferenceUtils.getStringFromPreference(PreferenceUtils.Language,""),preferenceUtils.getStringFromPreference(PreferenceUtils.USER_ID,""));

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
                if (response.isSuccessful()){
                    String searchResponse = response.body().toString();
                    try {
                        JSONObject responceObject = new JSONObject(searchResponse);
                        int status = responceObject.getInt("status");
                        String base_path = responceObject.getString("base_path");
                        if (status==1){
                            JSONArray array_topPics=responceObject.getJSONArray("data");
                            for (int i = 0; i <array_topPics.length(); i++){
                                JSONObject jsonObject=array_topPics.getJSONObject(i);
                                NewArrivalModel newArrivalModel = new NewArrivalModel();
                                newArrivalModel.setProd_id(jsonObject.getString("prod_id"));
                                newArrivalModel.setPname(jsonObject.getString("pname"));
                                newArrivalModel.setBrand_name(jsonObject.getString("brand_name"));
                                newArrivalModel.setProd_image(base_path + jsonObject.getString("prod_image"));
                                newArrivalModel.setPrice_sar(jsonObject.getString("price_sar"));
                                newArrivalModel.setRegular_price_sar(jsonObject.getString("regular_price_sar"));
                                newArrivalModel.setIs_wish_list(Integer.parseInt(jsonObject.getString("is_wish_list")));
                                top_pics_list.add(newArrivalModel);
                            }
                            setUprecyclerView();

                        }
                    }catch (Exception e){
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
    private void setUprecyclerView() {
        if (top_pics_list==null &&top_pics_list.size() == 0){
            Toast.makeText(getActivity(), "No Items", Toast.LENGTH_LONG).show();
        } else {
            adapter=new TopPics_Adapter(getActivity(),top_pics_list,"Top Pics");
            recyclerView.setAdapter(adapter);
        }
    }
}
