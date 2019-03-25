package com.example.wayupmanagement;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayupmanagement.constant.API;
import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.model.Job;
import com.example.wayupmanagement.util.DateTime;
import com.example.wayupmanagement.util.Preferences;
import com.example.wayupmanagement.util.ProcessDialog;
import com.example.wayupmanagement.util.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class EditJobActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText edtTitle, edtInfo, edtAddress, edtEstimatetime, edtSalary, edtSkill, edtFastInfo;
    private Button btnUpdate, btnCancel;
    private RequestQueue requestQueue;
    private ProcessDialog processDialog;
    private Job currentJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);

        findId();
        initViews();
        loadData();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarEditJob);
        edtTitle = findViewById(R.id.edtTitleEditJob);
        edtInfo = findViewById(R.id.edtInfoEditJob);
        edtAddress = findViewById(R.id.edtAddressEditJob);
        edtEstimatetime = findViewById(R.id.edtEstimatetimeEditJob);
        edtSalary = findViewById(R.id.edtSalaryEditJob);
        edtSkill = findViewById(R.id.edtSkillEditJob);
        edtFastInfo = findViewById(R.id.edtFastInfoEditJob);
        btnUpdate = findViewById(R.id.btnUpdateJob);
        btnCancel = findViewById(R.id.btnCancel);
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

        btnCancel.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    private void loadData() {
        Intent intent = getIntent();
        currentJob = (Job) intent.getSerializableExtra(MainHrActivity.KEY_PASS_DATA);

        edtTitle.setText(currentJob.getTitle());
        edtAddress.setText(currentJob.getAddress());
        edtEstimatetime.setText(String.valueOf(currentJob.getEstimatetime()));
        edtFastInfo.setText(currentJob.getFast_info());
        edtInfo.setText(currentJob.getInformation());
        edtSalary.setText(String.valueOf(currentJob.getSalary()));
        edtSkill.setText(currentJob.getSkills());
    }

    private void updateJob(Job job) {
        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(job));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT
                , Preferences.getData(Key.IP, this) + API.GET_ALL_JOB_WITH_PAGE
                , param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("job").toString();
                        if (data.equals("null")){
                            Toast.makeText(EditJobActivity.this, getString(R.string.errors), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {
                            currentJob = new Gson().fromJson(data, Job.class);
                            Toast.makeText(EditJobActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditJobActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                if (processDialog.isShow()){
                    processDialog.dismiss();
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel:
                finish();
                break;
            case R.id.btnUpdateJob:
                String title = edtTitle.getText().toString();
                String info = edtInfo.getText().toString();
                String address = edtAddress.getText().toString();
                String estimatetime = edtEstimatetime.getText().toString();
                String salary = edtSalary.getText().toString();
                String skill = edtSkill.getText().toString();
                String fastInfo = edtFastInfo.getText().toString();

                if (title.isEmpty() || info.isEmpty() || address.isEmpty() || estimatetime.isEmpty()
                        || salary.isEmpty() || skill.isEmpty() || fastInfo.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(btnCancel, getString(R.string.insert_info), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                currentJob.setTitle(title);
                currentJob.setInformation(info);
                currentJob.setAddress(address);
                currentJob.setEstimatetime(Integer.parseInt(estimatetime));
                currentJob.setSalary(Integer.parseInt(salary));
                currentJob.setSkills(skill);
                currentJob.setFast_info(fastInfo);

                updateJob(currentJob);
                processDialog.show();

                break;
        }
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
