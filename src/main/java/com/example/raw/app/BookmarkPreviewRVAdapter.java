package com.example.raw.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.raw.app.Entities.Book;
import com.example.raw.app.Entities.Bookmark;

import java.util.ArrayList;

public class BookmarkPreviewRVAdapter extends RecyclerView.Adapter<BookmarkPreviewRVAdapter.ViewHolder>{

    private Book book;
    private ArrayList<Bookmark> bookmarks;
    private Context context;
    private AlertDialog.Builder ad;

    private final byte CONTEXT_MENU_OPEN = 0;
    private final byte CONTEXT_MENU_REMOVING = 1;
    private final byte GROUP_ID = 4;

    BookmarkPreviewRVAdapter(Book book, Context context){
        this.book = book;
        this.bookmarks = book.getBookmarks();
        this.context = context;
        initAlertDialog();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener ,View.OnCreateContextMenuListener, View.OnClickListener{
        TextView page;
        TextView text;
        ItemClickListener itemClickListener;

        ViewHolder(final View itemView) {
            super(itemView);

            page = itemView.findViewById(R.id.bookmark_preview_page);
            text = itemView.findViewById(R.id.bookmark_preview_text);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Закладка");
            menu.add(GROUP_ID, CONTEXT_MENU_OPEN, 0, "Открыть");
            menu.add(GROUP_ID, CONTEXT_MENU_REMOVING, 0, "Очистить закладки");
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
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bookmark_preview_rv_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    private void initAlertDialog(){
        ad = new AlertDialog.Builder(context);
        ad.setTitle("Удалить");
        ad.setMessage("Действительно хотите удалить эту закладку?");
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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.page.setText(String.valueOf(bookmarks.get(position).getPage()));
        holder.text.setText(bookmarks.get(position).getText());
    }

    void getItemSelected(MenuItem item){
        if(item.getGroupId() != GROUP_ID)
            return;

        switch (item.getItemId()){
            case CONTEXT_MENU_OPEN:
                //TODO
                break;

            case CONTEXT_MENU_REMOVING:
                ad.show();
                break;
        }
    }

}
