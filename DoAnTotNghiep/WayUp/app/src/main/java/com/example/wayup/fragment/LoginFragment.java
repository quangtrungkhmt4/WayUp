package com.example.wayup.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayup.LoginRegisterActivity;
import com.example.wayup.MainActivity;
import com.example.wayup.R;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.model.User;
import com.example.wayup.util.ActivityController;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.ProcessDialog;
import com.example.wayup.util.Validator;
import com.example.wayup.util.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText edtEmail, edtPass;
    private Button btnLogin;
    private CheckBox cbRememberPass;
    private TextView tvForgotPass;
    private boolean isRemember = false;
    private RequestQueue requestQueue;
    private LoginRegisterActivity loginRegisterActivity;
    private ProcessDialog processDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        findId(view);
        initViews();
        return view;
    }

    private void findId(View view) {
        edtEmail = view.findViewById(R.id.edtEmailLogin);
        edtPass = view.findViewById(R.id.edtPassLogin);
        btnLogin = view.findViewById(R.id.btnLogin);
        cbRememberPass = view.findViewById(R.id.cbSavePass);
        tvForgotPass = view.findViewById(R.id.tvForgotPass);
    }

    private void initViews() {
        loginRegisterActivity = (LoginRegisterActivity) getActivity();
        requestQueue = VolleySingleton.getInstance(getContext()).getmRequestQueue();
        btnLogin.setOnClickListener(this);
        processDialog = new ProcessDialog(getContext());
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmail.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(edtEmail, getString(R.string.insert_mail), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                if (!Validator.isValidEmailAddress(edtEmail.getText().toString())){
                    Snackbar snackbar = Snackbar
                            .make(edtEmail, getString(R.string.email_error), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                forgotPass(edtEmail.getText().toString());
                processDialog.show();

            }
        });
    }

    private void forgotPass(final String email) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , Preferences.getData(Key.IP, loginRegisterActivity) + API.SEARCH_USER_WITH_EMAIL + "?email="+email, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("user").toString();
                        if (data.equals("null")){
                            Snackbar snackbar = Snackbar
                                    .make(edtEmail, getString(R.string.email_wrong), Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }else {
                            User u = new Gson().fromJson(data, User.class);
                            if (u.getPermission() != 0){
                                Snackbar snackbar = Snackbar
                                        .make(edtEmail, getString(R.string.permission_denied), Snackbar.LENGTH_LONG);
                                snackbar.show();
                                if (processDialog.isShow()){
                                    processDialog.dismiss();
                                }
                                return;
                            }

                            if (u.getLock() == 1){
                                Snackbar snackbar = Snackbar
                                        .make(edtEmail, getString(R.string.locked), Snackbar.LENGTH_LONG);
                                snackbar.show();
                                if (processDialog.isShow()){
                                    processDialog.dismiss();
                                }
                                return;
                            }


                            sendMailForgotPass(email, u.getPassword());

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
                if (processDialog.isShow()){
                    processDialog.dismiss();
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void sendMailForgotPass(String mail, String pass){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, loginRegisterActivity)
                + API.SEND_PASS + "?mail=" + mail +"&password=" + pass, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("response").toString();
                        if (data.equals("false")){
                            Toast.makeText(loginRegisterActivity, getString(R.string.errors), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {
                            Toast.makeText(loginRegisterActivity, getString(R.string.forgot_pass_done), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
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
                Toast.makeText(loginRegisterActivity, error.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(loginRegisterActivity, getString(R.string.errors), Toast.LENGTH_SHORT).show();
                if (processDialog.isShow()){
                    processDialog.dismiss();
                }
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View v) {
        String email = edtEmail.getText().toString();
        String pass = edtPass.getText().toString();
        if (email.isEmpty()||pass.isEmpty()){
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, loginRegisterActivity) + API.LOGIN_USER + "?email="+email + "&pass=" + pass, null, new Response.Listener<JSONObject>() {
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
                            User u = new Gson().fromJson(data, User.class);
                            if (u.getPermission() != 0){
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

                            if (cbRememberPass.isChecked()){
                                Preferences.saveData(Key.REMEMBER_PASS, Key.YES, getContext());
                            }else {
                                Preferences.saveData(Key.REMEMBER_PASS, Key.NO, getContext());
                            }

                            Preferences.saveData(Key.USER, data.toString(), getContext());

                            CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    ActivityController.switchActivityWithoutData(loginRegisterActivity, MainActivity.class);
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
                Toast.makeText(loginRegisterActivity, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
