package com.example.raw.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.nio.charset.Charset;
import java.util.SortedMap;

public class TXTViewer extends AppCompatActivity{
    private TextView textView;
    private boolean isExtraMenuHide = false;
    private LinearLayout footer;
    private SearchView searchView;
    private String comingString;
    private TXTViewerDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.txt_viewer);
        comingString = String.valueOf(getIntent().getSerializableExtra("Text"));

        footer = findViewById(R.id.txt_viewer_footer);
        searchView = findViewById(R.id.txt_viewer_search);
        searchView.setIconifiedByDefault(false);
        textView = findViewById(R.id.txt_viewer_text_view);
        textView.setText(comingString);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                footerAnimation(isExtraMenuHide);
                isExtraMenuHide = !isExtraMenuHide;
            }
        });

        SortedMap<String, Charset> sa = Charset.availableCharsets();
        String[] saas = new String[sa.size()];
        int i =0;
        for(String str : sa.keySet()){
            saas[i] = str;
            i++;
        }
        dialogFragment = new TXTViewerDialogFragment();
        dialogFragment.set(textView, comingString, saas, this);
    }

    private void footerAnimation(boolean show){
        int value = show ? -footer.getHeight() : footer.getHeight() ;
        footer.animate().translationYBy(value).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
    }
    
    private void searchViewAnimation(boolean show){
        int value = show ? searchView.getHeight() : -searchView.getHeight() ;
        searchView.animate().translationYBy(value).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_txt_viewer, menu);
        return true;
    }
    
    public void buttonsOnClick(View view){
        switch (view.getId()){
            case R.id.txt_viewer_button_search:
                searchViewAnimation(isExtraMenuHide);
            case R.id.txt_viewer_button_encoding:
                break;
        }
    }
}
