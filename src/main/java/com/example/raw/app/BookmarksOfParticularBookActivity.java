package com.example.raw.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.Utils.FileWorker;

public class BookmarksOfParticularBookActivity extends Activity{

    private AlertDialog.Builder ad;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks_of_particular_book);

        createDialog();
    }

    public void bmPartBookActivityOnClick(View view){
        switch (view.getId()){
            case R.id.bm_of_part_book_act_back:
                finish();
                break;

            case R.id.bm_of_part_book_act_removing:
                ad.show();
                break;
        }
    }

    private boolean removingBookmarks() {
        if (book.getBookmarks().size() > 0) {
            book.getBookmarks().clear();
            FileWorker.getInstance().refreshingJSON(FileWorker.getInstance().getRecentBooks());
            return true;
        }

        return  false;
    }

    private void createDialog() {
        ad = new AlertDialog.Builder(this);
        ad.setTitle("Очистить закладки");
        ad.setMessage("Действительно хотите очистить закладки?");
        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                if(removingBookmarks())
                    Toast.makeText(getBaseContext(), "Закладки очищены",
                            Toast.LENGTH_SHORT).show();
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
