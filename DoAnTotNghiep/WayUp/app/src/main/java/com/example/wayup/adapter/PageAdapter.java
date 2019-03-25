package com.example.wayup.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.wayup.fragment.LoginFragment;
import com.example.wayup.fragment.RegisterFragment;

public class PageAdapter extends FragmentStatePagerAdapter {

    int numberTabs;

    public PageAdapter(FragmentManager fm, int numberTabs) {
        super(fm);
        this.numberTabs = numberTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                LoginFragment loginFragment = new LoginFragment();
                return loginFragment;
            case 1:
                RegisterFragment registerFragment = new RegisterFragment();
                return registerFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberTabs;
    }
}
