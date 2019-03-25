package com.example.wayupmanagement;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.util.Preferences;
import com.example.wayupmanagement.util.Utils;
import com.google.gson.Gson;

public class SplashActivity extends AppCompatActivity {

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


        initDialogSetIP();
    }

    @SuppressLint("NewApi")
    private void initDialogSetIP(){
        dialog = new Dialog(SplashActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
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
                dialog.dismiss();
                CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }.start();
            }
        });
        dialog.show();
    }
}
