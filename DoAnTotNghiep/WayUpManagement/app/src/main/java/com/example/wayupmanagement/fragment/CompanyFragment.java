package com.example.wayupmanagement.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.wayupmanagement.EditCompanyActivity;
import com.example.wayupmanagement.LoginActivity;
import com.example.wayupmanagement.MainActivity;
import com.example.wayupmanagement.MainAdminActivity;
import com.example.wayupmanagement.MainHrActivity;
import com.example.wayupmanagement.R;
import com.example.wayupmanagement.constant.API;
import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.model.Company;
import com.example.wayupmanagement.model.Hr;
import com.example.wayupmanagement.model.User;
import com.example.wayupmanagement.util.Preferences;
import com.example.wayupmanagement.util.VolleySingleton;
import com.example.wayupmanagement.view.CusBoldTextView;
import com.example.wayupmanagement.view.CusRegularTextView;
import com.example.wayupmanagement.view.CustomListView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class CompanyFragment extends Fragment implements View.OnClickListener {

    private ImageView imLargeCompany, imLogo;
    private CusBoldTextView tvName;
    private CusRegularTextView tvAddress, tvCountry, tvType, tvMember, tvOT, tvInfo, tvTimeWork, tvContact;
    private Company currentCompany;
    private RequestQueue requestQueue;
    private FloatingActionButton btnEdit;
    private MainHrActivity mainHrActivity;
    private Hr currentHr;
    private User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        findId(view);
        initViews();
        return view;
    }

    private void findId(View view) {
        imLargeCompany = view.findViewById(R.id.imgLargeImageCompanyInfo);
        imLogo = view.findViewById(R.id.imLogoCompanyInfo);
        tvName = view.findViewById(R.id.tvNameCompanyInfo);
        tvAddress = view.findViewById(R.id.tvAddressCompanyInfo);
        tvCountry = view.findViewById(R.id.tvCountryCompanyInfo);
        tvType = view.findViewById(R.id.tvTypeCompanyInfo);
        tvOT = view.findViewById(R.id.tvOTCompanyInfo);
        tvInfo = view.findViewById(R.id.tvCompanyInfo);
        tvTimeWork = view.findViewById(R.id.tvTimeForWorkCompanyInfo);
        tvMember = view.findViewById(R.id.tvMemberCompanyInfo);
        tvContact = view.findViewById(R.id.tvContactCompanyInfo);
        btnEdit = view.findViewById(R.id.btnEditCompany);
    }

    private void initViews() {
        mainHrActivity = (MainHrActivity) getActivity();
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        btnEdit.setOnClickListener(this);

//        if (mainHrActivity.getCurrentHr() != null){
//            currentCompany = mainHrActivity.getCurrentHr().getCompany();
//        }
    }

    private void getCompany() {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.loading);
        String img = currentCompany.getImage().contains("http")?currentCompany.getImage():Preferences.getData(Key.DOMAIN, getContext()) + currentCompany.getImage();
        String thumb = currentCompany.getThumbnail().contains("http")?currentCompany.getThumbnail():Preferences.getData(Key.DOMAIN, getContext()) + currentCompany.getThumbnail();
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEditCompany:
                mainHrActivity.switchActivity(EditCompanyActivity.class, currentCompany);
                break;
        }
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
                            mainHrActivity.setCurrentHr(currentHr);
                            currentCompany = currentHr.getCompany();
                            getCompany();
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
}
