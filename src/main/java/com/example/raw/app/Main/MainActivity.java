package com.example.raw.app.Main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.Toast;

import com.example.raw.app.Main.Adapters.SearchRVAdapter;
import com.example.raw.app.Main.Adapters.TabPagerAdapter;
import com.example.raw.app.R;
import com.example.raw.app.Utils.BookOpener;
import com.example.raw.app.Utils.FileWorker;

import java.io.File;
import java.util.Set;

public class MainActivity extends AppCompatActivity{
    private SearchRVAdapter searchRVAdapter;
    private RecyclerView searchRecyclerView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ActionBarDrawerToggle toggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.acMainToolbar));
        initDrawer();
        initTabs();
        initSearch();

        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(sharedPref.getBoolean(getString(R.string.settings_autoStart), false))
            BookOpener.getInstance().opening(FileWorker.getInstance().getRecentBooks().get(0), this);
    }

    private void initDrawer(){
        DrawerLayout drawer = findViewById(R.id.acMainDrawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((NavigationView) findViewById(R.id.acMainNavigationView)).setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.acMainActionOpenBook:
                        Intent intent = new Intent()
                                .setType("*/*")
                                .setAction(Intent.ACTION_GET_CONTENT);

                        startActivityForResult(Intent.createChooser(intent, getString(R.string.intent_select_file)), 1);
                        return true;

                    case R.id.acMainActionBookmarks:
                        startActivity(new Intent(getBaseContext(), BookmarksActivity.class));
                        return true;

                    case R.id.acMainActionStatistics:
                        startActivity(new Intent(getBaseContext(), Statistics.class));
                        return true;

                    case R.id.acMainActionSettings:
                        startActivity(new Intent(getBaseContext(), Settings.class));
                        return true;

                    case R.id.acMainActionLike:
                        return true;

                    case R.id.acMainActionExit:
                        android.os.Process.killProcess(android.os.Process.myPid());
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK)
            bookOpening(data);
    }

    private void bookOpening(Intent data){
        Uri selectedFile = data.getData();
        File file = new File(getRealPathFromURI(selectedFile));

        try{
            BookOpener.getInstance().opening(file,this);
        } catch (IllegalArgumentException e){
            Toast.makeText(this, R.string.error_unsupported_format, Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);

        if (cursor == null)
            result = contentURI.getPath();
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }

        return result;
    }

    private void initTabs(){
        tabLayout = findViewById(R.id.acMainTabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_recent_books_location));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_local_books_location));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = findViewById(R.id.acMainPager);
        TabPagerAdapter pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    private void initSearch(){
        searchRVAdapter = new SearchRVAdapter(this);
        searchRecyclerView = findViewById(R.id.acMainSearchRecyclerView);
        searchRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        searchRecyclerView.setLayoutManager(layoutManager);
        searchRecyclerView.setAdapter(searchRVAdapter);
        searchRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        searchRVAdapter.getItemSelected(item);
        return super.onContextItemSelected(item);
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
                if (searchRVAdapter != null) searchRVAdapter.getFilter().filter(newText);
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item);
    }
}

