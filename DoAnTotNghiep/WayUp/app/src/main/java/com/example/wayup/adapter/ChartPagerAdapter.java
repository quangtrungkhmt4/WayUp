package com.example.wayup.adapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.wayup.fragment.CityChartFragment;
import com.example.wayup.fragment.LanguageChartFragment;
import com.example.wayup.fragment.LevelChartFragment;

public class ChartPagerAdapter extends FragmentStatePagerAdapter {

    int numberTabs;

    public ChartPagerAdapter(FragmentManager fm, int numberTabs) {
        super(fm);
        this.numberTabs = numberTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                LanguageChartFragment languageChartFragment = new LanguageChartFragment();
                return languageChartFragment;
            case 1:
                CityChartFragment cityChartFragment = new CityChartFragment();
                return cityChartFragment;
            case 2:
                LevelChartFragment levelChartFragment = new LevelChartFragment();
                return levelChartFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberTabs;
    }
}