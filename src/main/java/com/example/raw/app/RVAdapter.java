package com.example.raw.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
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

    void bookOpening(){
        if(FileWorker.isBookExist(selectedBook.getFilePath())){
            Intent intent = new Intent(context, PDFViewer.class);
            intent.putExtra("Book", selectedBook.getFilePath());
            context.startActivity(intent);
        }else{
            Toast.makeText(context, "Невозможно открыть, возможно книга была удалена", Toast.LENGTH_SHORT).show();
            books.remove(selectedBook);
            FileWorker.refreshingJSON(books);
            parent.dataSetChanging();
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
                    FileWorker.exportRecentBooksToJSON(selectedBook);
                    TabKeeper.notifyDataSetChanging();
                    bookOpening();
                }
            }
        });
    }
}