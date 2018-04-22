package com.example.raw.app.Main.Navigation.Adapters;

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
import com.example.raw.app.RVContextViewHolder;
import com.example.raw.app.RVViewHolder;
import com.example.raw.app.Utils.BookOpener;
import com.example.raw.app.Utils.FileWorker;

import java.util.ArrayList;

public class BookmarksRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Pair<Book, Integer>> books;
    private Context context;
    private Book selectedBook;
    private AlertDialog.Builder ad;

    private RecyclerView rvBookmarkPreview;

    private final byte CONTEXT_MENU_REMOVING = 0;
    private final byte GROUP_ID = 3;

    public BookmarksRVAdapter(ArrayList<Book> books, Context context) {
        this.books = new ArrayList<>();
        for (Book book : books)
            this.books.add(new Pair<>(book, 0));

        this.context = context;
        iniRemovingBookmarksDialog();
    }

    public class MainViewHolder extends RVContextViewHolder{

        private TextView bookName;
        private TextView bookmarksCount;
        private ImageView bookCover;

        MainViewHolder(final View itemView) {
            super(itemView);

            bookName = itemView.findViewById(R.id.acBookmarksBookName);
            bookCover = itemView.findViewById(R.id.acBookmarksBookCover);
            bookmarksCount = itemView.findViewById(R.id.acBookmarksCount);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle(selectedBook.getName());
            menu.add(GROUP_ID, CONTEXT_MENU_REMOVING, 0, "Очистить закладки");
        }

        @Override
        public void onClick(View view){
            int position = getLayoutPosition();
            selectedBook = books.get(position).first;

                //CLOSE ALL BOOKMARKS CONTAINERS
                for (int i = 0; i < books.size(); i++)
                    books.set(i, new Pair<>(books.get(i).first, 0));

                books.set(position, new Pair<>(books.get(position).first, 1));
                notifyDataSetChanged();
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        if (viewType == 0) {
            View v1 = inflater.inflate(R.layout.adt_rv_bookmarks, viewGroup, false);
            viewHolder = new MainViewHolder(v1);
        } else {
            View v2 = inflater.inflate(R.layout.adt_rv_bookmarks_preview, viewGroup, false);
            viewHolder = new ExtendedViewHolder(v2);

            rvBookmarkPreview = v2.findViewById(R.id.acBookmarksPreviewRecyclerView);
            rvBookmarkPreview.setLayoutManager(
                    new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public ArrayList<Pair<Book, Integer>> getBooks() {
        return books;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                mainViewHolderPreparing((MainViewHolder) holder, position);
                break;

            case 1:
                extendedViewHolderPreparing((ExtendedViewHolder) holder, position);
                break;
        }
    }

    public void getItemSelected(MenuItem item) {
        if (item.getGroupId() != GROUP_ID)
            return;

        switch (item.getItemId()) {
            case CONTEXT_MENU_REMOVING:
                ad.show();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return books.get(position).second == 0 ? 0 : 1;
    }

    public class ExtendedViewHolder extends RVViewHolder {

        ExtendedViewHolder(final View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {
            selectedBook = books.get(getLayoutPosition()).first;

            books.set(getLayoutPosition(), new Pair<>(books.get(getLayoutPosition()).first, 0));
            notifyDataSetChanged();
        }
    }

    private void mainViewHolderPreparing(MainViewHolder holder, int position) {
        holder.bookName.setText(books.get(position).first.getName());
        holder.bookmarksCount.setText(context.getString(R.string.bookmarks_count) +
                String.valueOf(books.get(position).first.getBookmarks().size()));

        Glide.with(context)
                .load(FileWorker.getInstance().getPicturesPath() + books.get(position).first.getName())
                .apply(new RequestOptions().fitCenter().placeholder(R.drawable.e))
                .into(holder.bookCover);
    }

    private void extendedViewHolderPreparing(ExtendedViewHolder holder, int position) {
        BookmarkPreviewRVAdapter bookmarkAdapter = new BookmarkPreviewRVAdapter(books.get(position).first, context);
        rvBookmarkPreview.setAdapter(bookmarkAdapter);
    }

    private void iniRemovingBookmarksDialog() {
        ad = new AlertDialog.Builder(context);
        ad.setTitle("Удалить");
        ad.setMessage("Действительно хотите удалить все закладки этой книги?");
        ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                selectedBook.getBookmarks().clear();

                for (Pair<Book, Integer> pair : books)
                    if (pair.second.equals(selectedBook)) {
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
