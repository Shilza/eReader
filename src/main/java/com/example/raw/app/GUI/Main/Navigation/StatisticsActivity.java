package com.example.raw.app.GUI.Main.Navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

public class StatisticsActivity extends Activity {

    private AlertDialog.Builder ad;
    private ArrayList<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        books = Repository.getInstance().getRecentBooks();

        initUI();
        createDialog();
    }

    private void initUI() {
        TableLayout table = findViewById(R.id.acStatisticsTableLayout);

        String[] listOfItems = new String[6];
        listOfItems[0] = getString(R.string.statistics_hours_of_read);
        listOfItems[1] = getString(R.string.statistics_pages);
        listOfItems[2] = getString(R.string.statistics_books_total);
        listOfItems[3] = getString(R.string.statistics_books_readed);
        listOfItems[4] = getString(R.string.statistics_read);
        listOfItems[5] = getString(R.string.statistic_bookmarks);

        String[] listOfValues = initStatisticsParams();

        int listItemIndex = 0;
        for (int i = 0; i < 3; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));

            for (int j = 0; j < 2; j++) {
                View cardView = LayoutInflater.from(this).inflate(R.layout.statistics_item, row, false);
                if (i == 0)
                    cardView.setOnClickListener((View view) -> {
                    });
                ((TextView) cardView.findViewById(R.id.acStatisticsItemCount)).setText(listOfValues[listItemIndex]);
                ((TextView) cardView.findViewById(R.id.acStatisticsItemDescription)).setText(listOfItems[listItemIndex]);
                row.addView(cardView, j);
                listItemIndex++;
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

        popup.setOnMenuItemClickListener((MenuItem item) -> {
            switch (item.getItemId()) {
                case R.id.acStatisticsActionRemove:
                    ad.show();
                    break;
            }
            return true;
        });

        popup.show();
    }

    private void removeStatistics() {
        for (Book book : books)
            book.setTimeOfReading(0);

        FileWorker.getInstance().refreshingJSON();
    }

    private void createDialog() {
        ad = new AlertDialog.Builder(this);
        ad.setTitle(R.string.dialog_title_delete_statistics);
        ad.setMessage(R.string.dialog_confirmation_of_removal_statistics);
        ad.setPositiveButton(R.string.dialog_consent, (DialogInterface dialog, int arg1) -> {
            removeStatistics();
            Toast.makeText(getBaseContext(), R.string.dialog_statistics_cleared,
                    Toast.LENGTH_SHORT).show();
        });
        ad.setNegativeButton(R.string.dialog_denial, (DialogInterface dialog, int arg1) -> {
        });
    }

    private String[] initStatisticsParams() {
        String[] list = new String[6];

        float hours = allReadingTime();
        String pattern = "##";
        if (hours < 1)
            pattern = "#0.0#";
        DecimalFormat f = new DecimalFormat(pattern);
        list[0] = f.format(hours);

        int browsPagesCount = 0;
        for (Book book : books)
            browsPagesCount += book.getCountOfBrowsePages();
        list[1] = String.valueOf(browsPagesCount);

        list[2] = String.valueOf(books.size() + Repository.getInstance().getLocalBooks().size());

        int countOfReadedBooks = 0;
        for (Book book : books)
            if (book.getTotalRead() >= 0.98f)
                countOfReadedBooks++;

        list[3] = String.valueOf(countOfReadedBooks);
        list[4] = String.valueOf(books.size());

        int bookmarksCount = 0;
        for (Book book : books)
            if (book.getBookmarks().size() > 0)
                bookmarksCount += book.getBookmarks().size();
        list[5] = String.valueOf(bookmarksCount);

        return list;
    }

    private float allReadingTime() {
        long time = 0;
        for (Book book : books)
            time += book.getTimeOfReading();

        return (float) time / (1000 * 3600); //in hours
    }
}