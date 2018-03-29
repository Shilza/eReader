package com.example.raw.app.Main.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.raw.app.Main.ContextMenuProperties;
import com.example.raw.app.Entities.Book;
import com.example.raw.app.R;
import com.example.raw.app.TabsKeeper;
import com.example.raw.app.Utils.FileWorker;

import java.util.ArrayList;

public class RecentBooksRVAdapter extends RVAdapter{

    private AlertDialog.Builder deleteFromListDialog;

    private final byte CONTEXT_MENU_OPEN = 0;
    private final byte CONTEXT_MENU_DELETE_FROM_LIST = 1;
    private final byte CONTEXT_MENU_DELETE = 2;
    private final byte CONTEXT_MENU_PROPERTIES = 3;
    private final byte GROUP_ID = 1;

    public RecentBooksRVAdapter(ArrayList<Book> books, Context context){
        super(books, context);
        initAlertDialog();
    }

    private void bookRemoving(Book book){
        books.remove(book);
        FileWorker.getInstance().refreshingJSON(books);
        TabsKeeper.getInstance().notifyDataSetChanged();
    }

    @Override
    public void getItemSelected(MenuItem item){
        if(item.getGroupId() != GROUP_ID)
            return;

        switch (item.getItemId()){
            case CONTEXT_MENU_OPEN:
                bookOpening();
                break;

            case CONTEXT_MENU_DELETE_FROM_LIST:
                deleteFromListDialog.show();
                break;

            case CONTEXT_MENU_DELETE:
                //TODO
                ad.show();
                //bookRemoving(selectedBook);
                break;

            case CONTEXT_MENU_PROPERTIES:
                Intent intent = new Intent(context, ContextMenuProperties.class);
                intent.putExtra("Book", selectedBook);
                context.startActivity(intent);
                break;
        }
    }

    private void initAlertDialog(){
        deleteFromListDialog = new AlertDialog.Builder(context);
        deleteFromListDialog.setTitle("Удалить из списка");
        deleteFromListDialog.setMessage("Действительно хотите удалить эту книгу?\nСтатистика и закладки будут очищены");
        deleteFromListDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                bookRemoving(selectedBook);
            }
        });
        deleteFromListDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
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
            menu.add(GROUP_ID, CONTEXT_MENU_DELETE_FROM_LIST, 0, "Удалить из списка");
            menu.add(GROUP_ID, CONTEXT_MENU_DELETE, 0, "Удалить");
            menu.add(GROUP_ID, CONTEXT_MENU_PROPERTIES, 0, "Свойства");
        }
    }

    @Override
    public RecentBooksViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item, viewGroup, false);
        return new RecentBooksViewHolder(view);
    }
}
