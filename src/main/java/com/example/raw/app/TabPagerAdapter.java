package com.example.raw.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public TabPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.mNoOfTabs = numberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                TabRecentBooks tabRecentBooks = new TabRecentBooks();
                return tabRecentBooks;
            case 1:
                TabLocalBooks tabLocalBooks = new TabLocalBooks();
                return  tabLocalBooks;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}