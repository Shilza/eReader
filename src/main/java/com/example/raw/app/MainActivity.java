package com.example.raw.app;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "myLogs";
    TextView text;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        text = findViewById(R.id.text);

        File folder = new File(Environment.getExternalStorageDirectory ()+"/eReader");
        if (!folder.exists()) {
            folder.mkdir();
        }
        //FileWriter write = new FileWriter(Environment.getExternalStorageDirectory ()+"/eReader/sas.txt");


        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Последние"));
        tabLayout.addTab(tabLayout.newTab().setText("Локальные"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        ViewPager viewPager = findViewById(R.id.pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
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
        }
        return super.onOptionsItemSelected(item);
    }
}

