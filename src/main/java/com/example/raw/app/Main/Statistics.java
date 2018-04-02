package com.example.raw.app.Main;

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

import java.util.ArrayList;

public class Statistics extends Activity {

    private AlertDialog.Builder ad;
    private ArrayList<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        books = FileWorker.getInstance().getRecentBooks();

        initUI();
        createDialog();
    }

    private void initUI() {
        TableLayout table = findViewById(R.id.acStatisticsTableLayout);

        String[] list = new String[4];
        list[0] = "Часов чтения";
        list[1] = "Пролистано\nстраниц";
        list[2] = "Книг всего";
        list[3] = "Книг прочитано";

        String[] list1 = new String[4];
        list1[0] = "13";
        list1[1] = "1318";
        list1[2] = String.valueOf(books.size() + FileWorker.getInstance().getLocalBooks().size());

        int count = 0;
        for(Book book : books)
            if(book.getTotalRead() >= 99)
                count++;

        list1[3] = String.valueOf(count);

        int a = 0;
        for (int i = 0; i < 2; i++) {
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
                ((TextView) cardView.findViewById(R.id.acStatisticsItemCount)).setText(list1[a]);
                ((TextView) cardView.findViewById(R.id.acStatisticsItemDescription)).setText(list[a]);
                row.addView(cardView, j);
                a++;
            }
            table.addView(row, i);
        }

        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));

        for (int j = 0; j < 2; j++) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.statistics_item, row, false);
            ((TextView) cardView.findViewById(R.id.acStatisticsItemCount)).setText("1");
            ((TextView) cardView.findViewById(R.id.acStatisticsItemDescription)).setText("Закладок");
            row.addView(cardView, j);
            a++;
        }

        table.addView(row, 2);
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

    private boolean removeStatistics() {
        //TODO
        return false;
    }

    private void createDialog() {
        ad = new AlertDialog.Builder(this);
        ad.setTitle(R.string.dialog_title_delete_statistics);
        ad.setMessage(R.string.dialog_confirmation_of_removal_statistics);
        ad.setPositiveButton(R.string.dialog_consent, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                if (removeStatistics())
                    Toast.makeText(getBaseContext(), R.string.dialog_statistics_cleared,
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getBaseContext(), R.string.dialog_statistics_is_empty,
                            Toast.LENGTH_SHORT).show();
            }
        });
        ad.setNegativeButton(R.string.dialog_denial, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
    }
}