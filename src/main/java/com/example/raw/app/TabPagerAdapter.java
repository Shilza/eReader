package com.example.raw.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter{

    private int mNoOfTabs;

    TabPagerAdapter(FragmentManager fm, int numberOfTabs){
        super(fm);
        this.mNoOfTabs = numberOfTabs;
    }


    @Override
    public Fragment getItem(int position){
        switch(position){
            case 0:
                return new TabRecentBooks();
            case 1:
                return new TabLocalBooks();
            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return mNoOfTabs;
    }
}