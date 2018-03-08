package com.example.raw.app;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchAdapter extends RVAdapter implements Filterable {

    private ArrayList<Book> allBooks;
    private final byte CONTEXT_MENU_OPEN = 0;
    private final byte CONTEXT_MENU_DELETE = 1;
    private final byte CONTEXT_MENU_PROPERTIES = 2;
    private final byte GROUP_ID = 2;

    SearchAdapter(ArrayList<Book> books, Context context) {
        super(new ArrayList<Book>(), context, null);
        this.allBooks = books;
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

    void bookOpening(){
        if(FileWorker.isBookExist(selectedBook.getFilePath())){
            Intent intent = new Intent(context, PDFViewer.class);
            intent.putExtra("Book", selectedBook.getFilePath());
            context.startActivity(intent);
        }else{
            Toast.makeText(context, "Невозможно открыть, возможно книга была удалена", Toast.LENGTH_SHORT).show();
            allBooks.remove(selectedBook);
            books.remove(selectedBook);

            for(Book obj : FileWorker.getRecentBooks())
                if(obj.equals(selectedBook)) {
                    FileWorker.getRecentBooks().remove(obj);
                    break;
                }
            for(Book obj : FileWorker.getLocalBooks())
                if(obj.equals(selectedBook)) {
                    FileWorker.getLocalBooks().remove(obj);
                    break;
                }

            notifyDataSetChanged();
            TabKeeper.notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String parsedString = charSequence.toString();

                if (parsedString.isEmpty()) {
                    books = new ArrayList<>();
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
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new SearchAdapter.ViewHolder(view);
    }
}