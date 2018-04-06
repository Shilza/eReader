package com.example.raw.app.Main.Navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.R;
import com.example.raw.app.Utils.FileWorker;
import com.example.raw.app.Utils.Repository;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StatisticsActivity extends Activity {

    private AlertDialog.Builder ad;
    private ArrayList<Book> books;
    private TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        books = Repository.getInstance().getRecentBooks();

        initUI();
        createDialog();
        createSpeech();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
    }

    private void initUI() {
        TableLayout table = findViewById(R.id.acStatisticsTableLayout);

        String[] list = new String[6];
        list[0] = getString(R.string.statistics_hours_of_read);
        list[1] = getString(R.string.statistics_pages);
        list[2] = getString(R.string.statistics_books_total);
        list[3] = getString(R.string.statistics_books_readed);
        list[4] = getString(R.string.statistics_read);
        list[5] = getString(R.string.statistic_bookmarks);

        String[] list1 = initParams();

        int index = 0;
        for (int i = 0; i < 3; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));

            for (int j = 0; j < 2; j++) {
                View cardView = LayoutInflater.from(this).inflate(R.layout.statistics_item, row, false);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                ((TextView) cardView.findViewById(R.id.acStatisticsItemCount)).setText(list1[index]);
                ((TextView) cardView.findViewById(R.id.acStatisticsItemDescription)).setText(list[index]);
                row.addView(cardView, j);
                index++;
            }
            table.addView(row, i);
        }

        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));
    }

    public void statisticsOnClick(View view) {
        switch (view.getId()) {

            case R.id.acStatisticsActionBack:
                finish();
                break;

            case R.id.acStatisticsSettings:
                actionSettings();
                break;
        }
    }

    private void actionSettings() {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.acStatisticsSettings));
        popup.getMenuInflater().inflate(R.menu.menu_statistics, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.acStatisticsActionRemove:
                        ad.show();
                        break;
                }
                return true;
            }
        });

        popup.show();
    }

    private void removeStatistics() {
        for(Book book : books)
            book.setTimeOfReading(0);

        FileWorker.getInstance().refreshingJSON();
    }

    private void createDialog() {
        ad = new AlertDialog.Builder(this);
        ad.setTitle(R.string.dialog_title_delete_statistics);
        ad.setMessage(R.string.dialog_confirmation_of_removal_statistics);
        ad.setPositiveButton(R.string.dialog_consent, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                removeStatistics();
                Toast.makeText(getBaseContext(), R.string.dialog_statistics_cleared,
                        Toast.LENGTH_SHORT).show();
            }
        });
        ad.setNegativeButton(R.string.dialog_denial, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
    }

    private void createSpeech(){
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    Locale locale = new Locale("ru");
                    int result = mTTS.setLanguage(locale);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED)
                        Toast.makeText(getBaseContext(), "Этот язык не поддерживается", Toast.LENGTH_SHORT).show();
                    else
                        mTTS.speak("Меня всегда огорчало, что в Android не было синтезатора речи на русском. Изначально выбор языков был ограничен английским, испанским, французским, немецким и итальянским. Существовали отдельные коммерческие движки, а также производители могли добавить в свои устройства какой-нибудь движок с нужным языком, видимо договорившись с разработчиком. Но хотелось поддержки из коробки от самой «корпорации добра", TextToSpeech.QUEUE_FLUSH, null);
                    //mTTS.speak("Deborah was angry at her son. Her son didn't listen to her. Her son was 16 years old. Her son thought he knew everything. Her son yelled at Deborah. He told her he didn't have to do anything. He didn't have to listen to her. He didn't have to go to school. He didn't have to do his homework. He didn't have to study. He was 16. He could do anything he wanted to do. What could Deborah do? She wasn't married. She was divorced. She could not control her son. He would listen to his father. But his father was not there. His father lived in another city.", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    private String[] initParams(){
        String[] list =  new String[6];

        float hours = allReadingTime();
        String pattern = "##";
        if(hours < 1)
            pattern = "#0.0#";
        DecimalFormat f = new DecimalFormat(pattern);
        list[0] = f.format(hours);

        list[1] = "1318";
        list[2] = String.valueOf(books.size() + Repository.getInstance().getLocalBooks().size());

        int countOfReadedBooks = 0;
        for(Book book : books)
            if(book.getTotalRead() >= 0.98f)
                countOfReadedBooks++;

        list[3] = String.valueOf(countOfReadedBooks);
        list[4] = String.valueOf(books.size());
        int bookmarksCount = 0;
        for(Book book : books)
            if(book.getBookmarks().size() > 0)
                bookmarksCount ++;
        list[5] = String.valueOf(bookmarksCount);

        return list;
    }

    private float allReadingTime(){
        long time = 0;
        for(Book book : books)
            time += book.getTimeOfReading();

        return (float) time/(1000 * 3600); //in hours
    }
}