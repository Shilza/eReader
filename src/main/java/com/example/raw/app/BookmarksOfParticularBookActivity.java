package com.example.raw.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.Entities.Bookmark;
import com.example.raw.app.Utils.FileWorker;
import com.example.raw.app.Viewers.PDFViewer;

import java.util.ArrayList;

public class BookmarksOfParticularBookActivity extends Activity {

    private AlertDialog.Builder ad;
    private ArrayList<Bookmark> bookmarks;
    private Book book;
    private BookmarkOfParticularBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks_of_particular_book);
        setData();

        if (bookmarks.size() == 0)
            ((TextView) findViewById(R.id.acBmOfParticularBookTvCount)).setText("Закладок не найдено");

        RecyclerView recyclerView = findViewById(R.id.acBmOfParticularBookRecyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        adapter = new BookmarkOfParticularBookAdapter(book, bookmarks, this);
        recyclerView.setAdapter(adapter);

        createDialog();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        adapter.getItemSelected(item);
        return super.onContextItemSelected(item);
    }

    public void bmPartBookActivityOnClick(View view) {
        switch (view.getId()) {
            case R.id.acBmOfParticularBookActionBack:
                finish();
                break;

            case R.id.acBmOfParticularBookActionRemoving:
                ad.show();
                break;
        }
    }

    private boolean removingBookmarks() {
        if (bookmarks.size() > 0) {
            book.getBookmarks().removeAll(bookmarks);
            bookmarks.clear();
            FileWorker.getInstance().refreshingJSON(FileWorker.getInstance().getRecentBooks());
            adapter.notifyDataSetChanged();
            return true;
        }

        return false;
    }

    private void createDialog() {
        ad = new AlertDialog.Builder(this);
        ad.setTitle("Очистить закладки");
        ad.setMessage("Действительно хотите очистить закладки?");
        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                if (removingBookmarks())
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

    private void setData() {
        bookmarks = (ArrayList<Bookmark>) getIntent().getSerializableExtra("Bookmarks");
        String filePath = String.valueOf(getIntent().getSerializableExtra("FilePath"));

        for (Book obj : FileWorker.getInstance().getRecentBooks())
            if (obj.getFilePath().equals(filePath)) {
                book = obj;
                break;
            }
    }
}
