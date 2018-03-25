package com.example.raw.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.raw.app.Entities.Book;
import com.example.raw.app.Utils.FileWorker;

import java.util.ArrayList;

public class BookmarksRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Pair<Integer, Book>> books;
    private Context context;
    private Book selectedBook;
    private AlertDialog.Builder ad;

    RecyclerView rvBookmarkPreview;

    private final byte CONTEXT_MENU_OPEN = 0;
    private final byte CONTEXT_MENU_REMOVING = 1;
    private final byte GROUP_ID = 3;


    BookmarksRVAdapter(ArrayList<Book> books, Context context){
        this.books = new ArrayList<>();
        for(Book book : books)
            this.books.add(new Pair<>(0, book));

        this.context = context;
        initAlertDialog();
    }

    public class BookmarksViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener ,View.OnCreateContextMenuListener, View.OnClickListener{
        private TextView bookName;
        private TextView bookmarksCount;
        private ImageView bookCover;

        ItemClickListener itemClickListener;

        BookmarksViewHolder(final View itemView) {
            super(itemView);

            bookName = itemView.findViewById(R.id.book_name);
            bookCover = itemView.findViewById(R.id.book_cover);
            bookmarksCount = itemView.findViewById(R.id.bookmarks_count);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle(selectedBook.getName());
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case 0:
                View v1 = inflater.inflate(R.layout.bookmarks_rv_item, viewGroup, false);
                viewHolder = new BookmarksViewHolder(v1);
                break;

            case 1:
                View v2 = inflater.inflate(R.layout.bookmarks_alt_rv_item, viewGroup, false);
                viewHolder = new BookmarksViewHolder1(v2);
                rvBookmarkPreview = v2.findViewById(R.id.bookmarks_alt_recycler_view);
                rvBookmarkPreview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){

            case 0:
            BookmarksViewHolder bookmarksViewHolder = (BookmarksViewHolder) holder;
            bookmarksViewHolder.bookName.setText(books.get(position).second.getName());
            bookmarksViewHolder.bookmarksCount.setText("Количество закладок: " +
                    String.valueOf(books.get(position).second.getBookmarks().size()));

            Glide.with(context)
                    .load(FileWorker.getInstance().getPicturesPath() + books.get(position).second.getName()+".png")
                    .apply(new RequestOptions().fitCenter().placeholder(R.drawable.e))
                    .into(bookmarksViewHolder.bookCover);

            bookmarksViewHolder.setOnLongClickListener(new ItemClickListener() {
                @Override
                public void onItemViewClick(int pos, boolean isLongClick) {
                    selectedBook = books.get(pos).second;
                    Toast.makeText(context, selectedBook.getName(), Toast.LENGTH_SHORT).show();

                    if(!isLongClick){
                        //CLOSE ALL BOOKMARKS CONTAINERS
                        for(int i=0; i< books.size(); i++)
                            books.set(i, new Pair<>(0, books.get(i).second));

                        books.set(pos, new Pair<>(1, books.get(pos).second));
                        notifyDataSetChanged();
                    }
                }
            });
            break;

            case 1:
                BookmarksViewHolder1 bookmarksViewHolder1 = (BookmarksViewHolder1) holder;
                bookmarksViewHolder1.setOnLongClickListener(new ItemClickListener() {
                    @Override
                    public void onItemViewClick(int pos, boolean isLongClick) {
                        selectedBook = books.get(pos).second;
                        Toast.makeText(context, selectedBook.getName(), Toast.LENGTH_SHORT).show();

                        if(!isLongClick){
                            books.set(pos, new Pair<>(0, books.get(pos).second));
                            notifyDataSetChanged();
                        }
                    }
                });

                BookmarkPreviewRVAdapter bookmarkAdapter = new BookmarkPreviewRVAdapter(books.get(position).second, context);
                rvBookmarkPreview.setAdapter(bookmarkAdapter);
                break;
        }
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

    @Override
    public int getItemViewType(int position) {
        return books.get(position).first == 0 ?  0 : 1;
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

            menu.setHeaderTitle(selectedBook.getName());
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
}
