package com.example.wayupmanagement.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayupmanagement.AddJobActivity;
import com.example.wayupmanagement.EditJobActivity;
import com.example.wayupmanagement.MainActivity;
import com.example.wayupmanagement.MainHrActivity;
import com.example.wayupmanagement.R;
import com.example.wayupmanagement.adapter.JobAdapter;
import com.example.wayupmanagement.constant.API;
import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.model.ApplyProfile;
import com.example.wayupmanagement.model.Hr;
import com.example.wayupmanagement.model.Job;
import com.example.wayupmanagement.model.User;
import com.example.wayupmanagement.util.Preferences;
import com.example.wayupmanagement.util.ProcessDialog;
import com.example.wayupmanagement.util.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JobFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView lvJob;
    private JobAdapter jobAdapter;
    private List<Job> jobs = new ArrayList<>();
    private MainHrActivity mainHrActivity;
    private RequestQueue requestQueue;
    private Hr currentHr;
    private User currentUser;
    private FloatingActionButton btnAddJob;
    private ProcessDialog processDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        findId(view);
        initViews();
        return view;
    }

    private void findId(View view) {
        lvJob = view.findViewById(R.id.lvJobSearch);
        TextView tvEmpty = view.findViewById(R.id.tvEmpty);
        lvJob.setEmptyView(tvEmpty);
        btnAddJob = view.findViewById(R.id.btnAddJob);
    }

    private void initViews() {
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        mainHrActivity = (MainHrActivity) getActivity();
        jobAdapter = new JobAdapter(mainHrActivity, R.layout.item_job, jobs);
        processDialog = new ProcessDialog(getContext());
        lvJob.setAdapter(jobAdapter);
        btnAddJob.setOnClickListener(this);
        lvJob.setOnItemClickListener(this);
        lvJob.setOnItemLongClickListener(this);
    }

    private void searchHr(int id_user) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , Preferences.getData(Key.IP, mainHrActivity) + API.SEARCH_HR + id_user
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("hr").toString();
                        if (data.equals("null")){
                            Toast.makeText(mainHrActivity, getString(R.string.errors), Toast.LENGTH_SHORT).show();
                        }else {
                            currentHr = new Gson().fromJson(data, Hr.class);
                            loadJobs();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainHrActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void loadJobs() {
        jobs.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, mainHrActivity)
                + API.GET_ALL_JOB_WITH_COMPANY + mainHrActivity.getCurrentHr().getCompany().getId_company(), null, new Response.Listener<JSONObject>() {
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
//                        if (shimmerFrameLayout.isShimmerStarted()){
//                            CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
//                                @Override
//                                public void onTick(long millisUntilFinished) {
//
//                                }
//
//                                @Override
//                                public void onFinish() {
//                                    shimmerFrameLayout.stopShimmer();
//                                    shimmerFrameLayout.setVisibility(View.GONE);
//                                }
//                            }.start();
//
//                        }
                        if (jobAdapter != null){
                            jobAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainHrActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        currentUser = new Gson().fromJson(Preferences.getData(Key.USER, mainHrActivity), User.class);
        searchHr(currentUser.getId_user());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddJob:
                mainHrActivity.switchActivity(AddJobActivity.class, null);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mainHrActivity.switchActivity(EditJobActivity.class, jobs.get(position));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (jobs.get(position).getLock() == 0){
            final AlertDialog.Builder builder = new AlertDialog.Builder(mainHrActivity);
            builder.setMessage("Bạn muốn đóng tin tuyển dụng này?")
                    .setCancelable(false)
                    .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            Job currentJob = jobs.get(position);
                            currentJob.setLock(1);
                            updateJob(currentJob);
                            processDialog.show();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mainHrActivity);
            builder.setMessage("Bạn muốn mở lại tin tuyển dụng này?")
                    .setCancelable(false)
                    .setPositiveButton("Mở", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            Job currentJob = jobs.get(position);
                            currentJob.setLock(0);
                            updateJob(currentJob);
                            processDialog.show();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }

        return true;
    }

    private void updateJob(Job job) {
        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(job));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT
                , Preferences.getData(Key.IP, mainHrActivity) + API.GET_ALL_JOB_WITH_PAGE
                , param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("job").toString();
                        if (data.equals("null")){
                            Toast.makeText(mainHrActivity, getString(R.string.errors), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {
                            currentUser = new Gson().fromJson(Preferences.getData(Key.USER, mainHrActivity), User.class);
                            searchHr(currentUser.getId_user());
                            Toast.makeText(mainHrActivity, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mainHrActivity, error.toString(), Toast.LENGTH_SHORT).show();
                if (processDialog.isShow()){
                    processDialog.dismiss();
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
