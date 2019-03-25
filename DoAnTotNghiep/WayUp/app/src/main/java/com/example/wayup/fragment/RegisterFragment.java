package com.example.wayup.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayup.LoginRegisterActivity;
import com.example.wayup.R;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.model.Job;
import com.example.wayup.util.DateTime;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.Validator;
import com.example.wayup.util.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private EditText edtEmail, edtPass, edtName;
    private Button btnRegister;
    private LoginRegisterActivity loginRegisterActivity;
    private RequestQueue requestQueue;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        findId(rootView);
        initViews();
        return rootView;
    }

    private void findId(View view) {
        edtEmail = view.findViewById(R.id.edtEmailRegister);
        edtPass = view.findViewById(R.id.edtPassRegister);
        edtName = view.findViewById(R.id.edtNameRegister);
        btnRegister = view.findViewById(R.id.btnRegister);
    }

    private void initViews() {
        loginRegisterActivity = (LoginRegisterActivity) getActivity();
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                String email = edtEmail.getText().toString();
                String pass = edtPass.getText().toString();
                String name = edtName.getText().toString();
                if (email.isEmpty()||pass.isEmpty()||name.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(edtEmail, getString(R.string.insert_info), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                if (!Validator.isValidEmailAddress(email)){
                    Snackbar snackbar = Snackbar
                            .make(edtEmail, getString(R.string.email_error), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                if (pass.length() <= 6){
                    Snackbar snackbar = Snackbar
                            .make(edtEmail, getString(R.string.pass_error), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                if (name.length() <= 6){
                    Snackbar snackbar = Snackbar
                            .make(edtEmail, getString(R.string.name_error), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                isExistsEmail(email, pass, name);

                break;
        }
    }

    private void isExistsEmail(final String email, final String pass, final String name){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, loginRegisterActivity) + API.IS_EXISTS_EMAIL + email, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONObject data= response.getJSONObject("data");
                        boolean isExists = data.getBoolean("existsEmail");
                        if (isExists){
                            Snackbar snackbar = Snackbar
                                    .make(edtEmail, getString(R.string.exists_email), Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }else {
                            registerUser(email, pass, name);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(loginRegisterActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void registerUser(String email, String pass, String name) {
        JSONObject param = new JSONObject();
        try {
            param.put("email", email);
            param.put("password", pass);
            param.put("name", name);
            param.put("phone", "null");
            param.put("image", "avatar_default.png");
            param.put("gender", "null");
            param.put("created_at", DateTime.currentDateTime());
            param.put("permission", 0);
            param.put("lock", 0);
            param.put("target", "null");
            param.put("birthday", "null");
            param.put("skype", "null");
            param.put("address", "null");
            param.put("education", "null");
            param.put("experience", "null");
            param.put("hard_skill", "null");
            param.put("soft_skill", "null");
            param.put("info", "null");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Preferences.getData(Key.IP, loginRegisterActivity) + API.REGISTER_USER, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONObject jsonObject = response.getJSONObject("data").getJSONObject("user");
                        if (jsonObject != null){
                            Snackbar snackbar = Snackbar
                                    .make(getActivity().findViewById(R.id.register), getString(R.string.register_success), Snackbar.LENGTH_LONG);
                            snackbar.show();
                            CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    loginRegisterActivity.switchFragment();
                                }
                            }.start();
                        }else {
                            Snackbar snackbar = Snackbar
                                    .make(getActivity().findViewById(R.id.register), getString(R.string.register_fail), Snackbar.LENGTH_LONG);
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
                Toast.makeText(loginRegisterActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
