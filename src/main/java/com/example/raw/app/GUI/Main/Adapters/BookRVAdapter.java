package com.example.raw.app.GUI.Main.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.raw.app.Entities.Book;
import com.example.raw.app.GUI.ItemClickListener;
import com.example.raw.app.GUI.Main.PropertiesActivity;
import com.example.raw.app.GUI.RVMediator;
import com.example.raw.app.R;
import com.example.raw.app.GUI.RVContextViewHolder;
import com.example.raw.app.Utils.BookOpener;
import com.example.raw.app.Utils.FileWorker;
import com.example.raw.app.Utils.Repository;

import java.util.ArrayList;

public abstract class BookRVAdapter extends RecyclerView.Adapter<BookRVAdapter.BookViewHolder>
        implements ContextMenuSelect {

    ArrayList<Book> books;
    Context context;
    Book selectedBook;

    BookRVAdapter(ArrayList<Book> books, Context context) {
        this.books = books;
        this.context = context;
    }

    abstract class BookViewHolder extends RVContextViewHolder {

        private TextView bookName;
        private TextView bookSize;
        private TextView bookLastActivity;
        private ImageView bookCover;

        BookViewHolder(final View itemView) {
            super(itemView);

            bookName = itemView.findViewById(R.id.acMainBookName);
            bookSize = itemView.findViewById(R.id.acMainBookSize);
            bookLastActivity = itemView.findViewById(R.id.acMainTabsLastActivity);
            bookCover = itemView.findViewById(R.id.acMainTabsBookCover);
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
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int position) {
        bookViewHolder.bookName.setText(books.get(position).getName());
        bookViewHolder.bookSize.setText(books.get(position).getSize());

        Glide.with(context)
                .load(FileWorker.getInstance().getPicturesPath() + books.get(position).getName())
                .apply(new RequestOptions().fitCenter().placeholder(R.drawable.e))
                .into(bookViewHolder.bookCover);

        bookViewHolder.bookLastActivity.setText(books.get(position).getLastActivity());

        bookViewHolder.setOnLongClickListener((int pos, boolean isLongClick) -> {
            selectedBook = books.get(pos);

            if (!isLongClick)
                clickProcessing();
        });
    }

    @Override
    public void bookOpening() {
        if (FileWorker.getInstance().isBookExist(selectedBook.getFilePath()))
            BookOpener.getInstance().opening(selectedBook, context);
        else {
            Toast.makeText(context, R.string.error_book_is_deleted, Toast.LENGTH_SHORT).show();
            books.remove(selectedBook);

            FileWorker.getInstance().refreshingJSON();
            RVMediator.getInstance().notifyDataSetChanged();
        }
    }

    @Override
    public void openProperties() {
        Intent intent = new Intent(context, PropertiesActivity.class);
        intent.putExtra("Book", selectedBook);
        context.startActivity(intent);
    }

    void moveBookToTheFirstPlace() {
        Repository.getInstance().unfix();
        selectedBook.setAffixed(true);

        Book book = Repository.getInstance().getRecentBooks().remove(
                Repository.getInstance().getRecentBooks().indexOf(selectedBook));
        Repository.getInstance().addToRecentBooks(book);
    }

    private void clickProcessing() {
        if (!Repository.getInstance().getRecentBooks().contains(selectedBook)) {
            Repository.getInstance().addToRecentBooks(selectedBook);
            Repository.getInstance().removeBookFromLocalBooks(selectedBook);
        } else {
            Book book = Repository.getInstance().getRecentBooks().remove(
                    Repository.getInstance().getRecentBooks().indexOf(selectedBook));
            Repository.getInstance().addToRecentBooks(book);
        }

        RVMediator.getInstance().notifyDataSetChanged();
        bookOpening();
    }

}