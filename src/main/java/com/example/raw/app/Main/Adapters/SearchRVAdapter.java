package com.example.raw.app.Main.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.example.raw.app.Main.PropertiesActivity;
import com.example.raw.app.Entities.Book;
import com.example.raw.app.R;
import com.example.raw.app.TabsKeeper;
import com.example.raw.app.Utils.BookOpener;
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
    public void getItemSelected(MenuItem item){
        if(item.getGroupId() != GROUP_ID)
            return;

        switch (item.getItemId()){
            case CONTEXT_MENU_OPEN:
                bookOpening();
                break;

            case CONTEXT_MENU_DELETE:
                //TODO
                ad.show();
                break;

            case CONTEXT_MENU_PROPERTIES:
                Intent intent = new Intent(context, PropertiesActivity.class);
                intent.putExtra("Book", selectedBook);
                context.startActivity(intent);
                break;
        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String parsedString = charSequence.toString();

                if (parsedString.isEmpty()) {
                    books.clear();
                } else{

                    ArrayList<Book> tempFilteredList = new ArrayList<>();

                    for (Book obj : FileWorker.getInstance().getRecentBooks())
                        if (obj.getName().toLowerCase().contains(parsedString))
                            tempFilteredList.add(obj);

                    for (Book obj : FileWorker.getInstance().getLocalBooks())
                        if (obj.getName().toLowerCase().contains(parsedString))
                            tempFilteredList.add(obj);

                    books = tempFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = books;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                books = (ArrayList<Book>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RVAdapter.BookViewHolder{
        ViewHolder(View view){
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
    public SearchRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adt_rv_main_tabs, viewGroup, false);
        return new SearchRVAdapter.ViewHolder(view);
    }
}