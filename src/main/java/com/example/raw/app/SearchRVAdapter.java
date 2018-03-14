package com.example.raw.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class SearchRVAdapter extends RVAdapter implements Filterable {

    private ArrayList<Book> allBooks;
    private final byte CONTEXT_MENU_OPEN = 0;
    private final byte CONTEXT_MENU_DELETE = 1;
    private final byte CONTEXT_MENU_PROPERTIES = 2;
    private final byte GROUP_ID = 2;

    SearchRVAdapter(ArrayList<Book> allBooks, Context context) {
        super(new ArrayList<Book>(), context);
        this.allBooks = allBooks;
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
                Intent intent = new Intent(context, ContextMenuProperties.class);
                intent.putExtra("Book", selectedBook);
                context.startActivity(intent);
                break;
            default:
                break;
        }
    }

    void bookOpening(){
        if(FileWorker.getInstance().isBookExist(selectedBook.getFilePath()))
            BookOpener.getInstance().opening(selectedBook, context);
        else{
            Toast.makeText(context, "Невозможно открыть, возможно книга была удалена", Toast.LENGTH_SHORT).show();
            books.remove(selectedBook);

            if(FileWorker.getInstance().getRecentBooks().contains(selectedBook))
                FileWorker.getInstance().getRecentBooks().remove(selectedBook);
            else if(FileWorker.getInstance().getLocalBooks().contains(selectedBook))
                FileWorker.getInstance().getLocalBooks().remove(selectedBook);

            notifyDataSetChanged();
            TabKeeper.getInstance().notifyDataSetChanged();
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

                    for (Book obj : allBooks)
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
    public SearchRVAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new SearchRVAdapter.ViewHolder(view);
    }
}