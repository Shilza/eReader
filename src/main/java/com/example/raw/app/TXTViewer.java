package com.example.raw.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.SortedMap;

public class TXTViewer extends AppCompatActivity{
    private boolean isExtraMenuHide = false;
    private boolean isSearchActive = false;
    private TextView tvMainText;
    private LinearLayout footer;
    private SearchView searchView;
    private String comingString;
    private TextView tvFilename;
    private TXTViewerDialogFragment dialogFragment;
    private String comingFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.txt_viewer);

        comingFilePath = String.valueOf(getIntent().getSerializableExtra("Text"));
        try{
            comingString = getString(comingFilePath);
        } catch (IOException ex){
            Toast.makeText(this, "Невозможо открыть файл", Toast.LENGTH_SHORT).show();
        }

        footer = findViewById(R.id.txt_viewer_footer);

        searchView = findViewById(R.id.txt_viewer_search);
        searchView.animate().translationYBy(-searchView.getHeight()).
                setDuration(0).setInterpolator(new AccelerateInterpolator()).start();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        tvMainText = findViewById(R.id.txt_viewer_text_view);
        tvMainText.setText(comingString);
        tvMainText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                footerAnimation(isExtraMenuHide);
                isExtraMenuHide = !isExtraMenuHide;
            }
        });

        tvFilename = findViewById(R.id.txt_viewer_filename);
        tvFilename.setText(new File(comingFilePath).getName());

        SortedMap<String, Charset> sa = Charset.availableCharsets();
        String[] saas = new String[sa.size()];
        int i =0;
        for(String str : sa.keySet()){
            saas[i] = str;
            i++;
        }
        dialogFragment = new TXTViewerDialogFragment();
        dialogFragment.set(tvMainText, comingString, saas, this);
    }

    private void footerAnimation(boolean show){
        int value = show ? -footer.getHeight() : footer.getHeight() ;
        footer.animate().translationYBy(value).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
    }
    
    private void searchViewAnimation(boolean show){
        int value = show ?  searchView.getHeight() : -searchView.getHeight() ;
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
                searchViewAnimation(isSearchActive);
                isSearchActive = !isSearchActive;
                break;
            case R.id.txt_viewer_button_encoding:
                break;
            case R.id.txt_viewer_button_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + comingFilePath));
                startActivity(Intent.createChooser(intent, "Share with"));
                //shareActionProvider.setShareIntent(intent);
                break;
            case R.id.txt_viewer_button_text_size:
                tvMainText.setTextSize(tvMainText.getTextSize()*2);
                break;
        }
    }

    private String getString(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        while((line = reader.readLine()) != null){
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }
}
