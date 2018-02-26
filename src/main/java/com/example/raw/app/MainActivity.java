package com.example.raw.app;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "myLogs";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        File folder = new File(Environment.getExternalStorageDirectory ()+"/eReader");
        if (!folder.exists()) {
            folder.mkdir();
        }
        //FileWriter write = new FileWriter(Environment.getExternalStorageDirectory ()+"/eReader/sas.txt");

        Toolbar toolBar = findViewById(R.id.toolbar);
        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Последние"));
        tabLayout.addTab(tabLayout.newTab().setText("Локальные"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            /*
            case R.id.action_settings:
                text.setText("1");
                return true;
            case R.id.action_bookmarks:
                text.setText("2");
                return true;
            case R.id.action_marks:
                text.setText("3");
                return true;
            case R.id.action_exit:
                text.setText("4");
                android.os.Process.killProcess(android.os.Process.myPid()); //REMAKE
                return true;
                */
        }
        return super.onOptionsItemSelected(item);
    }
}

