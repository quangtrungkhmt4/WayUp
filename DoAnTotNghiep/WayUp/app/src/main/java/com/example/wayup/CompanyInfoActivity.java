package com.example.wayup;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.wayup.adapter.LVJobAdapter;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.model.Company;
import com.example.wayup.model.Job;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.VolleySingleton;
import com.example.wayup.view.CusBoldTextView;
import com.example.wayup.view.CusRegularTextView;
import com.example.wayup.view.CustomListView;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompanyInfoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private Toolbar toolbar;
    private ImageView imLargeCompany, imLogo;
    private Button btnMap, btnComment;
    private CusBoldTextView tvName;
    private CusRegularTextView tvAddress, tvCountry, tvType, tvMember, tvOT, tvInfo, tvTimeWork, tvContact;
    private Company currentCompany;
    private CustomListView lvJob;
    private RequestQueue requestQueue;
    private List<Job> jobs = new ArrayList<>();
    private LVJobAdapter jobAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);

        findId();
        initViews();
        getCompany();

    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarCompanyInfo);
        imLargeCompany = findViewById(R.id.imgLargeImageCompanyInfo);
        imLogo = findViewById(R.id.imLogoCompanyInfo);
//        btnMap = findViewById(R.id.btnCheckMapCompany);
        btnComment = findViewById(R.id.btnCommentCompanyInfo);
        tvName = findViewById(R.id.tvNameCompanyInfo);
        tvAddress = findViewById(R.id.tvAddressCompanyInfo);
        tvCountry = findViewById(R.id.tvCountryCompanyInfo);
        tvType = findViewById(R.id.tvTypeCompanyInfo);
        tvOT = findViewById(R.id.tvOTCompanyInfo);
        tvInfo = findViewById(R.id.tvCompanyInfo);
        tvTimeWork = findViewById(R.id.tvTimeForWorkCompanyInfo);
        tvMember = findViewById(R.id.tvMemberCompanyInfo);
        lvJob = findViewById(R.id.lvJobContainCompanyInfo);
        tvContact = findViewById(R.id.tvContactCompanyInfo);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        }

        jobAdapter = new LVJobAdapter(this, R.layout.item_job, jobs);
        lvJob.setAdapter(jobAdapter);
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();
        lvJob.setOnItemClickListener(this);
        btnComment.setOnClickListener(this);
    }

    private void getCompany() {
        Intent intent = getIntent();
        currentCompany = (Company) intent.getSerializableExtra(MainActivity.KEY_PASS_DATA);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loading);
        String img = currentCompany.getImage().contains("http")?currentCompany.getImage():Preferences.getData(Key.DOMAIN, this) + currentCompany.getImage();
        String thumb = currentCompany.getThumbnail().contains("http")?currentCompany.getThumbnail():Preferences.getData(Key.DOMAIN, this) + currentCompany.getThumbnail();
        Glide.with(this).load(img).apply(requestOptions).into(imLargeCompany);
        Glide.with(this).load(thumb).apply(requestOptions).into(imLogo);

        tvName.setText(currentCompany.getName());
        tvAddress.setText(currentCompany.getAddress());
        tvCountry.setText(currentCompany.getCountry());
        tvType.setText(currentCompany.getType());
        tvTimeWork.setText(currentCompany.getTime_for_work());
        tvMember.setText(currentCompany.getMember());
        tvOT.setText(currentCompany.getOver_time());
        tvInfo.setText(currentCompany.getDescription());
        tvContact.setText(currentCompany.getContact());

        loadJobs();
    }

    private void loadJobs() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, CompanyInfoActivity.this)
                + API.GET_ALL_JOB_WITH_COMPANY + currentCompany.getId_company(), null, new Response.Listener<JSONObject>() {
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
                        jobAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CompanyInfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, JobInfoActivity.class);
        intent.putExtra(MainActivity.KEY_PASS_DATA, jobs.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(MainActivity.KEY_PASS_DATA, currentCompany);
        startActivity(intent);
    }
}
