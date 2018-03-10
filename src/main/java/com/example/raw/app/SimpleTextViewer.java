package com.example.raw.app;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SimpleTextViewer extends AppCompatActivity{
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_text_viewer);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_simple_text_viewer));
        ((Toolbar) findViewById(R.id.toolbar_simple_text_viewer)).setTitle("");
        textView = findViewById(R.id.simple_text_viewer_text_view);
        textView.setText(String.valueOf(getIntent().getSerializableExtra("Text")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_simple_text_viewer, menu);

        return true;
    }
}
