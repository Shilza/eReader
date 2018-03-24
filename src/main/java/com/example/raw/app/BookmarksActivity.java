package com.example.raw.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.Utils.FileWorker;

import java.util.ArrayList;

public class BookmarksActivity extends Activity {

    private AlertDialog.Builder ad;
    private int countOfBookmarks = 0;
    private BookmarksRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        for(Book book : FileWorker.getInstance().getRecentBooks())
            if(book.getBookmarks().size() != 0)
                countOfBookmarks++;

        createDialog();

        RecyclerView recyclerView = findViewById(R.id.bookmarks_recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        ArrayList<Book> books = new ArrayList<>();
        for(Book book : FileWorker.getInstance().getRecentBooks())
            if(book.getBookmarks().size() > 0)
                books.add(book);

        if(books.size() == 0)
            ((TextView) findViewById(R.id.tv_count)).setText("Закладок не найдено");

        adapter = new BookmarksRVAdapter(books, this);
        recyclerView.setAdapter(adapter);
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
                if(countOfBookmarks > 0){
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

    @Override
    public boolean onContextItemSelected(MenuItem item){
        adapter.getItemSelected(item);
        return  super.onContextItemSelected(item);
    }
}
