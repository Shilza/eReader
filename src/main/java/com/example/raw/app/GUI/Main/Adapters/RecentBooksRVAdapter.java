package com.example.raw.app.GUI.Main.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.GUI.RVMediator;
import com.example.raw.app.R;
import com.example.raw.app.Utils.FileWorker;

import java.util.ArrayList;

public class RecentBooksRVAdapter extends BookRVAdapter {

    private AlertDialog.Builder deleteFromListDialog;

    private final byte CONTEXT_MENU_AFFIX = 2;
    private final byte CONTEXT_MENU_UNFIX = 3;
    private final byte CONTEXT_MENU_DELETE_FROM_LIST = 4;
    private final byte GROUP_ID = 1;

    public RecentBooksRVAdapter(ArrayList<Book> books, Context context) {
        super(books, context);
        initAlertDialog();
    }

    @Override
    public void getItemSelected(MenuItem item) {
        if (item.getGroupId() != GROUP_ID)
            return;

        itemSelectedProcessing(item);

        switch (item.getItemId()) {
            case CONTEXT_MENU_AFFIX:
                moveBookToTheFirstPlace();
                notifyDataSetChanged();
                break;

            case CONTEXT_MENU_UNFIX:
                selectedBook.setAffixed(false);
                notifyDataSetChanged();
                break;

            case CONTEXT_MENU_DELETE_FROM_LIST:
                deleteFromListDialog.show();
                break;
        }
    }

    class RecentBooksViewHolder extends BookViewHolder {

        RecentBooksViewHolder(View view) {
            super(view);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle(selectedBook.getName());
            menu.add(GROUP_ID, CONTEXT_MENU_OPEN, 0, R.string.context_menu_open);
            if (selectedBook.isAffixed())
                menu.add(GROUP_ID, CONTEXT_MENU_UNFIX, 0, R.string.context_menu_unfix);
            else
                menu.add(GROUP_ID, CONTEXT_MENU_AFFIX, 0, R.string.context_menu_affix);
            menu.add(GROUP_ID, CONTEXT_MENU_DELETE_FROM_LIST, 0, R.string.context_menu_delete_from_list);
            menu.add(GROUP_ID, CONTEXT_MENU_PROPERTIES, 0, R.string.context_menu_properties);
        }
    }

    @NonNull
    @Override
    public RecentBooksViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int viewID;
        if (viewType == 0)
            viewID = R.layout.adt_rv_main_tabs;
        else
            viewID = R.layout.adt_affixed_rv_main_tabs;

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewID, viewGroup, false);
        return new RecentBooksViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return books.get(position).isAffixed() ? 1 : 0;
    }

    private void bookRemoving(Book book) {
        int position = books.indexOf(book);
        books.remove(book);
        FileWorker.getInstance().refreshingJSON();
        RVMediator.getInstance().notifyItemRemoved(position);
    }

    private void initAlertDialog() {
        deleteFromListDialog = new AlertDialog.Builder(context);
        deleteFromListDialog.setTitle("Удалить из списка");
        deleteFromListDialog.setMessage("Действительно хотите удалить эту книгу?\nСтатистика и закладки будут очищены");
        deleteFromListDialog.setPositiveButton("Да", (DialogInterface dialog, int arg1) -> {
            bookRemoving(selectedBook);
        });
        deleteFromListDialog.setNegativeButton("Нет", (DialogInterface dialog, int arg1) -> {
        });
    }
}
