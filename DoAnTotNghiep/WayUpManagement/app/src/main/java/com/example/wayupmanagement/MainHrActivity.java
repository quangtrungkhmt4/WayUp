package com.example.wayupmanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.wayupmanagement.adapter.PagerAdapter;
import com.example.wayupmanagement.constant.API;
import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.model.AbstractModel;
import com.example.wayupmanagement.model.Hr;
import com.example.wayupmanagement.model.User;
import com.example.wayupmanagement.util.Preferences;
import com.example.wayupmanagement.util.ProcessDialog;
import com.example.wayupmanagement.util.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MainHrActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private PagerAdapter pagerAdapter;
    private RequestQueue requestQueue;
    private Hr currentHr;
    private User currentUser;
    private ProcessDialog processDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hr);

        findId();
        initViews();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarMainHr);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.sliding_tabs);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();
        processDialog = new ProcessDialog(this);
//        processDialog.show();
        currentUser = new Gson().fromJson(Preferences.getData(Key.USER, this), User.class);
//        searchHr(currentUser.getId_user());
        pagerAdapter = new PagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public static final String KEY_PASS_DATA = "KEY_PASS_DATA";
    public<E extends AbstractModel> void switchActivity(Class<?> target, E object){
        Intent intent = new Intent(this, target);
        intent.putExtra(KEY_PASS_DATA, object);
        startActivity(intent);
    }

    private void searchHr(int id_user) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , Preferences.getData(Key.IP, this) + API.SEARCH_HR + id_user
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("code") == 0){
                        String data= response.getJSONObject("data").get("hr").toString();
                        if (data.equals("null")){
                            Toast.makeText(MainHrActivity.this, getString(R.string.errors), Toast.LENGTH_SHORT).show();
                        }else {
                            currentHr = new Gson().fromJson(data, Hr.class);
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
                Toast.makeText(MainHrActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public Hr getCurrentHr() {
        return currentHr;
    }

    public void setCurrentHr(Hr currentHr) {
        this.currentHr = currentHr;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Bạn muốn đăng xuất khỏi hệ thống?")
                        .setCancelable(false)
                        .setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                Preferences.saveData(Key.USER, "", MainHrActivity.this);
                                startActivity(new Intent(MainHrActivity.this, LoginActivity.class));
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
}
