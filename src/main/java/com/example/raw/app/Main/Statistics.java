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

import com.example.raw.app.R;

public class Statistics extends Activity {

    private AlertDialog.Builder ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initUI();
        createDialog();
    }

    private void initUI(){
        TableLayout table = findViewById(R.id.statistics_table_layout);

        String[] list = new String[4];
        list[0] = "Часов чтения";
        list[1] = "Пролистано\nстраниц";
        list[2] = "Книг всего";
        list[3] = "Книг прочитано";

        String[] list1 = new String[4];
        list1[0] = "13";
        list1[1] = "1318";
        list1[2] = "9";
        list1[3] = "3";

        int a=0;
        for(int i = 0; i<2; i++){
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT));

            for(int j = 0; j<2; j++){
                View cardView = LayoutInflater.from(this).inflate(R.layout.statistics_item, row, false);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                ((TextView)cardView.findViewById(R.id.statistics_count)).setText(list1[a]);
                ((TextView)cardView.findViewById(R.id.statistics_description)).setText(list[a]);
                row.addView(cardView, j);
                a++;
            }
            table.addView(row, i);
        }

        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));

        for(int j = 0; j<2; j++){
            View cardView = LayoutInflater.from(this).inflate(R.layout.statistics_item, row, false);
            ((TextView)cardView.findViewById(R.id.statistics_count)).setText("1");
            ((TextView)cardView.findViewById(R.id.statistics_description)).setText("Sas-sas");
            row.addView(cardView, j);
            a++;
        }

        table.addView(row, 2);
    }

    public void statisticsOnClick(View view){
        switch (view.getId()){

            case R.id.statistic_action_back:
                finish();
                break;

            case R.id.statistic_action_settings:
                actionSettings();
                break;
        }
    }

    private void actionSettings(){
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.statistic_action_settings));
        popup.getMenuInflater().inflate(R.menu.statistics_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.statistics_menu_clear:
                        ad.show();
                        break;
                    case R.id.statistics_menu_books:
                        //TODO
                        break;
                }
                return true;
            }
        });

        popup.show();
    }

    private boolean removingStatistics(){
        //TODO
        return false;
    }

    private void createDialog(){
        ad = new AlertDialog.Builder(this);
        ad.setTitle("Очистить статистику");
        ad.setMessage("Действительно хотите очистить статистику?");
        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                if(removingStatistics())
                    Toast.makeText(getBaseContext(), "Статистика очищена",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getBaseContext(), "Статистика пуста",
                            Toast.LENGTH_SHORT).show();
            }
        });
        ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
    }
}