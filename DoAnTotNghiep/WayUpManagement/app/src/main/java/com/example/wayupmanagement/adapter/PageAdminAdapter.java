package com.example.wayupmanagement.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.wayupmanagement.R;
import com.example.wayupmanagement.fragment.CompanyManagerFragment;
import com.example.wayupmanagement.fragment.UserManagerFragment;

public class PageAdminAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public PageAdminAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CompanyManagerFragment();
        } else if (position == 1){
            return new UserManagerFragment();
        }
        return null;
    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.category_company_manager);
            case 1:
                return mContext.getString(R.string.category_user_manager);
            default:
                return null;
        }
    }

}