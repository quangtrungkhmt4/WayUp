package com.example.wayupmanagement.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayupmanagement.MainActivity;
import com.example.wayupmanagement.MainAdminActivity;
import com.example.wayupmanagement.R;
import com.example.wayupmanagement.adapter.CompanyGridviewAdapter;
import com.example.wayupmanagement.constant.API;
import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.model.Company;
import com.example.wayupmanagement.util.Preferences;
import com.example.wayupmanagement.util.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CompanyManagerFragment extends Fragment implements  SearchView.OnQueryTextListener{

    private MainAdminActivity mainActivity;
    private GridView gridView;
    private CompanyGridviewAdapter adapter;
    private List<Company> companies = new ArrayList<>();
    private RequestQueue requestQueue;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_company_manager, container, false);
        findId(view);
        initViews();
        return view;
    }

    private void findId(View view) {
        gridView = view.findViewById(R.id.gvCompany);
        searchView = view.findViewById(R.id.searchViewCompany);
    }

    private void initViews() {
        mainActivity = (MainAdminActivity) getActivity();
        adapter = new CompanyGridviewAdapter(getContext(), R.layout.item_gv_company, companies);
        gridView.setAdapter(adapter);
        gridView.setTextFilterEnabled(true);
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        searchView.setOnQueryTextListener(this);
        loadData();
    }

    private void loadData() {
        companies.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, mainActivity) + API.GET_ALL_COMPANY, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0) {
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("companies");
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Company company = (Company) gson.fromJson(jsonArray.getJSONObject(i).toString(), Company.class);
                            companies.add(company);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onQueryTextSubmit(String string) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String string) {
        if (TextUtils.isEmpty(string)) {
            gridView.clearTextFilter();
        } else {
            gridView.setFilterText(string);
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}