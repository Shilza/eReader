package com.example.raw.app.Main.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.raw.app.ItemClickListener;
import com.example.raw.app.R;
import com.example.raw.app.Utils.FileWorker;

import java.util.ArrayList;

public class BookmarksRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Pair<Integer, Book>> books;
    private Context context;
    private Book selectedBook;
    private AlertDialog.Builder ad;

    private RecyclerView rvBookmarkPreview;

    private final byte CONTEXT_MENU_REMOVING = 0;
    private final byte GROUP_ID = 3;

    public BookmarksRVAdapter(ArrayList<Book> books, Context context){
        this.books = new ArrayList<>();
        for(Book book : books)
            this.books.add(new Pair<>(0, book));

        this.context = context;
        iniRemovingBookmarksDialog();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener ,View.OnCreateContextMenuListener, View.OnClickListener{

        private TextView bookName;
        private TextView bookmarksCount;
        private ImageView bookCover;

        ItemClickListener itemClickListener;

        MainViewHolder(final View itemView) {
            super(itemView);

            bookName = itemView.findViewById(R.id.acBookmarksBookName);
            bookCover = itemView.findViewById(R.id.acBookmarksBookCover);
            bookmarksCount = itemView.findViewById(R.id.acBookmarksCount);

            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle(selectedBook.getName());
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
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case 0:
                View v1 = inflater.inflate(R.layout.adt_rv_bookmarks, viewGroup, false);
                viewHolder = new MainViewHolder(v1);
                break;

            case 1:
                View v2 = inflater.inflate(R.layout.adt_rv_bookmarks_preview, viewGroup, false);
                viewHolder = new ExtendedViewHolder(v2);
                rvBookmarkPreview = v2.findViewById(R.id.acBookmarksPreviewRecyclerView);
                rvBookmarkPreview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public ArrayList<Pair<Integer, Book>> getBooks(){
        return books;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 0:
                mainViewHolderPreparing((MainViewHolder)holder, position);
                break;

            case 1:
                extendedViewHolderPreparing((ExtendedViewHolder) holder, position);
                break;
        }
    }

    public void getItemSelected(MenuItem item){
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
        return books.get(position).first == 0 ?  0 : 1;
    }

    public class ExtendedViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{

        ItemClickListener itemClickListener;

        ExtendedViewHolder(final View itemView) {
            super(itemView);

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

    private void mainViewHolderPreparing(MainViewHolder holder, int position){
        holder.bookName.setText(books.get(position).second.getName());
        holder.bookmarksCount.setText(R.string.bookmarks_count +
                String.valueOf(books.get(position).second.getBookmarks().size()));

        Glide.with(context)
                .load(FileWorker.getInstance().getPicturesPath() + books.get(position).second.getName()+".png")
                .apply(new RequestOptions().fitCenter().placeholder(R.drawable.e))
                .into(holder.bookCover);

        holder.setOnLongClickListener(new ItemClickListener() {
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
    }

    private void extendedViewHolderPreparing(ExtendedViewHolder holder, int position){
        holder.setOnLongClickListener(new ItemClickListener() {
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
    }

    private void iniRemovingBookmarksDialog(){
        ad = new AlertDialog.Builder(context);
        ad.setTitle("Удалить");
        ad.setMessage("Действительно хотите удалить все закладки этой книги?");
        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                selectedBook.getBookmarks().clear();

                for(Pair<Integer, Book> pair : books)
                    if(pair.second.equals(selectedBook)){
                        books.remove(pair);
                        break;
                    }

                Toast.makeText(context, "Закладки удалены", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
        ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
    }
}
