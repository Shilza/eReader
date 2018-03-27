package com.example.raw.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.raw.app.Entities.Bookmark;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BookmarkOfParticularBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Bookmark> bookmarks;
    private Context context;
    private Bookmark selectedBookmark;
    private AlertDialog.Builder ad;

    private final byte CONTEXT_MENU_GOTO = 0;
    private final byte CONTEXT_MENU_REMOVING = 1;
    private final byte GROUP_ID = 4;

    BookmarkOfParticularBookAdapter(ArrayList<Bookmark> bookmarks, Context context){
        this.bookmarks = bookmarks;
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

            menu.add(GROUP_ID, CONTEXT_MENU_GOTO, 0, "Перейти");
            menu.add(GROUP_ID, CONTEXT_MENU_REMOVING, 0, "Удалить закладку");
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

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v1 = inflater.inflate(R.layout.bookmarks_particular_book_item, viewGroup, false);

        return (new BookmarkOfParticularBookAdapter.ViewHolder(v1));
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
                bookmarks.remove(selectedBookmark);
                notifyDataSetChanged();
            }
        });
        ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.tvPage.setText("Страница " + String.valueOf(bookmarks.get(position).getPage()));
        viewHolder.tvText.setText(bookmarks.get(position).getText());

        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
        String date = sdf.format(bookmarks.get(position).getUploadDate());
        viewHolder.tvDate.setText(date);

        viewHolder.setOnLongClickListener(new ItemClickListener() {
            @Override
            public void onItemViewClick(int pos, boolean isLongClick) {
                selectedBookmark = bookmarks.get(pos);

                if (!isLongClick) {
                   //TODO
                }
            }
        });
    }

    void getItemSelected(MenuItem item){
        if(item.getGroupId() != GROUP_ID)
            return;

        switch (item.getItemId()){
            case CONTEXT_MENU_GOTO:
                break;

            case CONTEXT_MENU_REMOVING:
                ad.show();
                break;
        }
    }

}
