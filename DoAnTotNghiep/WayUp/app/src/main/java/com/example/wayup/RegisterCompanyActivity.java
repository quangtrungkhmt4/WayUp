package com.example.wayup;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.model.Company;
import com.example.wayup.model.Hr;
import com.example.wayup.model.User;
import com.example.wayup.model.VolleyMultipartRequest;
import com.example.wayup.util.DateTime;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.ProcessDialog;
import com.example.wayup.util.Validator;
import com.example.wayup.util.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RegisterCompanyActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Toolbar toolbar;
    private EditText edtEmailCompany, edtName, edtNameCompany, edtTypeCompany, edtMemberCompany
            , edtCountryCompany, edtTime, edtAddress, edtContact, edtOT, edtTitle, edtDesc;
    private ImageView imLogo, imImage;
    private Button btnSelectLogo, btnSelectImage, btnRegister, btnCancel;
    private RadioButton radExists, radCreate;
    private Spinner spinnerCompany;
    private LinearLayout lnExists, lnCreate;
    private RequestQueue requestQueue;
    private boolean isCreate = false;
    private List<Company> companies = new ArrayList<>();
    private List<String> companiesName = new ArrayList<>();
    private ArrayAdapter<String> companyAdapter;

    private User currentUser;
    private Company currentCompany;
    private Bitmap  bmLogo = null, bmImage = null;
    private String fileNameLogo, fileNameImage;
    private Dialog dialog;
    private ProcessDialog processDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_company);

        findId();
        initViews();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarRegisterPost);
        edtEmailCompany = findViewById(R.id.edtEmailReCompany);
        edtName = findViewById(R.id.edtNameReCompany);
        edtNameCompany = findViewById(R.id.edtNameCompanyReCompany);
        edtTypeCompany = findViewById(R.id.edtTypeCompanyReCompany);
        edtMemberCompany = findViewById(R.id.edtMemberCompanyReCompany);
        edtCountryCompany = findViewById(R.id.edtCountryCompanyReCompany);
        edtTime = findViewById(R.id.edtTimeWorkCompanyReCompany);
        edtAddress = findViewById(R.id.edtAddressCompanyReCompany);
        edtContact = findViewById(R.id.edtContactCompanyReCompany);
        edtOT = findViewById(R.id.edtOTCompanyReCompany);
        edtTitle = findViewById(R.id.edtSloganCompanyReCompany);
        edtDesc = findViewById(R.id.edtDescCompanyReCompany);
        imLogo = findViewById(R.id.imgLogoCompany);
        imImage = findViewById(R.id.imgImageCompany);
        btnSelectLogo = findViewById(R.id.btnChooseLogoCompany);
        btnSelectImage = findViewById(R.id.btnChooseImageCompany);
        btnRegister = findViewById(R.id.btnRegisterCompany);
        btnCancel = findViewById(R.id.btnCancel);
        radExists = findViewById(R.id.radExistsCompany);
        radCreate = findViewById(R.id.radCreateCompany);
        spinnerCompany = findViewById(R.id.spinnerCompany);
        lnExists = findViewById(R.id.lnExists);
        lnCreate = findViewById(R.id.lnCreate);
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
        String userString = Preferences.getData(Key.USER,this);
        currentUser = new Gson().fromJson(userString, User.class);
        companyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, companiesName);
        spinnerCompany.setAdapter(companyAdapter);
        getAllCompanies();
        processDialog = new ProcessDialog(this);
        lnCreate.setVisibility(View.GONE);
        lnExists.setVisibility(View.VISIBLE);
        radCreate.setOnCheckedChangeListener(this);
        radExists.setOnCheckedChangeListener(this);

        btnSelectLogo.setOnClickListener(this);
        btnSelectImage.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.radCreateCompany:
                if (isChecked){
                    lnCreate.setVisibility(View.VISIBLE);
                    lnExists.setVisibility(View.GONE);
                    isCreate = true;
                }
                break;
            case R.id.radExistsCompany:
                if (isChecked){
                    lnCreate.setVisibility(View.GONE);
                    lnExists.setVisibility(View.VISIBLE);
                    isCreate = false;
                }
                break;
        }
    }

    private void getAllCompanies() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, this) + API.GET_ALL_COMPANY, null, new Response.Listener<JSONObject>() {
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
                            companiesName.add(company.getName());
                        }
                        companyAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterCompanyActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private final int REQUEST_PICK_LOGO = 2;
    private final int REQUEST_PICK_IMAGE = 3;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnChooseLogoCompany:
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, REQUEST_PICK_LOGO);
                break;
            case R.id.btnChooseImageCompany:
                Intent intent2 = new Intent(Intent.ACTION_PICK);
                intent2.setType("image/*");
                startActivityForResult(intent2, REQUEST_PICK_IMAGE);
                break;
            case R.id.btnCancel:
                finish();
                break;
            case R.id.btnRegisterCompany:
                String email = edtEmailCompany.getText().toString();
                String name = edtName.getText().toString();
                if (email.isEmpty() || name.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(edtCountryCompany, getString(R.string.insert_info), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }
                if (!Validator.isValidEmailAddress(email)){
                    Snackbar snackbar = Snackbar
                            .make(edtCountryCompany, getString(R.string.email_error), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                processDialog.show();
                if (!isCreate){
                    int index = spinnerCompany.getSelectedItemPosition();
                    registerHr(currentUser, companies.get(index));
                }else {
                    if (bmImage == null || bmLogo == null){
                        Snackbar snackbar = Snackbar
                                .make(edtCountryCompany, getString(R.string.insert_info), Snackbar.LENGTH_LONG);
                        snackbar.show();
                        return;
                    }

                    String companyName = edtNameCompany.getText().toString();
                    String type = edtTypeCompany.getText().toString();
                    String member = edtMemberCompany.getText().toString();
                    String country = edtCountryCompany.getText().toString();
                    String time = edtTime.getText().toString();
                    String address = edtAddress.getText().toString();
                    String contact = edtContact.getText().toString();
                    String ot = edtOT.getText().toString();
                    String title = edtTitle.getText().toString();
                    String desc = edtDesc.getText().toString();

                    if (companyName.isEmpty() || type.isEmpty() || member.isEmpty() || country.isEmpty()
                    || time.isEmpty() || address.isEmpty() || contact.isEmpty() || ot.isEmpty()
                    || title.isEmpty() || desc.isEmpty()){
                        Snackbar snackbar = Snackbar
                                .make(edtCountryCompany, getString(R.string.insert_info), Snackbar.LENGTH_LONG);
                        snackbar.show();
                        return;
                    }

                    uploadLogo(bmLogo, ".png");
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_LOGO && resultCode == RESULT_OK && data != null){
            Uri imageUri = data.getData();
            try {
                bmLogo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imLogo.setImageBitmap(bmLogo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null){
            Uri imageUri = data.getData();
            try {
                bmImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imImage.setImageBitmap(bmImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerHr(User user, Company company) {
        final Hr hr = new Hr(user, company, DateTime.randomCode());
        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(hr));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Preferences.getData(Key.IP, this) + API.POST_HR, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONObject jsonObject = response.getJSONObject("data").getJSONObject("hr");
                        if (jsonObject != null){
                            sendMail(edtEmailCompany.getText().toString(), hr.getCode_confirm());
                        }else {
                            Snackbar snackbar = Snackbar
                                    .make(edtAddress, getString(R.string.register_hr_fail), Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterCompanyActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void updateUser(){
        currentUser.setPermission(1);
        String json = new Gson().toJson(currentUser);
        JSONObject requestBody = null;
        try {
            requestBody = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, Preferences.getData(Key.IP, this) + API.UPDATE_USER, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("user").toString();
                        if (data.equals("null")){
                            Snackbar snackbar = Snackbar
                                    .make(edtAddress, getString(R.string.errors), Snackbar.LENGTH_LONG);
                            snackbar.show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {
                            Snackbar snackbar = Snackbar
                                    .make(edtAddress, getString(R.string.register_hr_success), Snackbar.LENGTH_LONG);
                            snackbar.show();

                            CountDownTimer countDownTimer = new CountDownTimer(3000,1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    startActivity(new Intent(RegisterCompanyActivity.this, LoginRegisterActivity.class));
                                    finish();
                                }
                            }.start();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterCompanyActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void sendMail(String mail, final int code){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, this)
                + API.SEND_CODE + "?mail=" + mail + "&code=" + code, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("response").toString();
                        if (data.equals("false")){
                            Toast.makeText(RegisterCompanyActivity.this, getString(R.string.errors), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                            initDialogConfirmCode(code);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterCompanyActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    @SuppressLint("NewApi")
    private void initDialogConfirmCode(final int code){
        dialog = new Dialog(this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_confirm_code);
        dialog.setCancelable(false);

        final EditText edtCode = dialog.findViewById(R.id.edtConfiemCode);
        final Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCode.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(btnConfirm, getString(R.string.insert_info), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                if (code == Integer.parseInt(edtCode.getText().toString())){
                    updateUser();
                    dialog.dismiss();
                }else {
                    Snackbar snackbar = Snackbar
                            .make(btnConfirm, getString(R.string.code_error), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        dialog.show();
    }

    private void registerCompany(final Company company){
        String json = new Gson().toJson(company);
        JSONObject requestBody = null;
        try {
            requestBody = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Preferences.getData(Key.IP, this) + API.GET_ALL_COMPANY, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("company").toString();
                        if (data.equals("null")){
                            Snackbar snackbar = Snackbar
                                    .make(edtAddress, getString(R.string.errors), Snackbar.LENGTH_LONG);
                            snackbar.show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {
                            searchCompany(company.getName());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterCompanyActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void searchCompany(String name){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, this) + API.SEARCH_COMPANY + "?name=" + name, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("company").toString();
                        if (data.equals("null")){
                            Snackbar snackbar = Snackbar
                                    .make(edtAddress, getString(R.string.errors), Snackbar.LENGTH_LONG);
                            snackbar.show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {
                            registerHr(currentUser, new Gson().fromJson(data, Company.class));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterCompanyActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void uploadLogo(final Bitmap bitmap, final String type) {

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,  Preferences.getData(Key.IP, this) + API.UPLOAD_FILE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            fileNameLogo = obj.getString("fileName");
                            uploadImage(bmImage, ".png");
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
                        Toast.makeText(RegisterCompanyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart(currentUser.getId_user()+ "_" + imagename + type, getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        requestQueue.add(volleyMultipartRequest);
    }

    public void uploadImage(final Bitmap bitmap, final String type) {

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,  Preferences.getData(Key.IP, this) + API.UPLOAD_FILE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            fileNameImage = obj.getString("fileName");

                            String companyName = edtNameCompany.getText().toString();
                            String type = edtTypeCompany.getText().toString();
                            String member = edtMemberCompany.getText().toString();
                            String country = edtCountryCompany.getText().toString();
                            String time = edtTime.getText().toString();
                            String address = edtAddress.getText().toString();
                            String contact = edtContact.getText().toString();
                            String ot = edtOT.getText().toString();
                            String title = edtTitle.getText().toString();
                            String desc = edtDesc.getText().toString();

                            Company company = new Company(companyName, type, member, country, fileNameLogo, fileNameImage, time, address, contact, desc, ot, title);
                            registerCompany(company);

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
                        Toast.makeText(RegisterCompanyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("file", new DataPart(currentUser.getId_user()+ "_" + imagename + type, getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        requestQueue.add(volleyMultipartRequest);
    }
}
