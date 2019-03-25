package com.example.wayupmanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayupmanagement.adapter.ImageAdapter;
import com.example.wayupmanagement.constant.API;
import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.model.BitmapImage;
import com.example.wayupmanagement.model.Company;
import com.example.wayupmanagement.model.Hr;
import com.example.wayupmanagement.model.ImageSliding;
import com.example.wayupmanagement.model.Job;
import com.example.wayupmanagement.model.User;
import com.example.wayupmanagement.model.VolleyMultipartRequest;
import com.example.wayupmanagement.util.DateTime;
import com.example.wayupmanagement.util.Preferences;
import com.example.wayupmanagement.util.ProcessDialog;
import com.example.wayupmanagement.util.VolleySingleton;
import com.example.wayupmanagement.view.CustomGridView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AddJobActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private EditText edtTitle, edtInfo, edtAddress, edtEstimatetime, edtSalary, edtSkill, edtFastInfo;
    private Button btnSelectImage, btnRegister, btnCancel;
    private RequestQueue requestQueue;
    private CustomGridView gvImage;
    private Hr currentHr;
    private User currentUser;
    private List<BitmapImage> bitmapImages = new ArrayList<>();
    private List<ImageSliding> imageSlidings = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private ProcessDialog processDialog;
    private Job currentJob;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        findId();
        initViews();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarAddJob);
        edtTitle = findViewById(R.id.edtTitleAddJob);
        edtInfo = findViewById(R.id.edtInfoAddJob);
        edtAddress = findViewById(R.id.edtAddressAddJob);
        edtEstimatetime = findViewById(R.id.edtEstimatetimeAddJob);
        edtSalary = findViewById(R.id.edtSalaryAddJob);
        edtSkill = findViewById(R.id.edtSkillAddJob);
        edtFastInfo = findViewById(R.id.edtFastInfoAddJob);
        btnSelectImage = findViewById(R.id.btnChooseImage);
        gvImage = findViewById(R.id.gvImage);
        btnRegister = findViewById(R.id.btnRegisterJob);
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
        imageAdapter = new ImageAdapter(this, R.layout.item_image, bitmapImages);
        gvImage.setAdapter(imageAdapter);

        btnSelectImage.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void searchHr(int id_user) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , Preferences.getData(Key.IP, this) + API.SEARCH_HR + id_user
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("hr").toString();
                        if (data.equals("null")){
                            Toast.makeText(AddJobActivity.this, getString(R.string.errors), Toast.LENGTH_SHORT).show();
                        }else {
                            currentHr = new Gson().fromJson(data, Hr.class);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddJobActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        currentUser = new Gson().fromJson(Preferences.getData(Key.USER, this), User.class);
        searchHr(currentUser.getId_user());
    }

    private final int REQUEST_PICK_IMAGE = 2;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnChooseImage:
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, REQUEST_PICK_IMAGE);
                break;
            case R.id.btnRegisterJob:
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

                if (bitmapImages.size() == 0){
                    Snackbar snackbar = Snackbar
                            .make(btnCancel, getString(R.string.insert_image), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                int estima = Integer.parseInt(estimatetime);
                int sal = Integer.parseInt(salary);

                Job j = new Job(title, currentHr.getCompany().getThumbnail(), info, address, DateTime.currentDateTime(), estima, 0, sal, skill, fastInfo, currentHr.getCompany());
                registerJob(j);
                processDialog.show();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null){
            Uri imageUri = data.getData();
            try {
                BitmapImage bm = new BitmapImage(MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri));
                bitmapImages.add(bm);
                imageAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void postImage(final ImageSliding image){
        String json = new Gson().toJson(image);
        JSONObject requestBody = null;
        try {
            requestBody = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Preferences.getData(Key.IP, this) + API.GET_ALL_IMAGES_WITH_JOB, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("response").toString();
                        if (data.equals("false")){
                            Snackbar snackbar = Snackbar
                                    .make(edtAddress, getString(R.string.errors), Snackbar.LENGTH_LONG);
                            snackbar.show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {
                            if (count == bitmapImages.size() - 1){
                                if (processDialog.isShow()){
                                    processDialog.dismiss();
                                }
                                Toast.makeText(AddJobActivity.this, getString(R.string.add_job_success), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            count++;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddJobActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                if (processDialog.isShow()){
                    processDialog.dismiss();
                }
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

    private void registerJob(Job job) {
        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(job));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST
                , Preferences.getData(Key.IP, this) + API.GET_ALL_JOB_WITH_PAGE
                , param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("job").toString();
                        if (data.equals("null")){
                            Toast.makeText(AddJobActivity.this, getString(R.string.errors), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {
                            currentJob = new Gson().fromJson(data, Job.class);

                            for (int i=0; i<bitmapImages.size(); i++){
                                uploadImage(bitmapImages.get(i).getBitmap(), ".png");
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
                Toast.makeText(AddJobActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                if (processDialog.isShow()){
                    processDialog.dismiss();
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    public void uploadImage(final Bitmap bitmap, final String type) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,  Preferences.getData(Key.IP, this) + API.UPLOAD_FILE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            String fileNameLogo = obj.getString("fileName");
                            postImage(new ImageSliding(fileNameLogo, currentJob));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar snackbar = Snackbar
                                    .make(edtAddress, getString(R.string.errors), Snackbar.LENGTH_LONG);
                            snackbar.show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddJobActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        if (processDialog.isShow()){
                            processDialog.dismiss();
                        }
                    }
                }) {

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart("img_" + imagename + type, getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        requestQueue.add(volleyMultipartRequest);
    }
}
