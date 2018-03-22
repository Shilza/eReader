package com.example.raw.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.raw.app.Entities.Book;

public class BookmarksActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        int count = 0;
        for(Book book : FileWorker.getInstance().getRecentBooks())
            if(book.getBookmarks().size() != 0)
                count++;

        ((TextView) findViewById(R.id.tv_count)).setText("Количество книг, имеющих закладки " + count);
    }

    public void bookmarksActivityOnClick(View view){
        switch (view.getId()){
            case R.id.bookmarks_activity_action_back:
                finish();
                break;

            case R.id.bookmarks_activity_action_removing:
                removingBookmarks();
                break;
        }
    }

    private void removingBookmarks(){
        //TODO
    }
}
