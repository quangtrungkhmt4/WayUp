package com.example.wayup.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayup.JobInfoActivity;
import com.example.wayup.LoadMore;
import com.example.wayup.MainActivity;
import com.example.wayup.R;
import com.example.wayup.action.ApplyAction;
import com.example.wayup.adapter.JobAdapter;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.model.Job;
import com.example.wayup.model.User;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JobFragment extends Fragment implements JobAdapter.OnClickItemListener {

    private MainActivity mainActivity;
    private RecyclerView recyclerViewJob;
    private JobAdapter jobAdapter;
    private List<Job> jobs = new ArrayList<>();
    private RequestQueue requestQueue;
    private int allJob = 0;
    private int currentPage = 1;
    private ShimmerFrameLayout shimmerFrameLayout;
    private User currentUser;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        findId(view);
        initviews();
        return view;
    }


    private void findId(View view) {
        recyclerViewJob = view.findViewById(R.id.recyclerJob);
        shimmerFrameLayout = view.findViewById(R.id.shimmerFrameLayout);
    }

    private void initviews() {
        mainActivity = (MainActivity) getActivity();
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        currentUser = new Gson().fromJson(Preferences.getData(Key.USER, getContext()), User.class);
        recyclerViewJob.setLayoutManager(new LinearLayoutManager(getContext()));
        jobAdapter = new JobAdapter(recyclerViewJob, getActivity(), jobs);
        recyclerViewJob.setAdapter(jobAdapter);
        jobAdapter.setOnItemClickListener(this);
        shimmerFrameLayout.startShimmer();
        countAllJob();
        loadMoreData(currentPage);

        //Set Load more event
        jobAdapter.setLoadMore(new LoadMore() {
            @Override
            public void onLoadMore() {
                if(jobs.size() <= allJob) // Change max size
                {
                    jobs.add(null);
                    jobAdapter.notifyItemInserted(jobs.size()-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            jobs.remove(jobs.size()-1);
                            jobAdapter.notifyItemRemoved(jobs.size());
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, mainActivity) + API.GET_ALL_JOB_WITH_PAGE + "?page=" + page, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("jobs");
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        for (int i=0; i< jsonArray.length(); i++){
                            Job job = (Job) gson.fromJson(jsonArray.getJSONObject(i).toString(), Job.class);
                            jobs.add(job);
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
                        jobAdapter.notifyDataSetChanged();
                        jobAdapter.setLoaded();
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

    private void countAllJob(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, mainActivity) + API.GET_ALL_JOB, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONObject data = response.getJSONObject("data");
                        allJob = data.getInt("countJob");
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
    public void onItemClick(View view, int position) {
        Job currentJob = jobs.get(position);
        mainActivity.switchActivity(JobInfoActivity.class, currentJob);
    }

    @Override
    public void onButtonClick(View view, int position) {
        ApplyAction applyAction = new ApplyAction(getContext(), jobs.get(position));
        applyAction.showDialog();
    }
}