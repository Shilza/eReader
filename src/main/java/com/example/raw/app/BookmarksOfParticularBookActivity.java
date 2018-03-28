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
import android.widget.Toast;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.Entities.Bookmark;
import com.example.raw.app.Utils.FileWorker;
import com.example.raw.app.Viewers.PDFViewer;

import java.util.ArrayList;

public class BookmarksOfParticularBookActivity extends Activity{

    private AlertDialog.Builder ad;
    private ArrayList<Bookmark> bookmarks;
    private Book book;
    private BookmarkOfParticularBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks_of_particular_book);
        setData();

        RecyclerView recyclerView = findViewById(R.id.bookmarks_of_particular_book_recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        adapter = new BookmarkOfParticularBookAdapter(book, bookmarks, this);
        recyclerView.setAdapter(adapter);

        createDialog();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        adapter.getItemSelected(item);
        return  super.onContextItemSelected(item);
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
        if (bookmarks.size() > 0) {
            bookmarks.clear();
            FileWorker.getInstance().refreshingJSON(FileWorker.getInstance().getRecentBooks());
            adapter.notifyDataSetChanged();
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

    private void setData(){
        bookmarks = (ArrayList<Bookmark>) getIntent().getSerializableExtra("Bookmarks");
        String filePath = String.valueOf(getIntent().getSerializableExtra("FilePath"));

        for(Book obj : FileWorker.getInstance().getRecentBooks())
            if(obj.getFilePath().equals(filePath)){
                book = obj;
                break;
            }
        if(book == null)
            for(Book obj : FileWorker.getInstance().getLocalBooks())
                if(obj.getFilePath().equals(filePath)){
                    book = obj;
                    break;
                }

        ArrayList<Bookmark> bm = new ArrayList<>();

        for(Bookmark bookmark : book.getBookmarks())
            for(Bookmark bookmark1 : bookmarks){
                if(bookmark.equals(bookmark1)){
                    bm.add(bookmark);
                    break;
                }
            }

        bookmarks.clear();
        bookmarks.addAll(bm);
    }
}
