package com.example.raw.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public abstract class RVAdapter extends RecyclerView.Adapter<RVAdapter.BookViewHolder> {

    ArrayList<Book> books;
    Context context;
    Tab parent;
    Book selectedBook;

    RVAdapter(ArrayList<Book> books, Context context, Tab parent){
        this.books = books;
        this.context = context;
        this.parent = parent;
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

    @Override
    public void onBindViewHolder(final BookViewHolder bookViewHolder, int position) {
        bookViewHolder.bookName.setText(books.get(position).getName());
        bookViewHolder.bookSize.setText(books.get(position).getSize());
        bookViewHolder.bookCover.setImageResource(books.get(position).getCoverId());

        if(books.get(position).getLastActivity()!=0){
            SimpleDateFormat sdf;

            if(books.get(position).getLastActivity()+86400000 > (new Date().getTime())) //60*60*24*1000 one day
                sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
            else
                sdf = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
            String formattedDate = sdf.format(books.get(position).getLastActivity());

            bookViewHolder.bookLastActivity.setText(formattedDate);
        }

        bookViewHolder.setOnLongClickListener(new ItemClickListener() {
            @Override
            public void onItemViewClick(int pos, boolean isLongClick) {
                selectedBook = books.get(pos);
                Toast.makeText(context, selectedBook.getName(), Toast.LENGTH_SHORT).show();

                if(!isLongClick){
                    FileWorker.exportToJSON(selectedBook);
                    TabKeeper.notifyDataSetChanging();
                }
            }
        });
    }
}