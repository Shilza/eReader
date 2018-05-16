package com.example.raw.app.GUI.Main.Navigation;

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
import com.example.raw.app.GUI.Main.Navigation.Adapters.BookmarksRVAdapter;
import com.example.raw.app.R;
import com.example.raw.app.Utils.FileWorker;
import com.example.raw.app.Utils.Repository;

import java.util.ArrayList;

public class BookmarksActivity extends Activity {

    private AlertDialog.Builder ad;
    private int countOfBookmarks = 0;
    private BookmarksRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        for (Book book : Repository.getInstance().getRecentBooks())
            if (book.getBookmarks().size() != 0)
                countOfBookmarks++;

        createDialog();
        createRecyclerView();
    }

    public void bookmarksActivityOnClick(View view) {
        switch (view.getId()) {
            case R.id.acBookmarksActionBack:
                finish();
                break;

            case R.id.acBookmarksActionRemoving:
                ad.show();
                break;
        }
    }

    private void removingBookmarks() {
        ArrayList<Book> recentBooks = Repository.getInstance().getRecentBooks();
        for (Book book : recentBooks)
            book.getBookmarks().clear();
        adapter.getBooks().clear();

        FileWorker.getInstance().refreshingJSON();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        adapter.getItemSelected(item);
        return super.onContextItemSelected(item);
    }

    private void createDialog() {
        ad = new AlertDialog.Builder(this);
        ad.setTitle(R.string.dialog_delete_bookmarks);
        ad.setMessage(R.string.dialog_confirmation_of_removal_bookmarks);
        ad.setPositiveButton(R.string.dialog_consent, (DialogInterface dialog, int arg1) -> {
            if (countOfBookmarks > 0) {
                removingBookmarks();
                Toast.makeText(getBaseContext(), R.string.dialog_bookmarks_cleared,
                        Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getBaseContext(), R.string.dialog_bookmarks_not_found,
                        Toast.LENGTH_SHORT).show();
        });
        ad.setNegativeButton(R.string.dialog_denial, (DialogInterface dialog, int arg1) -> {
        });
    }

    private void createRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.acBookmarksRecyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        ArrayList<Book> books = new ArrayList<>();
        for (Book book : Repository.getInstance().getRecentBooks())
            if (book.getBookmarks().size() > 0)
                books.add(book);

        if (books.size() == 0)
            ((TextView) findViewById(R.id.acBookmarksTvCount)).setText(R.string.dialog_bookmarks_not_found);

        adapter = new BookmarksRVAdapter(books, this);
        recyclerView.setAdapter(adapter);
    }
}
