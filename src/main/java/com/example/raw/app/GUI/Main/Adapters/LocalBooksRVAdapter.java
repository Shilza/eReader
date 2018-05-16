package com.example.raw.app.GUI.Main.Adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.R;

import java.util.ArrayList;

public class LocalBooksRVAdapter extends BookRVAdapter {

    private final byte GROUP_ID = 0;

    public LocalBooksRVAdapter(ArrayList<Book> books, Context context) {
        super(books, context);
    }

    @Override
    public void getItemSelected(MenuItem item) {
        if (item.getGroupId() != GROUP_ID)
            return;

        itemSelectedProcessing(item);
    }

    class LocalBooksViewHolder extends BookRVAdapter.BookViewHolder {

        LocalBooksViewHolder(View view){
            super(view);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle(selectedBook.getName());
            menu.add(GROUP_ID, CONTEXT_MENU_OPEN, 0, R.string.context_menu_open);
            menu.add(GROUP_ID, CONTEXT_MENU_PROPERTIES, 0, R.string.context_menu_properties);
        }
    }

    @Override
    public LocalBooksViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adt_rv_main_tabs, viewGroup, false);
        return new LocalBooksViewHolder(view);
    }
}
