package com.example.raw.app;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class TXTViewer extends Activity {
    private boolean isExtraMenuHide = false;
    private boolean isSearchActive = false;
    private boolean isPlusMinusActive = false;
    private TextView tvMainText;
    private LinearLayout footer;
    private LinearLayout plusMinus;
    private SearchView searchView;
    private String comingString;
    private TextView tvFilename;
    private String comingFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt_viewer);

        comingFilePath = String.valueOf(getIntent().getSerializableExtra("Text"));
        try{
            comingString = getString(comingFilePath);
        } catch (IOException ex){
            Toast.makeText(this, "Невозможо открыть файл", Toast.LENGTH_SHORT).show();
        }

        footer = findViewById(R.id.txt_viewer_footer);
        plusMinus = findViewById(R.id.txt_viewer_plus_minus);
        plusMinus.setVisibility(View.GONE);

        searchView = findViewById(R.id.txt_viewer_search);
        searchView.setVisibility(View.GONE);
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

                if(isPlusMinusActive){
                    plusMinus.setVisibility(View.GONE);
                    isPlusMinusActive = !isPlusMinusActive;
                }
            }
        });

        tvFilename = findViewById(R.id.txt_viewer_filename);
        tvFilename.setText(new File(comingFilePath).getName());

        /*
        SortedMap<String, Charset> sa = Charset.availableCharsets();
        String[] saas = new String[sa.size()];
        int i =0;
        for(String str : sa.keySet()){
            saas[i] = str;
            i++;
        }
        */
    }

    private void footerAnimation(boolean show){
        int value = show ? -footer.getHeight() : footer.getHeight() ;
        footer.animate().translationYBy(value).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
    }
    
    public void txtViewerOnClick(View view){
        switch (view.getId()){
            case R.id.txt_viewer_button_search:
                isSearchActive=!isSearchActive;
                if(isSearchActive){
                    tvFilename.setVisibility(View.GONE);
                    searchView.setVisibility(View.VISIBLE);
                    searchView.setIconified(false);
                } else{
                    tvFilename.setVisibility(View.VISIBLE);
                    searchView.setVisibility(View.GONE);
                }
                break;
            case R.id.txt_viewer_button_encoding:
                break;
            case R.id.txt_viewer_button_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + comingFilePath));
                startActivity(Intent.createChooser(intent, "Share with"));
                break;
            case R.id.txt_viewer_button_text_size:
                isPlusMinusActive = !isPlusMinusActive;
                if(isPlusMinusActive)
                    plusMinus.setVisibility(View.VISIBLE);
                else
                    plusMinus.setVisibility(View.GONE);
                break;
            case R.id.txt_viewer_button_text_copy:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", comingString);
                Toast.makeText(this, "Текст скопирован в буфер обмена", Toast.LENGTH_SHORT).show();
                try{
                    clipboard.setPrimaryClip(clip);
                } catch (NullPointerException ex){
                    Toast.makeText(this, "Не удалось скопировать в буфер обмена", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txt_viewer_button_plus:
                tvMainText.setTextSize(tvMainText.getTextSize()+1);
                break;
            case R.id.txt_viewer_button_minus:
                tvMainText.setTextSize(tvMainText.getTextSize()-1);
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
