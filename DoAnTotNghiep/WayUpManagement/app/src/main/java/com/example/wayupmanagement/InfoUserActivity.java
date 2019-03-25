package com.example.wayupmanagement;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.model.ApplyProfile;
import com.example.wayupmanagement.model.User;
import com.example.wayupmanagement.util.Preferences;
import com.example.wayupmanagement.util.ProcessDialog;
import com.example.wayupmanagement.util.VolleySingleton;
import com.example.wayupmanagement.view.CusBoldTextView;
import com.example.wayupmanagement.view.CusRegularTextView;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoUserActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView imAvatar;
    private CusBoldTextView tvName;
    private CusRegularTextView tvEmail, tvPass, tvPhone, tvGender, tvBirth, tvSkype, tvAddress, tvEducation, tvExp, tvHardSkill, tvSoftSkill, tvInfo, tvTarget;
    private RequestQueue requestQueue;
    private ProcessDialog processDialog;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);
        findId();
        initViews();
        loadData();
    }


    private void findId() {
        toolbar = findViewById(R.id.toolbarInfoUser);
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
        processDialog = new ProcessDialog(this);
    }

    private void loadData() {
        Intent intent = getIntent();
        currentUser = (User) intent.getSerializableExtra(MainAdminActivity.KEY_PASS_DATA);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loading);
        String img = currentUser.getImage().contains("http") ? currentUser.getImage() : Preferences.getData(Key.DOMAIN, this) + currentUser.getImage();
        Glide.with(this).load(img).apply(requestOptions).into(imAvatar);

        tvName.setText(currentUser.getName());
        tvEmail.setText(currentUser.getEmail());
        tvPhone.setText(currentUser.getPhone());
        tvGender.setText(currentUser.getGender());
        tvTarget.setText(currentUser.getTarget());
        tvBirth.setText(currentUser.getBirthday());
        tvSkype.setText(currentUser.getSkype());
        tvAddress.setText(currentUser.getAddress());
        tvEducation.setText(currentUser.getEducation());
        tvExp.setText(currentUser.getExperience());
        tvHardSkill.setText(currentUser.getHard_skill());
        tvSoftSkill.setText(currentUser.getSoft_skill());
        tvInfo.setText(currentUser.getInfo());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}