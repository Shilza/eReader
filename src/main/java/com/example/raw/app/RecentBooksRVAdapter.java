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

class RecentBooksRVAdapter extends RVAdapter{

    private final byte CONTEXT_MENU_OPEN = 0;
    private final byte CONTEXT_MENU_BOOKMARKS = 1;
    private final byte CONTEXT_MENU_DELETE_FROM_LIST = 2;
    private final byte CONTEXT_MENU_DELETE = 3;
    private final byte CONTEXT_MENU_PROPERTIES = 4;
    private final byte GROUP_ID = 0;

    RecentBooksRVAdapter(ArrayList<Book> books, Context context, Tab parent){
        super(books, context, parent);
    }

    private void bookRemoving(Book book){
        books.remove(book);
        FileWorker.refreshingJSON(books);
        parent.dataSetChanging();
    }

    @Override
    public void getItemSelected(MenuItem item){
        if(item.getGroupId() != GROUP_ID)
            return;

        switch (item.getItemId()){
            case CONTEXT_MENU_OPEN:
                bookOpening();
                break;
            case CONTEXT_MENU_BOOKMARKS:
                //TODO
                break;
            case CONTEXT_MENU_DELETE_FROM_LIST:
                bookRemoving(selectedBook);
                break;
            case CONTEXT_MENU_DELETE:
                //TODO
                bookRemoving(selectedBook);
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

    class RecentBooksViewHolder extends BookViewHolder{

        RecentBooksViewHolder(View view){
            super(view);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle(selectedBook.getName());
            menu.add(GROUP_ID, CONTEXT_MENU_OPEN, 0, "Открыть");
            menu.add(GROUP_ID, CONTEXT_MENU_BOOKMARKS, 0, "Закладки");
            menu.add(GROUP_ID, CONTEXT_MENU_DELETE_FROM_LIST, 0, "Удалить из списка");
            menu.add(GROUP_ID, CONTEXT_MENU_DELETE, 0, "Удалить");
            menu.add(GROUP_ID, CONTEXT_MENU_PROPERTIES, 0, "Свойства");
        }
    }

    @Override
    public RecentBooksViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new RecentBooksViewHolder(view);
    }
}
