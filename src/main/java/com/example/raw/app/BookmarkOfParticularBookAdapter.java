package com.example.raw.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raw.app.Entities.Bookmark;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BookmarkOfParticularBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Pair<Integer, Bookmark>> bookmarks;
    private Context context;
    private Bookmark selectedBookmark;
    private AlertDialog.Builder ad;


    private final byte CONTEXT_MENU_REMOVING = 0;
    private final byte GROUP_ID = 4;


    public BookmarkOfParticularBookAdapter(ArrayList<Bookmark> bookmarks, Context context){
        this.bookmarks = new ArrayList<>();
        for(Bookmark bookmark : bookmarks)
            this.bookmarks.add(new Pair<>(0, bookmark));

        this.context = context;
        initAlertDialog();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener ,View.OnCreateContextMenuListener, View.OnClickListener{

        TextView tvPage;
        TextView tvText;
        TextView tvDate;
        ItemClickListener itemClickListener;

        ViewHolder(final View itemView) {
            super(itemView);

            tvPage = itemView.findViewById(R.id.bm_of_part_book_rv_item_page);
            tvText = itemView.findViewById(R.id.bm_of_part_book_rv_item_text);
            tvDate = itemView.findViewById(R.id.bm_of_part_book_rv_item_date);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.add(GROUP_ID, CONTEXT_MENU_REMOVING, 0, "Удалить");
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case 0:
                View v1 = inflater.inflate(R.layout.bookmarks_particular_book_item, viewGroup, false);
                viewHolder = new BookmarkOfParticularBookAdapter.ViewHolder(v1);
                break;

            case 1:
                //View v2 = inflater.inflate(R.layout.bookmarks_alt_rv_item, viewGroup, false);
                //viewHolder = new BookmarkOfParticularBookAdapter.BookmarksViewHolder1(v2);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    private void initAlertDialog(){
        ad = new AlertDialog.Builder(context);
        ad.setTitle("Удалить");
        ad.setMessage("Действительно хотите удалить эту закладку");
        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 0:
                BookmarkOfParticularBookAdapter.ViewHolder bookmarksViewHolder = (BookmarkOfParticularBookAdapter.ViewHolder) holder;

                bookmarksViewHolder.tvPage.setText("Страница " + String.valueOf(bookmarks.get(position).second.getPage()));
                bookmarksViewHolder.tvText.setText(bookmarks.get(position).second.getText());

                SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
                String date = sdf.format(bookmarks.get(position).second.getUploadDate());
                bookmarksViewHolder.tvDate.setText(date);

                bookmarksViewHolder.setOnLongClickListener(new ItemClickListener() {
                    @Override
                    public void onItemViewClick(int pos, boolean isLongClick) {
                        selectedBookmark = bookmarks.get(pos).second;
                        Toast.makeText(context, selectedBookmark.getPage(), Toast.LENGTH_SHORT).show();

                        if(!isLongClick){
                            //TODO
                        }
                    }
                });
                break;

            case 1:
                break;
        }
    }

    void getItemSelected(MenuItem item){
        if(item.getGroupId() != GROUP_ID)
            return;

        switch (item.getItemId()){
            case CONTEXT_MENU_REMOVING:
                ad.show();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return bookmarks.get(position).first == 0 ?  0 : 1;
    }

    public class BookmarksViewHolder1 extends RecyclerView.ViewHolder implements View.OnLongClickListener ,View.OnCreateContextMenuListener, View.OnClickListener{

        ItemClickListener itemClickListener;

        BookmarksViewHolder1(final View itemView) {
            super(itemView);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.add(GROUP_ID, CONTEXT_MENU_REMOVING, 0, "Удалить");
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
}
