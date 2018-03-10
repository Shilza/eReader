package com.example.raw.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public abstract class RVAdapter extends RecyclerView.Adapter<RVAdapter.BookViewHolder> {

    ArrayList<Book> books;
    Context context;
    Book selectedBook;


    RVAdapter(ArrayList<Book> books, Context context){
        this.books = books;
        this.context = context;
    }

    public abstract class BookViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener ,View.OnCreateContextMenuListener, View.OnClickListener{
        private TextView bookName;
        private TextView bookSize;
        private TextView bookLastActivity;
        private ImageView bookCover;

        ItemClickListener itemClickListener;

        BookViewHolder(final View itemView) {
            super(itemView);

            bookName = itemView.findViewById(R.id.book_name);
            bookSize = itemView.findViewById(R.id.book_size);
            bookLastActivity = itemView.findViewById(R.id.book_last_activity);
            bookCover = itemView.findViewById(R.id.book_cover);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public abstract void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo);

        private void setOnLongClickListener(ItemClickListener listener){
            this.itemClickListener = listener;
        }

        @Override
        public void onClick(View view){
            this.itemClickListener.onItemViewClick(getLayoutPosition(), false);
        }

        @Override
        public boolean onLongClick(View view){
            this.itemClickListener.onItemViewClick(getLayoutPosition(), true);
            return false;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public abstract BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int i);

    @Override
    public int getItemCount() {
        return books.size();
    }

    public abstract void getItemSelected(MenuItem item);

    void bookOpening(){
        if(FileWorker.getInstance().isBookExist(selectedBook.getFilePath())){
            BookOpener.getInstance().opening(selectedBook, context);
        }else{
            Toast.makeText(context, "Невозможно открыть, возможно книга была удалена", Toast.LENGTH_SHORT).show();
            books.remove(selectedBook);
            FileWorker.getInstance().refreshingJSON(books);
            TabKeeper.getInstance().notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(final BookViewHolder bookViewHolder, int position) {
        bookViewHolder.bookName.setText(books.get(position).getName());
        bookViewHolder.bookSize.setText(books.get(position).getSize());
        //bookViewHolder.bookCover.setImageBitmap(bitmap);
        bookViewHolder.bookCover.setImageResource(R.drawable.e);
        //R.drawable.e //default cover

        bookViewHolder.bookLastActivity.setText(books.get(position).getLastActivity());

        bookViewHolder.setOnLongClickListener(new ItemClickListener() {
            @Override
            public void onItemViewClick(int pos, boolean isLongClick) {
                selectedBook = books.get(pos);
                Toast.makeText(context, selectedBook.getName(), Toast.LENGTH_SHORT).show();

                if(!isLongClick){
                    if(!FileWorker.getInstance().getRecentBooks().contains(selectedBook)){
                        FileWorker.getInstance().addingToRecentBooks(selectedBook);
                        FileWorker.getInstance().removeBookFromLocalBooks(selectedBook);
                    } else{
                        Book book = books.remove(books.indexOf(selectedBook));
                        FileWorker.getInstance().addingToRecentBooks(book);
                    }

                    TabKeeper.getInstance().notifyDataSetChanged();
                    bookOpening();
                }
            }
        });
    }
}