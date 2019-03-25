package com.example.wayupmanagement;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.wayupmanagement.model.User;
import com.example.wayupmanagement.util.Preferences;
import com.example.wayupmanagement.util.Validator;
import com.example.wayupmanagement.util.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPass;
    private Button btnLogin;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findId();
        initViews();
    }

    private void findId() {
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPass = findViewById(R.id.edtPassLogin);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void initViews() {
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String email = edtEmail.getText().toString();
        String pass = edtPass.getText().toString();

        if (email.isEmpty() || pass.isEmpty()){
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

        login(email, pass);
    }

    private void login(String email, String pass) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , Preferences.getData(Key.IP, this) + API.LOGIN_USER + "?email="+email + "&pass=" + pass
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("user").toString();
                        if (data.equals("null")){
                            Snackbar snackbar = Snackbar
                                    .make(edtEmail, getString(R.string.email_pass_wrong), Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }else {
                            final User u = new Gson().fromJson(data, User.class);
                            if (u.getPermission() == 0){
                                Snackbar snackbar = Snackbar
                                        .make(edtEmail, getString(R.string.permission_denied), Snackbar.LENGTH_LONG);
                                snackbar.show();
                                return;
                            }

                            if (u.getLock() == 1){
                                Snackbar snackbar = Snackbar
                                        .make(edtEmail, getString(R.string.locked), Snackbar.LENGTH_LONG);
                                snackbar.show();
                                return;
                            }

                            Snackbar snackbar = Snackbar
                                    .make(edtEmail, getString(R.string.login_success), Snackbar.LENGTH_LONG);
                            snackbar.show();

                            Preferences.saveData(Key.USER, data.toString(), LoginActivity.this);

                            CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    if (u.getPermission() == 1){
                                        startActivity(new Intent(LoginActivity.this, MainHrActivity.class));
                                    }else if (u.getPermission() == 2){
                                        startActivity(new Intent(LoginActivity.this, MainAdminActivity.class));
                                    }
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
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
