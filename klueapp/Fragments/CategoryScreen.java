package com.volive.klueapp.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.volive.klueapp.Activities.MainActivity;
import com.volive.klueapp.Models.OccationsModel;
import com.volive.klueapp.R;
import com.volive.klueapp.Utils.API_class;
import com.volive.klueapp.Utils.Constants;
import com.volive.klueapp.Utils.Retrofit_funtion_class;
import com.volive.klueapp.adpaters.CategoryAdapter;
import com.volive.klueapp.adpaters.OccationsAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryScreen extends Fragment {

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categoryscreen, container, false);

        initializeUI(view);
        setUprecyclerView();
        return view;
    }

    private void initializeUI(View view) {
        recyclerView = view.findViewById(R.id.rview_categories);
        LinearLayoutManager lManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lManager);
    }

    public void setUprecyclerView() {
        if (Home.category_list != null &&Home.category_list.size() == 0){
            Toast.makeText(getActivity(), "No Items", Toast.LENGTH_LONG).show();
        } else {
            CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), Home.category_list);
            recyclerView.setAdapter(categoryAdapter);
        }
    }
}