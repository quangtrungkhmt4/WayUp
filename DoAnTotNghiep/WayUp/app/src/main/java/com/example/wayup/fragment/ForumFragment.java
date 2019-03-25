package com.example.wayup.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayup.CommentActivity;
import com.example.wayup.MainActivity;
import com.example.wayup.R;
import com.example.wayup.adapter.CompanyGridviewAdapter;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.model.Company;
import com.example.wayup.util.ActivityController;
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

public class ForumFragment extends Fragment implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    private MainActivity mainActivity;
    private GridView gridView;
    private CompanyGridviewAdapter adapter;
    private List<Company> companies = new ArrayList<>();
    private RequestQueue requestQueue;
    private ShimmerFrameLayout shimmerFrameLayout;
    private SearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        findId(view);
        initViews();
        return view;
    }

    private void findId(View view) {
        gridView = view.findViewById(R.id.gvCompany);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayoutForum);
        searchView = view.findViewById(R.id.searchViewCompany);
    }

    private void initViews() {
        mainActivity = (MainActivity) getActivity();
        adapter = new CompanyGridviewAdapter(getContext(), R.layout.item_gv_company, companies);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        gridView.setTextFilterEnabled(true);
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        shimmerFrameLayout.startShimmer();
        searchView.setOnQueryTextListener(this);
        loadData();
    }

    private void loadData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, mainActivity) + API.GET_ALL_COMPANY, null, new Response.Listener<JSONObject>() {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        mainActivity.switchActivity(CommentActivity.class, companies.get(position));

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
}