package com.example.raw.app.Viewers;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raw.app.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SimpleTextViewer extends Activity {

    private boolean isExtraMenuHide = false;
    private boolean isSearchActive = false;
    private boolean isPlusMinusActive = false;

    private ArrayList<Integer> resultsList;
    private int resultSelected;
    private int lengthOfQueryString;
    private SpannableStringBuilder sb;
    private BackgroundColorSpan bcs;

    private String comingString;
    private String comingFilePath;

    private TextView tvMainText;
    private LinearLayout footer;
    private LinearLayout plusMinus;
    private LinearLayout searchPanel;
    private SearchView searchView;
    private TextView tvFilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_text_viewer);

        comingFilePath = String.valueOf(getIntent().getSerializableExtra("Text"));
        try{
            comingString = getString(comingFilePath);
        } catch (IOException ex){
            Toast.makeText(this, "Невозможо открыть файл", Toast.LENGTH_SHORT).show();
        }

        footer = findViewById(R.id.txt_viewer_footer);
        plusMinus = findViewById(R.id.txt_viewer_plus_minus);
        plusMinus.setVisibility(View.GONE);

        searchViewInit();

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
    }

    private void searchViewInit(){
        resultsList = new ArrayList<>();
        sb = new SpannableStringBuilder(comingString);
        bcs = new BackgroundColorSpan(Color.rgb(255,64,129));

        searchPanel = findViewById(R.id.txt_viewer_search_panel);
        searchPanel.setVisibility(View.GONE);

        searchView = findViewById(R.id.txt_viewer_search);
        searchView.setVisibility(View.GONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String str) {
                if(comingString.toLowerCase().contains(str.toLowerCase())){
                    lengthOfQueryString = str.length();
                    resultSelected = 0;

                    resultsList.clear();
                    resultsList.add(comingString.indexOf(str));
                    int count = 0;

                    while (resultsList.get(resultsList.size()-1) >= 0) {
                        count++;
                        resultsList.add(comingString.indexOf(str, resultsList.get(resultsList.size()-1) + 1));
                    }

                    setSpan(false);

                    Toast toast = Toast.makeText(getBaseContext(), "Результатов " + count, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    searchPanel.setVisibility(View.VISIBLE);
                } else{
                    setSpan(true);
                    Toast.makeText(getBaseContext(), "Поиск не дал результатов", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchPanel.setVisibility(View.GONE);
                setSpan(true);
                return false;
            }
        });
    }

    private void setSpan(boolean isClear){
        if(isClear)
            sb.removeSpan(bcs);
        else
            sb.setSpan(bcs, resultsList.get(resultSelected),
                resultsList.get(resultSelected)+lengthOfQueryString,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        tvMainText.setText(sb);
    }

    private void footerAnimation(boolean show){
        int value = show ? -footer.getHeight() : footer.getHeight();
        footer.animate().translationYBy(value).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
    }
    
    public void txtViewerOnClick(View view){
        switch (view.getId()){
            case R.id.txt_viewer_button_search:
                if(isPlusMinusActive){
                    plusMinus.setVisibility(View.GONE);
                    isPlusMinusActive = !isPlusMinusActive;
                }
                searchViewAnimation();
                break;

            case R.id.txt_viewer_button_encoding:
                //TODO DIALOG
                /*
                    SortedMap<String, Charset> sa = Charset.availableCharsets();
                    String[] saas = new String[sa.size()];
                    int i =0;
                    for(String str : sa.keySet()){
                        saas[i] = str;
                        i++;
                    }
                */
                break;

            case R.id.txt_viewer_button_share:
                bookSharing();
                break;

            case R.id.txt_viewer_button_text_size:
                sizeChanging();
                break;

            case R.id.txt_viewer_button_text_copy:
                textCopy();
                break;

            case R.id.txt_viewer_button_plus:{
                DisplayMetrics metrics;
                metrics = getApplicationContext().getResources().getDisplayMetrics();
                float textSize = tvMainText.getTextSize()/metrics.density + 2;
                tvMainText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                break;
            }

            case R.id.txt_viewer_button_minus:
                DisplayMetrics metrics;
                metrics = getApplicationContext().getResources().getDisplayMetrics();
                float textSize = tvMainText.getTextSize()/metrics.density - (tvMainText.getTextSize()/metrics.density)/10;
                tvMainText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                break;
        }
    }

    public void txtViewerSearchPanelOnClick(View view){
        switch (view.getId()){
            case R.id.txt_viewer_button_search_backward:
                if(resultSelected > 0)
                    resultSelected--;
                else
                    resultSelected = resultsList.size()-2;

                setSpan(false);
                break;

            case R.id.txt_viewer_button_search_navigation:
                searchView.setIconified(false);
                break;

            case R.id.txt_viewer_button_search_forward:
                if(resultsList.size()-2-resultSelected > 0)
                    resultSelected++;
                else
                    resultSelected = 0;

                setSpan(false);
                break;
        }
    }

    private void textCopy(){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", comingString);
        Toast.makeText(this, "Текст скопирован в буфер обмена", Toast.LENGTH_SHORT).show();
        try{
            clipboard.setPrimaryClip(clip);
        } catch (NullPointerException ex){
            Toast.makeText(this, "Не удалось скопировать в буфер обмена", Toast.LENGTH_SHORT).show();
        }
    }

    private void sizeChanging(){
        isPlusMinusActive = !isPlusMinusActive;
        if(isPlusMinusActive)
            plusMinus.setVisibility(View.VISIBLE);
        else
            plusMinus.setVisibility(View.GONE);
    }

    private void bookSharing(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + comingFilePath));
        startActivity(Intent.createChooser(intent, "Share with"));
    }

    private void searchViewAnimation(){
        isSearchActive=!isSearchActive;

        if(isSearchActive){
            tvFilename.setVisibility(View.GONE);
            searchView.setVisibility(View.VISIBLE);
            searchView.setIconified(false);
        } else{
            tvFilename.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.GONE);
            searchPanel.setVisibility(View.GONE);
            setSpan(true);
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