package com.example.raw.app.Viewers;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

    private ArrayList<Integer> searchResultsList;

    private TextView tvMainText;
    private TextView tvFilename;
    private LinearLayout footer;
    private LinearLayout plusMinus;
    private LinearLayout searchPanel;
    private SearchView searchView;
    private ScrollView mainTextScroll;

    private SpannableStringBuilder sb;
    private BackgroundColorSpan bcs;

    private boolean isExtraMenuHide = false;
    private boolean isSearchActive = false;
    private boolean isPlusMinusActive = false;

    private int resultSelected;
    private int lengthOfQueryString;
    private String comingString;
    private String comingFilePath;

    //Variable for stabilizing animation for extra menu
    private volatile boolean isFooterAnimationStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_text_viewer);

        initializationComingData();
        initializationUI();
    }

    public void txtViewerOnClick(View view) {
        switch (view.getId()) {
            case R.id.acSimpleTextViewerBtSearch:
                startSearch();
                break;

            case R.id.acSimpleTextViewerBtShare:
                bookSharing();
                break;

            case R.id.acSimpleTextViewerBtTextSize:
                sizeChanging();
                break;

            case R.id.acSimpleTextViewerBtCopy:
                textCopy();
                break;

            case R.id.acSimpleTextViewerBtPlus:
                increaseTextSize();
                break;

            case R.id.acSimpleTextViewerBtMinus:
                reduceTextSize();
                break;
        }
    }

    private void initializationComingData() {
        comingFilePath = String.valueOf(getIntent().getSerializableExtra("Filepath"));
        try {
            comingString = getTextFromFile(comingFilePath);
        } catch (IOException ex) {
            Toast.makeText(this, R.string.error_cannot_open_file, Toast.LENGTH_SHORT).show();
        }
    }

    private void initializationUI() {
        footer = findViewById(R.id.acSimpleTextViewerFooter);
        plusMinus = findViewById(R.id.acSimpleTextViewerTextSizeChangingLayout);
        plusMinus.setVisibility(View.GONE);

        searchViewInit();

        mainTextScroll = findViewById(R.id.acSimpleTextViewerMainTextScrollView);

        tvFilename = findViewById(R.id.acSimpleTextViewerFilename);
        tvFilename.setText(new File(comingFilePath).getName());

        tvMainText = findViewById(R.id.acSimpleTextViewerMainTextView);
        tvMainText.setText(comingString);
        tvMainText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isFooterAnimationStarted) {
                    footerAnimation(isExtraMenuHide);
                    isExtraMenuHide = !isExtraMenuHide;
                }

                if (isPlusMinusActive) {
                    plusMinus.setVisibility(View.GONE);
                    isPlusMinusActive = !isPlusMinusActive;
                }
            }
        });

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String font = sharedPref.getString(getString(R.string.settings_fonts), "serif");
        tvMainText.setTypeface(Typeface.create(font, Typeface.NORMAL));

        Float fontSize = sharedPref.getFloat(getString(R.string.settings_font_size), 12);
        tvMainText.setTextSize(fontSize);

        //defValue colorBlack
        int fontColor = sharedPref.getInt(getString(R.string.settings_font_color), -16777216);
        tvMainText.setTextColor(fontColor);
    }

    private void searchViewInit() {
        searchResultsList = new ArrayList<>();
        sb = new SpannableStringBuilder(comingString);
        bcs = new BackgroundColorSpan(Color.rgb(255, 64, 129));

        searchPanel = findViewById(R.id.acSimpleTextViewerSearchPanel);
        searchPanel.setVisibility(View.GONE);

        searchView = findViewById(R.id.acSimpleTextViewerSearchView);
        searchView.setVisibility(View.GONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String str) {
                str = str.toLowerCase();

                if (comingString.toLowerCase().contains(str)) {
                    lengthOfQueryString = str.length();
                    resultSelected = 0;

                    searchResultsList.clear();
                    searchResultsList.add(comingString.toLowerCase().indexOf(str));
                    int count = 0;

                    while (searchResultsList.get(searchResultsList.size() - 1) >= 0) {
                        count++;
                        searchResultsList.add(comingString.toLowerCase().indexOf(
                                str, searchResultsList.get(searchResultsList.size() - 1) + 1));
                    }

                    setSpan(false);
                    changeScrollBarPosition();

                    Toast toast = Toast.makeText(getBaseContext(), getString(R.string.text_viewer_results) + count, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    searchPanel.setVisibility(View.VISIBLE);
                } else {
                    setSpan(true);

                    searchPanel.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), R.string.text_viewer_search_has_not_given_any_results, Toast.LENGTH_SHORT).show();
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

    private void setSpan(boolean isClear) {
        if (isClear)
            sb.removeSpan(bcs);
        else
            sb.setSpan(bcs, searchResultsList.get(resultSelected),
                    searchResultsList.get(resultSelected) + lengthOfQueryString,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        tvMainText.setText(sb);
    }

    private void footerAnimation(boolean show) {
        isFooterAnimationStarted = true;
        int value = show ? -footer.getHeight() : footer.getHeight();
        footer.animate().translationYBy(value).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    isFooterAnimationStarted = false;
                } catch (InterruptedException e) {
                }

            }
        }).start();
    }

    public void txtViewerSearchPanelOnClick(View view) {
        switch (view.getId()) {
            case R.id.acSimpleTextViewerSearchBackward:
                if (resultSelected > 0)
                    resultSelected--;
                else
                    resultSelected = searchResultsList.size() - 2;

                setSpan(false);
                changeScrollBarPosition();

                break;

            case R.id.acSimpleTextViewerSearchNavigation:
                searchView.setIconified(false);
                break;

            case R.id.acSimpleTextViewerSearchForward:
                if (searchResultsList.size() - 2 - resultSelected > 0)
                    resultSelected++;
                else
                    resultSelected = 0;

                setSpan(false);
                changeScrollBarPosition();

                break;
        }
    }

    private void changeScrollBarPosition(){
        tvMainText.post(new Runnable() {
            @Override
            public void run() {
                //value 200 just to aligning
                int tvHeight = getResources().getDisplayMetrics().heightPixels - 200;
                //Approximate number of symbols in line
                int countOfSymbols = tvMainText.length() / tvMainText.getLineCount();
                //The line in which the text is located (approximately)
                int line = searchResultsList.get(resultSelected) / countOfSymbols;
                //Height of one line
                int heightOfLine = tvMainText.getHeight() / tvMainText.getLineCount();
                //if search text in field of vision
                if(heightOfLine*line < mainTextScroll.getScrollY() || heightOfLine*line > mainTextScroll.getScrollY()+tvHeight)
                    mainTextScroll.smoothScrollTo(0, heightOfLine*line);
            }
        });
    }

    private void increaseTextSize() {
        DisplayMetrics metrics;
        metrics = getApplicationContext().getResources().getDisplayMetrics();
        float textSize = tvMainText.getTextSize() / metrics.density + 2;
        tvMainText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    private void reduceTextSize() {
        DisplayMetrics metrics;
        metrics = getApplicationContext().getResources().getDisplayMetrics();
        float textSize = tvMainText.getTextSize() / metrics.density - (tvMainText.getTextSize() / metrics.density) / 10;
        tvMainText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    private void startSearch() {
        if (isPlusMinusActive) {
            plusMinus.setVisibility(View.GONE);
            isPlusMinusActive = !isPlusMinusActive;
        }
        searchViewAnimation();
    }

    private void textCopy() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", comingString);
        Toast.makeText(this, R.string.text_viewer_text_copy_to_buffer, Toast.LENGTH_SHORT).show();
        try {
            clipboard.setPrimaryClip(clip);
        } catch (NullPointerException ex) {
            Toast.makeText(this, R.string.text_viewer_text_could_not_copy, Toast.LENGTH_SHORT).show();
        }
    }

    private void sizeChanging() {
        isPlusMinusActive = !isPlusMinusActive;
        if (isPlusMinusActive)
            plusMinus.setVisibility(View.VISIBLE);
        else
            plusMinus.setVisibility(View.GONE);
    }

    private void bookSharing() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + comingFilePath));
        startActivity(Intent.createChooser(intent, getString(R.string.intent_share)));
    }

    private void searchViewAnimation() {
        isSearchActive = !isSearchActive;

        if (isSearchActive) {
            tvFilename.setVisibility(View.GONE);
            searchView.setVisibility(View.VISIBLE);
            searchView.setIconified(false);
        } else {
            tvFilename.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.GONE);
            searchPanel.setVisibility(View.GONE);
            setSpan(true);
        }
    }

    private String getTextFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
