package com.example.wayup;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayup.adapter.CommentAdapter;
import com.example.wayup.constant.API;
import com.example.wayup.constant.Key;
import com.example.wayup.model.Company;
import com.example.wayup.model.CompanyComment;
import com.example.wayup.model.User;
import com.example.wayup.util.DateTime;
import com.example.wayup.util.Preferences;
import com.example.wayup.util.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ListView lvComment;
    private List<CompanyComment> comments = new ArrayList<>();
    private EditText edtComment;
    private Button btnSend;
    private Company currentCompany;
    private User currentUser;
    private RequestQueue requestQueue;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        findId();
        getData();
        initViews();
        loadComments();
    }


    private void findId() {
        toolbar = findViewById(R.id.toolbarComment);
        lvComment = findViewById(R.id.lvCommentCompany);
        edtComment = findViewById(R.id.edtComment);
        btnSend = findViewById(R.id.btnSendComment);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        }
        toolbar.setTitleTextColor(0xFFFFFFFF);
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();

        commentAdapter = new CommentAdapter(this, R.layout.item_comment, comments);
        lvComment.setAdapter(commentAdapter);

        btnSend.setOnClickListener(this);
    }

    private void getData() {
        Intent intent = getIntent();
        currentCompany = (Company) intent.getSerializableExtra(MainActivity.KEY_PASS_DATA);
        currentUser = (User) new Gson().fromJson(Preferences.getData(Key.USER, this), User.class);
    }

    private void loadComments() {
        comments.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Preferences.getData(Key.IP, this)
                + API.GET_COMPANY_COMMENT + currentCompany.getId_company(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        JSONArray jsonArray = response.getJSONObject("data").getJSONArray("comments");
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        for (int i=0; i< jsonArray.length(); i++){
                            CompanyComment cmts = (CompanyComment) gson.fromJson(jsonArray.getJSONObject(i).toString(), CompanyComment.class);
                            comments.add(cmts);
                        }
//                        if (shimmerFrameLayout.isShimmerStarted()){
//                            CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
//                                @Override
//                                public void onTick(long millisUntilFinished) {
//
//                                }
//
//                                @Override
//                                public void onFinish() {
//                                    shimmerFrameLayout.stopShimmer();
//                                    shimmerFrameLayout.setVisibility(View.GONE);
//                                }
//                            }.start();
//
//                        }
                        commentAdapter.notifyDataSetChanged();
                        lvComment.setSelection(comments.size() - 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CommentActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void sendComment(String comment){
        CompanyComment companyComment = new CompanyComment(comment, DateTime.currentDateTime(), currentUser, currentCompany);
        Gson gson = new Gson();
        String json = gson.toJson(companyComment);
        JSONObject param = null;
        try {
            param = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Preferences.getData(Key.IP, this) + API.POST_COMPANY_COMMENT, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadComments();
//                try {
//                    if (response.getInt("code") == 0){
//                        JSONObject jsonObject = response.getJSONObject("data").getJSONObject("user");
//                        if (jsonObject != null){
//                            Snackbar snackbar = Snackbar
//                                    .make(getActivity().findViewById(R.id.register), getString(R.string.register_success), Snackbar.LENGTH_LONG);
//                            snackbar.show();
//                            CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
//                                @Override
//                                public void onTick(long millisUntilFinished) {
//
//                                }
//
//                                @Override
//                                public void onFinish() {
//                                    loginRegisterActivity.switchFragment();
//                                }
//                            }.start();
//                        }else {
//                            Snackbar snackbar = Snackbar
//                                    .make(getActivity().findViewById(R.id.register), getString(R.string.register_fail), Snackbar.LENGTH_LONG);
//                            snackbar.show();
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CommentActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public void onClick(View v) {
        String comment = edtComment.getText().toString();
        if (comment.isEmpty()){
            return;
        }
        edtComment.setText("");
        sendComment(comment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
