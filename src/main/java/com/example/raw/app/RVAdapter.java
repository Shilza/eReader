package com.example.raw.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    private void initAlertDialog(){
        ad = new AlertDialog.Builder(context);
        ad.setTitle("Удалить");
        ad.setMessage("Действительно хотите удалить эту книгу?");
        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(context, "Вы сделали правильный выбор",
                        Toast.LENGTH_SHORT).show();
            }
        });
        ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(context, "Возможно вы правы", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void bookOpening(){
        if(FileWorker.getInstance().isBookExist(selectedBook.getFilePath()))
            BookOpener.getInstance().opening(selectedBook, context);
        else {
            Toast.makeText(context, "Невозможно открыть, возможно книга была удалена", Toast.LENGTH_SHORT).show();
            books.remove(selectedBook);
            FileWorker.getInstance().refreshingJSON(books);
            TabKeeper.getInstance().notifyDataSetChanged();
        }
    }

    private void clickProcessing(){
        if(!FileWorker.getInstance().getRecentBooks().contains(selectedBook)){
            FileWorker.getInstance().addingToRecentBooks(selectedBook);
            FileWorker.getInstance().removeBookFromLocalBooks(selectedBook);
        } else{
            Book book = FileWorker.getInstance().getRecentBooks().remove(
                    FileWorker.getInstance().getRecentBooks().indexOf(selectedBook));
            FileWorker.getInstance().addingToRecentBooks(book);
        }

        TabKeeper.getInstance().notifyDataSetChanged();
        bookOpening();
    }

    @Override
    public void onBindViewHolder(final BookViewHolder bookViewHolder, int position) {
        bookViewHolder.bookName.setText(books.get(position).getName());
        bookViewHolder.bookSize.setText(books.get(position).getSize());

        Glide.with(context)
                .load(FileWorker.getInstance().getPicturesPath() + books.get(position).getName()+".png")
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
}