package com.example.wayupmanagement;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.example.wayupmanagement.adapter.VoteAdapter;
import com.example.wayupmanagement.constant.API;
import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.model.ApplyProfile;
import com.example.wayupmanagement.model.Job;
import com.example.wayupmanagement.model.User;
import com.example.wayupmanagement.model.Vote;
import com.example.wayupmanagement.util.Preferences;
import com.example.wayupmanagement.util.ProcessDialog;
import com.example.wayupmanagement.util.VolleySingleton;
import com.example.wayupmanagement.view.CusBoldTextView;
import com.example.wayupmanagement.view.CusRegularTextView;
import com.example.wayupmanagement.view.CustomListView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoApplyActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private CircleImageView imAvatar;
    private CusBoldTextView tvName;
    private CusRegularTextView tvEmail, tvPass, tvPhone, tvGender, tvBirth, tvSkype
            , tvAddress, tvEducation, tvExp, tvHardSkill, tvSoftSkill, tvInfo, tvTarget;
    private RequestQueue requestQueue;
    private ProcessDialog processDialog;
    private ApplyProfile currentApply;
    private Button btnDownload;
    private CustomListView lvVote;
    private VoteAdapter voteAdapter;
    private List<Vote> votes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_apply);

        findId();
        initViews();
        loadData();
    }


    private void findId() {
        toolbar = findViewById(R.id.toolbarInfoApply);
        imAvatar = findViewById(R.id.imgAvatarInfo);
        tvName = findViewById(R.id.tvNameProfile);
        tvEmail = findViewById(R.id.tvMailProfile);
        tvPhone = findViewById(R.id.tvPhoneProfile);
        tvGender = findViewById(R.id.tvGenderProfile);
        tvBirth = findViewById(R.id.tvBirthdayProfile);
        tvSkype = findViewById(R.id.tvSkypeProfile);
        tvAddress = findViewById(R.id.tvAddressProfile);
        tvEducation = findViewById(R.id.tvEducationProfile);
        tvExp = findViewById(R.id.tvExpProfile);
        tvHardSkill = findViewById(R.id.tvHardSkillProfile);
        tvSoftSkill = findViewById(R.id.tvSoftSkillProfile);
        tvInfo = findViewById(R.id.tvInfoProfile);
        tvTarget = findViewById(R.id.tvTargetProfile);
        btnDownload = findViewById(R.id.btnDownloadCV);
        lvVote = findViewById(R.id.lvVote);
    }

    private void initViews() {
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        }
        voteAdapter = new VoteAdapter(this, R.layout.item_vote, votes);
        lvVote.setAdapter(voteAdapter);
        processDialog = new ProcessDialog(this);
        btnDownload.setOnClickListener(this);
    }

    private void loadData() {
        Intent intent = getIntent();
        currentApply = (ApplyProfile) intent.getSerializableExtra(MainHrActivity.KEY_PASS_DATA);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loading);
        String img = currentApply.getUser().getImage().contains("http")?currentApply.getUser().getImage(): Preferences.getData(Key.DOMAIN, this) + currentApply.getUser().getImage();
        Glide.with(this).load(img).apply(requestOptions).into(imAvatar);

        tvName.setText(currentApply.getUser().getName());
        tvEmail.setText(currentApply.getUser().getEmail());
        tvPhone.setText(currentApply.getUser().getPhone());
        tvGender.setText(currentApply.getUser().getGender());
        tvTarget.setText(currentApply.getUser().getTarget());
        tvBirth.setText(currentApply.getUser().getBirthday());
        tvSkype.setText(currentApply.getUser().getSkype());
        tvAddress.setText(currentApply.getUser().getAddress());
        tvEducation.setText(currentApply.getUser().getEducation());
        tvExp.setText(currentApply.getUser().getExperience());
        tvHardSkill.setText(currentApply.getUser().getHard_skill());
        tvSoftSkill.setText(currentApply.getUser().getSoft_skill());
        tvInfo.setText(currentApply.getUser().getInfo());
        getVote();
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        DownloadManager mgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(Preferences.getData(Key.DOMAIN, this) + currentApply.getProfile());

        Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .mkdirs();

        long lastDownload = mgr.enqueue(new DownloadManager.Request(uri)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                        DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        currentApply.getUser().getName() + "_" + currentApply.getCreated_at() + ".docx"));

        Snackbar snackbar = Snackbar
                .make(btnDownload, getString(R.string.download_success), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void getVote() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , Preferences.getData(Key.IP, this) + API.SEARCH_VOTE_WITH_USER + currentApply.getUser().getId_user()
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray data= response.getJSONObject("data").getJSONArray("votes");
                        if (data == null){
                            Toast.makeText(InfoApplyActivity.this, getString(R.string.errors), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {
                            for (int i=0; i<data.length(); i++){
                                votes.add(new Gson().fromJson(data.getJSONObject(i).toString(), Vote.class));
                            }
                            if (voteAdapter != null){
                                voteAdapter.notifyDataSetChanged();
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
                Toast.makeText(InfoApplyActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                if (processDialog.isShow()){
                    processDialog.dismiss();
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
