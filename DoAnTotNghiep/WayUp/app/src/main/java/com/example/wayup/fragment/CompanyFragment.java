package com.example.wayup.fragment;


import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayup.CompanyInfoActivity;
import com.example.wayup.JobInfoActivity;
import com.example.wayup.LoadMore;
import com.example.wayup.MainActivity;
import com.example.wayup.R;
import com.example.wayup.adapter.CompanyAdapter;
import com.example.wayup.adapter.JobAdapter;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.model.Company;
import com.example.wayup.model.Job;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CompanyFragment extends Fragment implements CompanyAdapter.OnClickItemListener {

    private MainActivity mainActivity;
    private RecyclerView recyclerViewCompany;
    private CompanyAdapter companyAdapter;
    List<Company> companies = new ArrayList<>();
    private RequestQueue requestQueue;
    private ShimmerFrameLayout shimmerFrameLayout;
    private int allCompany = 0;
    private int currentPage = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        findId(view);
        initViews();
        return view;
    }

    private void findId(View view) {
        recyclerViewCompany = view.findViewById(R.id.recyclerCompany);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayoutCompany);
    }

    private void initViews() {
        mainActivity = (MainActivity) getActivity();
        recyclerViewCompany.setLayoutManager(new LinearLayoutManager(getContext()));
        companyAdapter = new CompanyAdapter(recyclerViewCompany, getActivity(), companies);
        recyclerViewCompany.setAdapter(companyAdapter);
        companyAdapter.setOnItemClickListener(this);
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        shimmerFrameLayout.startShimmer();
        countAllCompany();

        loadMoreData(currentPage);

        //Set Load more event
        companyAdapter.setLoadMore(new LoadMore() {
            @Override
            public void onLoadMore() {
                if(companies.size() <= allCompany) // Change max size
                {
                    companies.add(null);
                    companyAdapter.notifyItemInserted(companies.size()-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            companies.remove(companies.size()-1);
                            companyAdapter.notifyItemRemoved(companies.size());
                            currentPage++;
                            loadMoreData(currentPage);

                        }
                    },3000); // Time to load
                }else{
                    Toast.makeText(mainActivity, "Load data completed !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loadMoreData(int page){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, mainActivity) + API.GET_ALL_COMPANY_WITH_PAGE + "?page=" + page, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("companies");
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        for (int i=0; i< jsonArray.length(); i++){
                            Company company = (Company) gson.fromJson(jsonArray.getJSONObject(i).toString(), Company.class);
                            companies.add(company);
                        }
                        if (shimmerFrameLayout.isShimmerStarted()){
                            CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    shimmerFrameLayout.stopShimmer();
                                    shimmerFrameLayout.setVisibility(View.GONE);
                                }
                            }.start();

                        }
                        companyAdapter.notifyDataSetChanged();
                        companyAdapter.setLoaded();
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
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        switch (transit) {
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE:
                if (enter) {
                    return AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
                } else {
                    return AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
                }
            case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE:
                if (enter) {
                    return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_close_enter);
                } else {
                    return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_close_exit);
                }
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
            default:
                if (enter) {
                    return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_open_enter);
                } else {
                    return AnimationUtils.loadAnimation(getActivity(), R.anim.activity_open_exit);
                }
        }
    }

    private void countAllCompany(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, mainActivity) + API.GET_COUNT_COMPANY, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONObject data = response.getJSONObject("data");
                        allCompany = data.getInt("countCompany");
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
    public void onItemClick(View view, int position) {
        Company currentCompany = companies.get(position);
        mainActivity.switchActivity(CompanyInfoActivity.class, currentCompany);
    }

}