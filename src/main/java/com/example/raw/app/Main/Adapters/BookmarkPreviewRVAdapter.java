package com.example.raw.app.Main.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.Entities.Bookmark;
import com.example.raw.app.ItemClickListener;
import com.example.raw.app.Main.BookmarksActivity;
import com.example.raw.app.R;
import com.example.raw.app.Utils.BookOpener;

import java.util.ArrayList;

public class BookmarkPreviewRVAdapter extends RecyclerView.Adapter<BookmarkPreviewRVAdapter.ViewHolder>{

    private Book book;
    private ArrayList<Bookmark> bookmarks;
    private Context context;

    BookmarkPreviewRVAdapter(Book book, Context context){
        this.book = book;
        this.bookmarks = book.getBookmarks();
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{
        TextView page;
        TextView text;
        ItemClickListener itemClickListener;

        ViewHolder(final View itemView) {
            super(itemView);

            page = itemView.findViewById(R.id.acBookmarksPreviewPage);
            text = itemView.findViewById(R.id.acBookmarksPreviewText);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adt_rv_bookmark_previev, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        int page = bookmarks.get(position).getPage()+1;
        holder.page.setText(String.valueOf(page));
        holder.text.setText(bookmarks.get(position).getText());

        holder.setOnLongClickListener(new ItemClickListener() {
            @Override
            public void onItemViewClick(int pos, boolean isLongClick) {
                ((BookmarksActivity) context).finish();
                BookOpener.getInstance().opening(book, bookmarks.get(pos).getPage(), context);
            }
        });
    }

}
