package com.example.raw.app.Main.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.raw.app.Entities.Book;
import com.example.raw.app.ItemClickListener;
import com.example.raw.app.Main.PropertiesActivity;
import com.example.raw.app.R;
import com.example.raw.app.TabsKeeper;
import com.example.raw.app.Utils.BookOpener;
import com.example.raw.app.Utils.FileWorker;

import java.util.ArrayList;

public abstract class RVAdapter extends RecyclerView.Adapter<RVAdapter.BookViewHolder> {

    ArrayList<Book> books;
    Context context;
    Book selectedBook;
    AlertDialog.Builder ad;

    RVAdapter(ArrayList<Book> books, Context context){
        this.books = books;
        this.context = context;
        initAlertDialog();
    }

    public abstract class BookViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener ,View.OnCreateContextMenuListener, View.OnClickListener{
        private TextView bookName;
        private TextView bookSize;
        private TextView bookLastActivity;
        private ImageView bookCover;

        ItemClickListener itemClickListener;

        BookViewHolder(final View itemView) {
            super(itemView);

            bookName = itemView.findViewById(R.id.acMainBookName);
            bookSize = itemView.findViewById(R.id.acMainBookSize);
            bookLastActivity = itemView.findViewById(R.id.acMainTabsLastActivity);
            bookCover = itemView.findViewById(R.id.acMainTabsBookCover);

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
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
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
    public void onBindViewHolder(@NonNull final BookViewHolder bookViewHolder, int position) {
        bookViewHolder.bookName.setText(books.get(position).getName());
        bookViewHolder.bookSize.setText(books.get(position).getSize());

        Glide.with(context)
                .load(FileWorker.getInstance().getPicturesPath() + books.get(position).getName())
                .apply(new RequestOptions().fitCenter().placeholder(R.drawable.e))
                .into(bookViewHolder.bookCover);

        bookViewHolder.bookLastActivity.setText(books.get(position).getLastActivity());

        bookViewHolder.setOnLongClickListener(new ItemClickListener() {
            @Override
            public void onItemViewClick(int pos, boolean isLongClick) {
                selectedBook = books.get(pos);
                Toast.makeText(context, selectedBook.getName(), Toast.LENGTH_SHORT).show();

                if(!isLongClick)
                   clickProcessing();
            }
        });
    }

    void bookOpening(){
        if(FileWorker.getInstance().isBookExist(selectedBook.getFilePath()))
            BookOpener.getInstance().opening(selectedBook, context);
        else {
            Toast.makeText(context, R.string.error_book_is_deleted, Toast.LENGTH_SHORT).show();
            books.remove(selectedBook);
            FileWorker.getInstance().refreshingJSON(books);
            TabsKeeper.getInstance().notifyDataSetChanged();
        }
    }

    void openProperties(){
        Intent intent = new Intent(context, PropertiesActivity.class);
        intent.putExtra("Book", selectedBook);
        context.startActivity(intent);
    }

    private void initAlertDialog(){
        ad = new AlertDialog.Builder(context);
        ad.setTitle(R.string.dialog_delete_book);
        ad.setMessage(R.string.dialog_confirmation_of_removal_book);
        ad.setPositiveButton(R.string.dialog_consent, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

                //context.deleteFile(selectedBook.getName());
                Toast.makeText(context, "Вы сделали правильный выбор",
                        Toast.LENGTH_SHORT).show();
            }
        });
        ad.setNegativeButton(R.string.dialog_denial, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(context, "Возможно вы правы", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickProcessing(){
        if(!FileWorker.getInstance().getRecentBooks().contains(selectedBook)){
            FileWorker.getInstance().addToRecentBooks(selectedBook);
            FileWorker.getInstance().removeBookFromLocalBooks(selectedBook);
        } else{
            Book book = FileWorker.getInstance().getRecentBooks().remove(
                    FileWorker.getInstance().getRecentBooks().indexOf(selectedBook));
            FileWorker.getInstance().addToRecentBooks(book);
        }

        TabsKeeper.getInstance().notifyDataSetChanged();
        bookOpening();
    }
}