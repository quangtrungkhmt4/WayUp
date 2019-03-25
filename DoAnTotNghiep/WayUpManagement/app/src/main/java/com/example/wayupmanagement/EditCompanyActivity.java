package com.example.wayupmanagement;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wayupmanagement.constant.API;
import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.model.Company;
import com.example.wayupmanagement.model.Hr;
import com.example.wayupmanagement.model.User;
import com.example.wayupmanagement.model.VolleyMultipartRequest;
import com.example.wayupmanagement.util.Preferences;
import com.example.wayupmanagement.util.ProcessDialog;
import com.example.wayupmanagement.util.VolleySingleton;
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

public class EditCompanyActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private EditText edtNameCompany, edtTypeCompany, edtMemberCompany
            , edtCountryCompany, edtTime, edtAddress, edtContact, edtOT, edtTitle, edtDesc;
    private ImageView imLogo, imImage;
    private Button btnSelectLogo, btnSelectImage, btnUpdate, btnCancel;
    private RequestQueue requestQueue;

    private User currentUser;
    private Company currentCompany;
    private Bitmap bmLogo = null, bmImage = null;
    private String fileNameLogo, fileNameImage;
    private Dialog dialog;
    private ProcessDialog processDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company);

        findId();
        initViews();
        loadData();
    }


    private void findId() {
        toolbar = findViewById(R.id.toolbarEditCompanyInfo);
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
        btnUpdate = findViewById(R.id.btnUpdateCompany);
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
        btnUpdate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSelectImage.setOnClickListener(this);
        btnSelectLogo.setOnClickListener(this);
    }

    private void loadData() {
        Intent intent = getIntent();
        currentCompany = (Company) intent.getSerializableExtra(MainHrActivity.KEY_PASS_DATA);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loading);
        String img = currentCompany.getImage().contains("http")?currentCompany.getImage(): Preferences.getData(Key.DOMAIN, this) + currentCompany.getImage();
        String thumb = currentCompany.getThumbnail().contains("http")?currentCompany.getThumbnail():Preferences.getData(Key.DOMAIN, this) + currentCompany.getThumbnail();
        Glide.with(this).load(img).apply(requestOptions).into(imImage);
        Glide.with(this).load(thumb).apply(requestOptions).into(imLogo);

        edtNameCompany.setText(currentCompany.getName());
        edtAddress.setText(currentCompany.getAddress());
        edtCountryCompany.setText(currentCompany.getCountry());
        edtTypeCompany.setText(currentCompany.getType());
        edtTime.setText(currentCompany.getTime_for_work());
        edtMemberCompany.setText(currentCompany.getMember());
        edtOT.setText(currentCompany.getOver_time());
        edtTitle.setText(currentCompany.getTitle());
        edtDesc.setText(currentCompany.getDescription());
        edtContact.setText(currentCompany.getContact());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private final int REQUEST_PICK_LOGO = 2;
    private final int REQUEST_PICK_IMAGE = 3;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUpdateCompany:

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

                currentCompany.setName(companyName);
                currentCompany.setType(type);
                currentCompany.setMember(member);
                currentCompany.setCountry(country);
                currentCompany.setTime_for_work(time);
                currentCompany.setAddress(address);
                currentCompany.setContact(contact);
                currentCompany.setDescription(desc);
                currentCompany.setOver_time(ot);
                currentCompany.setTitle(title);
                if (bmImage != null){
                    currentCompany.setImage(fileNameImage);
                }
                if (bmLogo != null){
                    currentCompany.setThumbnail(fileNameLogo);
                }

                updateCompany(currentCompany);
                processDialog.show();
                break;
            case R.id.btnCancel:
                finish();
                break;
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
                uploadLogo(bmLogo,".png");
                processDialog.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null){
            Uri imageUri = data.getData();
            try {
                bmImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imImage.setImageBitmap(bmImage);
                uploadImage(bmImage, ",png");
                processDialog.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void uploadLogo(final Bitmap bitmap, final String type) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,  Preferences.getData(Key.IP, this) + API.UPLOAD_FILE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            fileNameLogo = obj.getString("fileName");
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
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
                        Toast.makeText(EditCompanyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void uploadImage(final Bitmap bitmap, final String type) {

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,  Preferences.getData(Key.IP, this) + API.UPLOAD_FILE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            fileNameImage = obj.getString("fileName");
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
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
                        Toast.makeText(EditCompanyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateCompany(Company company) {
        JSONObject param = null;
        try {
            param = new JSONObject(new Gson().toJson(company));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT
                , Preferences.getData(Key.IP, this) + API.GET_ALL_COMPANY
                , param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("company").toString();
                        if (data.equals("null")){
                            Toast.makeText(EditCompanyActivity.this, getString(R.string.errors), Toast.LENGTH_SHORT).show();
                        }else {
                            currentCompany = new Gson().fromJson(data, Company.class);
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                            Toast.makeText(EditCompanyActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditCompanyActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                if (processDialog.isShow()){
                    processDialog.dismiss();
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
