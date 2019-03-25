package com.example.wayup;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wayup.action.ApplyAction;
import com.example.wayup.adapter.SlidingAdapter;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.model.Company;
import com.example.wayup.model.ImageSliding;
import com.example.wayup.model.Job;
import com.example.wayup.util.Database;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.VolleySingleton;
import com.example.wayup.view.CusBoldTextView;
import com.example.wayup.view.CusRegularTextView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JobInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private ImageView imLogo;
    private Button btnSave, btnApply;
    private CusRegularTextView tvAddress, tvSalary, tvSkill, tvInfo;
    private CusBoldTextView tvTitle;
    private SlidingAdapter slidingAdapter;
    private List<ImageSliding> imageSlidings = new ArrayList<>();
    private RequestQueue requestQueue;
    private ShimmerFrameLayout shimmerFrameLayout;
    private Job currentJob;
    private Company currentCompany;
    private Toolbar toolbar;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_info);

        findId();
        initViews();
    }

    private void findId() {
        viewPager = findViewById(R.id.imgPager);
        imLogo = findViewById(R.id.imLogoJobInfo);
        btnSave = findViewById(R.id.btnSaveJob);
        btnApply = findViewById(R.id.btnApplyJobInfo);
        tvAddress = findViewById(R.id.tvAddressJobInfo);
        tvSalary = findViewById(R.id.tvSalaryJobInfo);
        tvSkill = findViewById(R.id.tvSkillJobInfo);
        tvInfo = findViewById(R.id.tvJobInfo);
        tvTitle = findViewById(R.id.tvTitleJobInfo);
        toolbar = findViewById(R.id.toolbarJobInfo);
    }

    private void initViews() {
        slidingAdapter = new SlidingAdapter(this,imageSlidings);
        viewPager.setAdapter(slidingAdapter);
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        }
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle(getString(R.string.job_info));
        database = new Database(this);
        btnApply.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        getJob();
        getImages();
        getCompany();

    }

    private void loadData() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loading);
        String thumbnail = currentJob.getThumbnail().contains("http")?currentJob.getThumbnail(): Preferences.getData(Key.DOMAIN, this) + currentJob.getThumbnail();
        Glide.with(this).load(thumbnail).apply(requestOptions).into(imLogo);
        tvTitle.setText(currentJob.getTitle());
        tvAddress.setText(currentCompany.getAddress());
        tvSalary.setText(currentJob.getSalary()+"$");
        tvSkill.setText(currentJob.getSkills());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvInfo.setText(Html.fromHtml(currentJob.getInformation(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvInfo.setText(Html.fromHtml(currentJob.getInformation()));
        }
    }

    private void getJob() {
        Intent intent = getIntent();
        currentJob = (Job) intent.getSerializableExtra(MainActivity.KEY_PASS_DATA);
    }

    private void getImages() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, this)
                + API.GET_ALL_IMAGES_WITH_JOB + "?id_job=" + currentJob.getId_job(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("images");
                        Gson gson = new Gson();
                        for (int i=0; i< jsonArray.length(); i++){
                            ImageSliding image = (ImageSliding) gson.fromJson(jsonArray.getJSONObject(i).toString(), ImageSliding.class);
                            imageSlidings.add(image);
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
                        slidingAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(JobInfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getCompany() {
        currentCompany = currentJob.getCompany();
        loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnApplyJobInfo:
                ApplyAction applyAction = new ApplyAction(this, currentJob);
                applyAction.showDialog();
                break;
            case R.id.btnSaveJob:
                List<Job> js = database.getJobs();
                boolean check = false;
                for (Job j : js){
                    if (j.getId_job() == currentJob.getId_job()){
                        Toast.makeText(this, getString(R.string.exists_save), Toast.LENGTH_SHORT).show();
                        check = true;
                        break;
                    }
                }

                if (!check){
                    database.insertJob(currentJob);
                    Toast.makeText(this, getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
