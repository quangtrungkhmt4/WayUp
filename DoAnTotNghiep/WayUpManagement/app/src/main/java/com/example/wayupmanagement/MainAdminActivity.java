package com.example.wayupmanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.example.wayupmanagement.adapter.PageAdminAdapter;
import com.example.wayupmanagement.adapter.PagerAdapter;
import com.example.wayupmanagement.constant.Key;
import com.example.wayupmanagement.model.AbstractModel;
import com.example.wayupmanagement.model.User;
import com.example.wayupmanagement.util.Preferences;
import com.example.wayupmanagement.util.ProcessDialog;
import com.example.wayupmanagement.util.VolleySingleton;
import com.google.gson.Gson;

public class MainAdminActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private PageAdminAdapter pagerAdapter;
    private RequestQueue requestQueue;
    private ProcessDialog processDialog;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        findId();
        initViews();
    }

    private void findId() {
        toolbar = findViewById(R.id.toolbarMainAdmin);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.sliding_tabs);
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        requestQueue = VolleySingleton.getInstance(this).getmRequestQueue();
        processDialog = new ProcessDialog(this);
        currentUser = new Gson().fromJson(Preferences.getData(Key.USER, this), User.class);
        pagerAdapter = new PageAdminAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public static final String KEY_PASS_DATA = "KEY_PASS_DATA";
    public <E extends AbstractModel> void switchActivity(Class<?> target, E object) {
        Intent intent = new Intent(this, target);
        intent.putExtra(KEY_PASS_DATA, object);
        startActivity(intent);
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
                                Preferences.saveData(Key.USER, "", MainAdminActivity.this);
                                startActivity(new Intent(MainAdminActivity.this, LoginActivity.class));
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