package com.example.raw.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

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

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initDrawer();
        initTabs();
        initSearch();
    }

    private void initDrawer(){
        DrawerLayout drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((NavigationView) findViewById(R.id.navigation_view)).setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.action_open_book:
                        Intent intent = new Intent()
                                .setType("*/*")
                                .setAction(Intent.ACTION_GET_CONTENT);

                        startActivityForResult(Intent.createChooser(intent, "Выберите файл"), 1);

                        return true;
                    case R.id.action_bookmarks:
                        return true;
                    case R.id.action_statistics:
                        return true;
                    case R.id.action_settings:
                        return true;
                    case R.id.action_like:
                        return true;
                    case R.id.action_exit:
                        android.os.Process.killProcess(android.os.Process.myPid()); //REMAKE
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK) {
            Uri selectedFile = data.getData();
            File file = new File(getRealPathFromURI(selectedFile));

            boolean isReadable = false;
            for (Extensions ext : Extensions.values())
                if (file.getName().endsWith(ext.getDescription())) {
                    read(file);
                    isReadable = true;
                    break;
                }

            if(!isReadable)
                Toast.makeText(this, "Неподдерживаемый формат", Toast.LENGTH_SHORT).show();
        }

    }

    private void read(File file){
        Book book = FileWorker.getInstance().bookPreparing(file);
        boolean isBookExistInList = false;

        boolean isSearchable = false;
        for(Extensions ext : Extensions.searchableExtensions()){
            if(book.getExtension() == ext){
                isSearchable = true;
                break;
            }
        }

        if(isSearchable){
            for (Book obj : FileWorker.getInstance().getRecentBooks()) {
                if (obj.getFilePath().equals(book.getFilePath())) {
                    Book item = FileWorker.getInstance().getRecentBooks().remove(
                            FileWorker.getInstance().getRecentBooks().indexOf(obj)
                    );
                    FileWorker.getInstance().addingToRecentBooks(item);
                    isBookExistInList = true;
                    break;
                }
            }

            for (Book obj : FileWorker.getInstance().getLocalBooks()) {
                if (obj.getFilePath().equals(book.getFilePath())) {
                    FileWorker.getInstance().removeBookFromLocalBooks(obj);
                    FileWorker.getInstance().addingToRecentBooks(book);
                    isBookExistInList = true;
                    break;
                }
            }

            if (!isBookExistInList) {
                FileWorker.getInstance().addingToRecentBooks(book);
                TabKeeper.getInstance().notifyDataSetChanged();
            }
        }

        BookOpener.getInstance().opening(book, this);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

    private void initTabs(){
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
    }

    private void initSearch(){
        ArrayList<Book> list = new ArrayList<>(FileWorker.getInstance().getRecentBooks());
        list.addAll(FileWorker.getInstance().getLocalBooks());

        searchRVAdapter = new SearchRVAdapter(list, this);
        searchRecyclerView = findViewById(R.id.search_recycler_view);
        searchRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        searchRecyclerView.setLayoutManager(layoutManager);
        searchRecyclerView.setAdapter(searchRVAdapter);
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

