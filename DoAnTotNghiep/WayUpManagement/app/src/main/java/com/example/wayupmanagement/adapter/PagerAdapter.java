package com.example.wayupmanagement.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.wayupmanagement.R;
import com.example.wayupmanagement.fragment.ApplyFragment;
import com.example.wayupmanagement.fragment.CompanyFragment;
import com.example.wayupmanagement.fragment.JobFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public PagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CompanyFragment();
        } else if (position == 1){
            return new JobFragment();
        } else if (position == 2){
            return new ApplyFragment();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.category_company);
            case 1:
                return mContext.getString(R.string.category_job);
            case 2:
                return mContext.getString(R.string.category_apply);
            default:
                return null;
        }
    }

}