package com.example.raw.app.GUI.Main.Adapters;

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
import com.example.raw.app.Utils.Repository;

import java.util.ArrayList;

public class SearchRVAdapter extends BookRVAdapter implements Filterable {

    private final byte GROUP_ID = 2;

    public SearchRVAdapter(Context context) {
        super(new ArrayList<>(), context);
    }

    @Override
    public void getItemSelected(MenuItem item) {
        if (item.getGroupId() != GROUP_ID)
            return;

        itemSelectedProcessing(item);
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

            private void queryProcessing(String parsedString) {
                books.clear();
                for (Book obj : Repository.getInstance().getRecentBooks())
                    if (obj.getName().toLowerCase().contains(parsedString) && !books.contains(obj))
                        books.add(obj);

                for (Book obj : Repository.getInstance().getLocalBooks())
                    if (obj.getName().toLowerCase().contains(parsedString) && !books.contains(obj))
                        books.add(obj);

            }
        };
    }

    class ViewHolder extends BookRVAdapter.BookViewHolder {
        ViewHolder(View view) {
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
    public SearchRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adt_rv_main_tabs, viewGroup, false);
        return new SearchRVAdapter.ViewHolder(view);
    }
}