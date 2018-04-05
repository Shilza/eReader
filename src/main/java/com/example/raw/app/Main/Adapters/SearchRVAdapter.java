package com.example.raw.app.Main.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.R;
import com.example.raw.app.Utils.FileWorker;

import java.util.ArrayList;

public class SearchRVAdapter extends RVAdapter implements Filterable {

    private final byte CONTEXT_MENU_OPEN = 0;
    private final byte CONTEXT_MENU_DELETE = 1;
    private final byte CONTEXT_MENU_PROPERTIES = 2;
    private final byte GROUP_ID = 2;

    public SearchRVAdapter(Context context) {
        super(new ArrayList<Book>(), context);
    }

    @Override
    public void getItemSelected(MenuItem item) {
        if (item.getGroupId() != GROUP_ID)
            return;

        switch (item.getItemId()) {
            case CONTEXT_MENU_OPEN:
                bookOpening();
                break;

            case CONTEXT_MENU_DELETE:
                //TODO
                ad.show();
                break;

            case CONTEXT_MENU_PROPERTIES:
                openProperties();
                break;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String parsedString = charSequence.toString().toLowerCase();
                queryProcessing(parsedString);

                FilterResults filterResults = new FilterResults();
                filterResults.values = books;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                books = (ArrayList<Book>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RVAdapter.BookViewHolder {
        ViewHolder(View view) {
            super(view);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle(selectedBook.getName());
            menu.add(GROUP_ID, CONTEXT_MENU_OPEN, 0, R.string.context_menu_open);
            menu.add(GROUP_ID, CONTEXT_MENU_DELETE, 0, R.string.context_menu_delete);
            menu.add(GROUP_ID, CONTEXT_MENU_PROPERTIES, 0, R.string.context_menu_properties);
        }
    }

    @Override
    public SearchRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adt_rv_main_tabs, viewGroup, false);
        return new SearchRVAdapter.ViewHolder(view);
    }

    private void queryProcessing(String parsedString) {
        books.clear();
        for (Book obj : FileWorker.getInstance().getRecentBooks())
            if (obj.getName().toLowerCase().contains(parsedString) && !books.contains(obj))
                books.add(obj);

        for (Book obj : FileWorker.getInstance().getLocalBooks())
            if (obj.getName().toLowerCase().contains(parsedString) && !books.contains(obj))
                books.add(obj);

    }
}