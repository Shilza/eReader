package com.example.raw.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raw.app.Entities.Book;

import java.io.File;
import java.util.ArrayList;

public class BookmarksActivity extends Activity {

    private AlertDialog.Builder ad;
    private int countofBookmarks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        for(Book book : FileWorker.getInstance().getRecentBooks())
            if(book.getBookmarks().size() != 0)
                countofBookmarks++;

        ((TextView) findViewById(R.id.tv_count)).setText("Количество книг, имеющих закладки " + countofBookmarks);
        createDialog();
    }

    public void bookmarksActivityOnClick(View view){
        switch (view.getId()){
            case R.id.bookmarks_activity_action_back:
                finish();
                break;

            case R.id.bookmarks_activity_action_removing:
                ad.show();
                break;
        }
    }

    private void removingBookmarks(){
        ArrayList<Book> recentBooks = FileWorker.getInstance().getRecentBooks();
        for(Book book : recentBooks)
            book.getBookmarks().clear();

        FileWorker.getInstance().refreshingJSON(recentBooks);
    }

    private void createDialog(){
        ad = new AlertDialog.Builder(this);
        ad.setTitle("Очистить закладки");
        ad.setMessage("Действительно хотите очистить закладки?");
        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                if(countofBookmarks > 0){
                    removingBookmarks();
                    Toast.makeText(getBaseContext(), "Закладки очищены",
                            Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getBaseContext(), "Закладок не найдено",
                            Toast.LENGTH_SHORT).show();
            }
        });
        ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
    }
}
