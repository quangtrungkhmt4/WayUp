package com.example.wayupmanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.wayupmanagement.constant.API;
import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.model.Company;
import com.example.wayupmanagement.model.Vote;
import com.example.wayupmanagement.util.Preferences;
import com.example.wayupmanagement.util.VolleySingleton;
import com.example.wayupmanagement.view.CusBoldTextView;
import com.example.wayupmanagement.view.CusRegularTextView;
import com.example.wayupmanagement.view.CustomListView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class InfoCompanyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imLargeCompany, imLogo;
    private CusBoldTextView tvName;
    private CusRegularTextView tvAddress, tvCountry, tvType, tvMember, tvOT, tvInfo, tvTimeWork, tvContact;
    private Company currentCompany;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_company);

        findId();
        initViews();
        getCompany();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarCompanyInfo);
        imLargeCompany = findViewById(R.id.imgLargeImageCompanyInfo);
        imLogo = findViewById(R.id.imLogoCompanyInfo);
        tvName = findViewById(R.id.tvNameCompanyInfo);
        tvAddress = findViewById(R.id.tvAddressCompanyInfo);
        tvCountry = findViewById(R.id.tvCountryCompanyInfo);
        tvType = findViewById(R.id.tvTypeCompanyInfo);
        tvOT = findViewById(R.id.tvOTCompanyInfo);
        tvInfo = findViewById(R.id.tvCompanyInfo);
        tvTimeWork = findViewById(R.id.tvTimeForWorkCompanyInfo);
        tvMember = findViewById(R.id.tvMemberCompanyInfo);
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

        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();
    }

    private void getCompany() {
        Intent intent = getIntent();
        currentCompany = (Company) intent.getSerializableExtra(MainAdminActivity.KEY_PASS_DATA);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loading);
        String img = currentCompany.getImage().contains("http")?currentCompany.getImage(): Preferences.getData(Key.DOMAIN, this) + currentCompany.getImage();
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.delete:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Bạn muốn xóa công ty này khỏi hệ thống?")
                        .setCancelable(false)
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                deleteCompany(currentCompany.getId_company());
                                finish();
                            }
                        })
                        .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteCompany(int id) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE
                , Preferences.getData(Key.IP, this) + API.GET_ALL_COMPANY + "?id_company=" + id
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        Toast.makeText(InfoCompanyActivity.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InfoCompanyActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
