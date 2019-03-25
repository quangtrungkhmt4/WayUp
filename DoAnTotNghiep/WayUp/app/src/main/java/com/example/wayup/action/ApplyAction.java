package com.example.wayup.action;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayup.R;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.model.ApplyProfile;
import com.example.wayup.model.Job;
import com.example.wayup.model.User;
import com.example.wayup.util.DateTime;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.ProcessDialog;
import com.example.wayup.util.Validator;
import com.example.wayup.util.VolleySingleton;
import com.example.wayup.view.CusBoldTextView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApplyAction {

    private Context context;
    private RequestQueue requestQueue;
    private User currentUser;
    private Dialog dialog;
    private Job currentJob;
    private ProcessDialog processDialog;
    private String[] cvs = new String[5];
    ArrayAdapter<String> adapter = null;

    public ApplyAction(Context context, Job currentJob) {
        this.context = context;
        this.currentJob = currentJob;
        requestQueue = VolleySingleton.getInstance(context).getmRequestQueue();

        Gson gson = new Gson();
        String userString = Preferences.getData(Key.USER,context);
        currentUser = gson.fromJson(userString, User.class);
        processDialog = new ProcessDialog(context);

    }

    public void showDialog(){
        getProfile(currentUser);
        dialog = new Dialog(context,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_apply);
        dialog.setCancelable(true);

        CusBoldTextView tvTitle = dialog.findViewById(R.id.tvTitleJobDialogApply);
        final EditText edtName  = dialog.findViewById(R.id.edtNameDialogApply);
        final EditText edtMail = dialog.findViewById(R.id.edtMailDialogApply);
        final Spinner spinnerCV = dialog.findViewById(R.id.spinnerCV);
        final Button btnApply = dialog.findViewById(R.id.btnSenCVDialogApply);
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, cvs);
        spinnerCV.setAdapter(adapter);

        tvTitle.setText(currentJob.getTitle());

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String mail = edtMail.getText().toString();

                if (name.isEmpty() || mail.isEmpty()){
                    Snackbar snackbar = (Snackbar) Snackbar
                            .make(btnApply, context.getString(R.string.insert_info), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                if (!Validator.isValidEmailAddress(mail)){
                    Snackbar snackbar = (Snackbar) Snackbar
                            .make(btnApply, context.getString(R.string.email_error), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                if (spinnerCV.getSelectedItem() == null){
                    Snackbar snackbar = (Snackbar) Snackbar
                            .make(btnApply, context.getString(R.string.insert_cv), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                searchApplyVer2(currentUser, currentJob, mail, name, spinnerCV.getSelectedItem().toString());
                processDialog.show();
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void saveApply(final ApplyProfile applyProfile){
        String json = new Gson().toJson(applyProfile);
        JSONObject param = null;
        try {
            param = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Preferences.getData(Key.IP, context)
                + API.POST_APPLY , param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("applyProfile").toString();
                        if (data.equals("null")){
                            Toast.makeText(context, context.getString(R.string.apply_fail), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {
                            Toast.makeText(context, context.getString(R.string.apply_success), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                            sendMail(applyProfile.getEmail());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void sendMail(String mail){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, context)
                + API.SEND_MAIL + "?mail=" + mail , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
//                        String data= response.getJSONObject("data").get("response").toString();
//                        if (data.equals("false")){
//                            Toast.makeText(context, context.getString(R.string.apply_fail), Toast.LENGTH_SHORT).show();
//                        }else {
//
//                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private void searchApplyVer2(final User user, final Job job, final String email, final String name, final String profile){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, context)
                + API.GET_APPLY_WITH_USER + user.getId_user(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray data= response.getJSONObject("data").getJSONArray("applyProfiles");
                        int count = 0;
                        for (int i=0; i<data.length(); i++){
                            ApplyProfile ap = new Gson().fromJson(data.getJSONObject(i).toString(), ApplyProfile.class);
                            if (ap.getStatus() == 0){
                                count++;
                            }
                        }

                        if (count == 5){
                            Toast.makeText(context, context.getString(R.string.max_apply), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {
                            boolean isExists = false;
                            for (int i=0; i<data.length(); i++){
                                ApplyProfile ap = new Gson().fromJson(data.getJSONObject(i).toString(), ApplyProfile.class);
                                if (ap.getJob().getTitle().equalsIgnoreCase(job.getTitle()) && ap.getJob().getThumbnail().equalsIgnoreCase(job.getThumbnail())){
                                    isExists = true;
                                    break;
                                }
                            }
                            if (isExists){
                                Toast.makeText(context, context.getString(R.string.exists_apply), Toast.LENGTH_SHORT).show();
                                if (processDialog.isShow()){
                                    processDialog.dismiss();
                                }
                            }else {
                                ApplyProfile applyProfile = new ApplyProfile(email, name, DateTime.currentDateTime(), 0, 0, user, job, profile);
                                saveApply(applyProfile);
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
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void searchApply(final User user, final Job job, final String email, final String name, final String profile){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, context)
                + API.SEARCH_APPLY + user.getId_user() + "/" + job.getId_job(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("response").toString();
                        if (data.equalsIgnoreCase("true")){
                            Toast.makeText(context, context.getString(R.string.exists_apply), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {

                            ApplyProfile applyProfile = new ApplyProfile(email, name, DateTime.currentDateTime(), 0, 0, user, job, profile);
                            saveApply(applyProfile);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getProfile(final User user){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, context)
                + API.GET_ALL_PROFILE + user.getId_user(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray data= response.getJSONObject("data").getJSONArray("profiles");
                        if (data.length() == 0){
                            Toast.makeText(context, context.getString(R.string.insert_cv), Toast.LENGTH_SHORT).show();
                            if (processDialog.isShow()){
                                processDialog.dismiss();
                            }
                        }else {
                            for (int i=0; i< data.length(); i++){
                                cvs[i] = data.getJSONObject(i).get("url").toString();
                            }
                            if (adapter != null){
                                adapter.notifyDataSetChanged();
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
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
