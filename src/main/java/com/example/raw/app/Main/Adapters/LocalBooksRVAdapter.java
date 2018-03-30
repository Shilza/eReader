package com.example.raw.app.Main.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.raw.app.Main.PropertiesActivity;
import com.example.raw.app.Entities.Book;
import com.example.raw.app.R;

import java.util.ArrayList;

public class LocalBooksRVAdapter extends RVAdapter{

    private final byte CONTEXT_MENU_OPEN = 0;
    private final byte CONTEXT_MENU_DELETE = 1;
    private final byte CONTEXT_MENU_PROPERTIES = 2;
    private final byte GROUP_ID = 0;

    public LocalBooksRVAdapter(ArrayList<Book> books, Context context){
        super(books, context);
    }

    @Override
    public void getItemSelected(MenuItem item){
        if(item.getGroupId() != GROUP_ID)
            return;

        switch (item.getItemId()){
            case CONTEXT_MENU_OPEN:
                bookOpening();
                break;

            case CONTEXT_MENU_DELETE:
                ad.show();
                break;

            case CONTEXT_MENU_PROPERTIES:
                Intent intent = new Intent(context, PropertiesActivity.class);
                intent.putExtra("Book", selectedBook);
                context.startActivity(intent);
                break;
        }
    }

    class LocalBooksViewHolder extends RVAdapter.BookViewHolder {

        LocalBooksViewHolder(View view){
            super(view);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle(selectedBook.getName());
            menu.add(GROUP_ID, CONTEXT_MENU_OPEN, 0, "Открыть");
            menu.add(GROUP_ID, CONTEXT_MENU_DELETE, 0, "Удалить");
            menu.add(GROUP_ID, CONTEXT_MENU_PROPERTIES, 0, "Свойства");
        }
    }

    @Override
    public LocalBooksRVAdapter.LocalBooksViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adt_rv_main_tabs, viewGroup, false);
        return new LocalBooksRVAdapter.LocalBooksViewHolder(view);
    }
}
