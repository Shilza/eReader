package com.example.raw.app;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.BookViewHolder> {

    public static class BookViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        TextView bookName;
        TextView bookSize;
        ImageView bookCover;

        BookViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            bookName = itemView.findViewById(R.id.book_name);
            bookSize = itemView.findViewById(R.id.book_path);
            bookCover = itemView.findViewById(R.id.book_cover);
        }
    }

    List<Book> books;


    RVAdapter(List<Book> books){
        this.books = books;
    }

    //RecyclerView rv;
    //TextView tx;
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //rv = recyclerView;
        //tx = rv.findViewById(R.id.textrv);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        BookViewHolder pvh = new BookViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(BookViewHolder bookViewHolder, int i) {
        bookViewHolder.bookName.setText(books.get(i).name);
        bookViewHolder.bookSize.setText(books.get(i).size);
        bookViewHolder.bookCover.setImageResource(books.get(i).coverId);


        bookViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //tx.setText(String.valueOf(rv.indexOfChild(v)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}