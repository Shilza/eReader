package com.example.raw.app;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class LocalBooksRVAdapter extends RVAdapter{

    private final byte CONTEXT_MENU_OPEN = 0;
    private final byte CONTEXT_MENU_DELETE = 1;
    private final byte CONTEXT_MENU_PROPERTIES = 2;

    LocalBooksRVAdapter(ArrayList<Book> books, Context context, Tab parent){
        super(books, context, parent);
    }

    @Override
    public void getItemSelected(MenuItem item){
        switch (item.getItemId()){
            case CONTEXT_MENU_OPEN:
                //TODO
                /*
                if(FileWorker.isBookExist(selectedBook.getFilePath())){
                    //todo
                }else{
                    Toast.makeText(context, "Невозможно открыть, возможно книга была удалена", Toast.LENGTH_SHORT).show();
                    books.remove(selectedBook);
                    FileWorker.refreshingLocalBooksJSON();
                    parent.dataSetChanging();
                }
                */
                break;
            case CONTEXT_MENU_DELETE:
                //TODO
                //FileWorker.refreshingLocalBooksJSON();
                break;
            case CONTEXT_MENU_PROPERTIES:
                Intent intent = new Intent(context, ContextMenuProperties.class);
                intent.putExtra("Book", selectedBook);
                context.startActivity(intent);
                break;
            default:
                break;
        }
    }

    class LocalBooksViewHolder extends RVAdapter.BookViewHolder {

        LocalBooksViewHolder(View view){
            super(view);
        }

        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle(selectedBook.getName());
            menu.add(0, CONTEXT_MENU_OPEN, 0, "Открыть");
            menu.add(0, CONTEXT_MENU_DELETE, 0, "Удалить");
            menu.add(0, CONTEXT_MENU_PROPERTIES, 0, "Свойства");
        }
    }

    @Override
    public LocalBooksRVAdapter.LocalBooksViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new LocalBooksRVAdapter.LocalBooksViewHolder(view);
    }
}
