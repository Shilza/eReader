package com.example.raw.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.BookViewHolder> {

    private ArrayList<Book> books;
    private Context context;
    private String contextMenuHeaderTitleName;

    private final byte CONTEXT_MENU_OPEN_ID = 0;
    private final byte CONTEXT_MENU_DELETE = 1;
    private final byte CONTEXT_MENU_PROPERTIES = 2;
    private final byte CONTEXT_MENU_FIX = 3;

    RVAdapter(ArrayList<Book> books, Context context){
        this.books = books;
        this.context = context;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener ,View.OnCreateContextMenuListener{

        TextView bookName;
        TextView bookSize;
        ImageView bookCover;

        LongClickListener longClickListener;

        BookViewHolder(final View itemView) {
            super(itemView);

            bookName = itemView.findViewById(R.id.book_name);
            bookSize = itemView.findViewById(R.id.book_path);
            bookCover = itemView.findViewById(R.id.book_cover);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle(contextMenuHeaderTitleName);
            menu.add(0, CONTEXT_MENU_OPEN_ID, 0, "Открыть");
            menu.add(0, CONTEXT_MENU_FIX, 0, "Закрепить");
            menu.add(0, CONTEXT_MENU_PROPERTIES, 0, "Свойства");
            menu.add(0, CONTEXT_MENU_DELETE, 0, "Удалить");
        }

        public void setOnLongClickListener(LongClickListener listener){
            this.longClickListener = listener;
        }

        @Override
        public boolean onLongClick(View view){
            this.longClickListener.onItemViewClick(getLayoutPosition());
            return false;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookViewHolder bookViewHolder, int i) {
        bookViewHolder.bookName.setText(books.get(i).getName());
        bookViewHolder.bookSize.setText(books.get(i).getSize());
        bookViewHolder.bookCover.setImageResource(books.get(i).getCoverId());

        bookViewHolder.setOnLongClickListener(new LongClickListener() {
            @Override
            public void onItemViewClick(int pos) {
                contextMenuHeaderTitleName = books.get(pos).getName();
                Toast.makeText(context, contextMenuHeaderTitleName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void getItemSelected(MenuItem item){
        switch (item.getItemId()){
            case CONTEXT_MENU_OPEN_ID:
                Intent intent = new Intent(context, ContextMenuProperties.class);
                for(Book a : books){
                    if(a.getName().equals(contextMenuHeaderTitleName))
                        intent.putExtra("Book", a);
                }
                context.startActivity(intent);
                break;
            case CONTEXT_MENU_DELETE:
                for(Book a : books){
                    if(a.getName().equals(contextMenuHeaderTitleName))
                        FileWorker.JSONWorker.exportToJSON(a);
                }
            default:
                break;
        }
    }
}