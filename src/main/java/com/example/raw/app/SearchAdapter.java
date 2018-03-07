package com.example.raw.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {
    private ArrayList<Book> recentBooks;
    private ArrayList<Book> localBooks;
    private ArrayList<Book> filteredList;

    SearchAdapter(ArrayList<Book> recentBooks, ArrayList<Book> localBooks) {
        this.recentBooks = recentBooks;
        this.localBooks = localBooks;
        filteredList = new ArrayList<>();
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder viewHolder, int position) {
        viewHolder.bookName.setText(filteredList.get(position).getName());
        viewHolder.bookSize.setText(filteredList.get(position).getSize());
        //viewHolder.bookCover.setImageBitmap(filteredList.get(position).getCover());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String parsedString = charSequence.toString();

                if (parsedString.isEmpty()) {
                    filteredList = new ArrayList<>();
                } else{

                    ArrayList<Book> tempFilteredList = new ArrayList<>();

                    for (Book obj : recentBooks)
                        if (obj.getName().toLowerCase().contains(parsedString))
                            tempFilteredList.add(obj);
                    for (Book obj : localBooks)
                        if (obj.getName().toLowerCase().contains(parsedString))
                            tempFilteredList.add(obj);

                    filteredList = tempFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<Book>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView bookName;
        TextView bookSize;
        ImageView bookCover;

        ViewHolder(final View itemView) {
            super(itemView);

            bookName = itemView.findViewById(R.id.book_name);
            bookSize = itemView.findViewById(R.id.book_last_activity);
            bookCover = itemView.findViewById(R.id.book_cover);
        }
    }
}