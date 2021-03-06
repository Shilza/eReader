package com.example.raw.app.GUI.Viewers.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.Entities.Bookmark;
import com.example.raw.app.GUI.ItemClickListener;
import com.example.raw.app.R;
import com.example.raw.app.GUI.RVContextViewHolder;
import com.example.raw.app.Utils.BookOpener;
import com.example.raw.app.Utils.FileWorker;
import com.example.raw.app.GUI.Viewers.Activities.BookmarksOfParticularBookActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BookmarkOfParticularBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Book book;
    private ArrayList<Bookmark> bookmarks;
    private Context context;
    private Bookmark selectedBookmark;
    private AlertDialog.Builder ad;

    private final byte CONTEXT_MENU_GOTO = 0;
    private final byte CONTEXT_MENU_REMOVING = 1;
    private final byte GROUP_ID = 4;

    public BookmarkOfParticularBookAdapter(Book book, ArrayList<Bookmark> bookmarks, Context context) {
        this.book = book;
        this.bookmarks = bookmarks;
        this.context = context;
        initAlertDialog();
    }

    public class ViewHolder extends RVContextViewHolder {

        TextView tvPage;
        TextView tvText;
        TextView tvDate;

        ViewHolder(final View itemView) {
            super(itemView);

            tvPage = itemView.findViewById(R.id.acBmOfPartBookmarkPage);
            tvText = itemView.findViewById(R.id.acBmOfPartBookmarkText);
            tvDate = itemView.findViewById(R.id.acBmOfPartBookmarkDate);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            menu.add(GROUP_ID, CONTEXT_MENU_GOTO, 0, "Перейти");
            menu.add(GROUP_ID, CONTEXT_MENU_REMOVING, 0, "Удалить закладку");
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v1 = inflater.inflate(R.layout.adt_rv_bookmarks_particular_book, viewGroup, false);

        return (new BookmarkOfParticularBookAdapter.ViewHolder(v1));
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.tvPage.setText("Страница " + String.valueOf(bookmarks.get(position).getPage() + 1));
        viewHolder.tvText.setText(bookmarks.get(position).getText());

        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
        String date = sdf.format(bookmarks.get(position).getUploadDate());
        viewHolder.tvDate.setText(date);

        viewHolder.setOnLongClickListener((int pos, boolean isLongClick) -> {
            selectedBookmark = bookmarks.get(pos);

            if (!isLongClick)
                gotoPage(pos);
        });
    }

    public void getItemSelected(MenuItem item) {
        if (item.getGroupId() != GROUP_ID)
            return;

        switch (item.getItemId()) {
            case CONTEXT_MENU_GOTO:
                gotoPage(selectedBookmark.getPage());
                break;

            case CONTEXT_MENU_REMOVING:
                ad.show();
                break;
        }
    }

    private void initAlertDialog() {
        ad = new AlertDialog.Builder(context);
        ad.setTitle(R.string.dialog_bopb_adt_delete);
        ad.setMessage(R.string.dialog_bopb_adt_confirmation_of_removal_bookmark);
        ad.setPositiveButton(R.string.dialog_consent, (DialogInterface dialog, int arg1) -> {
            book.getBookmarks().remove(selectedBookmark);
            bookmarks.remove(selectedBookmark);
            FileWorker.getInstance().refreshingJSON();
            notifyDataSetChanged();
        });
        ad.setNegativeButton(R.string.dialog_denial, (DialogInterface dialog, int arg1) -> {
        });
    }

    private void gotoPage(int position) {
        ((BookmarksOfParticularBookActivity) context).finish();
        BookOpener.getInstance().opening(book, bookmarks.get(position).getPage(), context);
    }

}
