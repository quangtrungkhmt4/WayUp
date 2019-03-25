package com.example.wayup;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wayup.adapter.PageAdapter;
import com.example.wayup.fragment.LoginFragment;


public class LoginRegisterActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        findID();
        initviews();
    }

    private void findID() {
        viewPager = findViewById(R.id.viewPager);
    }

    private void initviews() {
        pageAdapter = new PageAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(pageAdapter);
    }

    private int INDEX_FRAGMENT_LOGIN = 0;
    public void switchFragment(){
        viewPager.setAdapter(pageAdapter);
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}
