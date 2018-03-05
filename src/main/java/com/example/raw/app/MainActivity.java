package com.example.raw.app;

import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

public class MainActivity extends AppCompatActivity{
    private SearchAdapter searchAdapter;
    private RecyclerView searchRecyclerView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        FileWorker.checkAppFolder();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Последние"));
        tabLayout.addTab(tabLayout.newTab().setText("Локальные"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.pager);
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
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

        searchAdapter = new SearchAdapter(FileWorker.getRecentBooks(), FileWorker.getLocalBooks());
        searchRecyclerView = findViewById(R.id.search_recycler_view);
        searchRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        searchRecyclerView.setLayoutManager(layoutManager);
        searchRecyclerView.setAdapter(searchAdapter);
        searchRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                tabLayout.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                searchRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                searchRecyclerView.setVisibility(View.GONE);
            }
        });
        search(searchView);

        return true;
    }
    private void search(final SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchAdapter != null) searchAdapter.getFilter().filter(newText);
                return true;
            }
        });

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

