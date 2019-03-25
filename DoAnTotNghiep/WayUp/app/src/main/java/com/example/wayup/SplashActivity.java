package com.example.wayup;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.model.User;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.Utils;
import com.example.wayup.util.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class SplashActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private Dialog dialog;
    private final String REGEX_IP = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!Utils.checkPermission(this) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startActivity(new Intent(this, PermissionActivity.class));
            finish();
            return;
        }


        requestQueue = VolleySingleton.getInstance(SplashActivity.this).getmRequestQueue();
        initDialogSetIP();

        // test

//        String ip = "http://192.168.15.100:9999";
//        Preferences.saveData(Key.IP, ip,SplashActivity.this);
//        Preferences.saveData(Key.DOMAIN, "http://192.168.15.100/storage_wayup/",SplashActivity.this);
//        String isRememberPass = Preferences.getData(Key.REMEMBER_PASS,SplashActivity.this);
//        if (isRememberPass.equals(Key.NO)){
//            setTargetActivity(LoginRegisterActivity.class);
//        }else if (isRememberPass.equals(Key.YES)){
//            Gson gson = new Gson();
//            String userString = Preferences.getData(Key.USER,SplashActivity.this);
//            User user = gson.fromJson(userString, User.class);
//            loginRequest(user.getEmail(), user.getPassword(), userString);
//        }else {
//            setTargetActivity(LoginRegisterActivity.class);
//        }


    }

    @SuppressLint("NewApi")
    private void initDialogSetIP(){
        dialog = new Dialog(SplashActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
//        else {
//            Objects.requireNonNull(dialog.getWindow()).setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        }
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_set_ip);
        dialog.setCancelable(false);

        final EditText edtIP = dialog.findViewById(R.id.edtIP);
        final Button btnConfirm = dialog.findViewById(R.id.btnSetIP);

        edtIP.setText("192.168.15.100");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = edtIP.getText().toString();
                if (ip.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(btnConfirm, getString(R.string.insert_info), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                if (!ip.matches(REGEX_IP)){
                    Snackbar snackbar = Snackbar
                            .make(btnConfirm, getString(R.string.ip_error), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }
                Preferences.saveData(Key.DOMAIN, "http://"+ip+"/storage_wayup/",SplashActivity.this);
                ip = "http://" + ip + ":9999";
                Preferences.saveData(Key.IP, ip,SplashActivity.this);

                String isRememberPass = Preferences.getData(Key.REMEMBER_PASS,SplashActivity.this);
                if (isRememberPass.equals(Key.NO)){
                    setTargetActivity(LoginRegisterActivity.class);
                }else if (isRememberPass.equals(Key.YES)){
                    Gson gson = new Gson();
                    String userString = Preferences.getData(Key.USER,SplashActivity.this);
                    User user = gson.fromJson(userString, User.class);
                    loginRequest(user.getEmail(), user.getPassword(), userString);
                }else {
                    setTargetActivity(LoginRegisterActivity.class);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void loginRequest(String email, String pass, final String user){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, SplashActivity.this) +API.LOGIN_USER + "?email="+email + "&pass=" + pass, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("user").toString();
                        if (data.equals("null")){
                            setTargetActivity(LoginRegisterActivity.class);
                        }else {
                            if (data.toString().equals(user)){
                                setTargetActivity(MainActivity.class);
                            }else {
                                setTargetActivity(LoginRegisterActivity.class);
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
                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void setTargetActivity(final Class<?> target){
        CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this, target));
                finish();
            }
        }.start();
    }
}