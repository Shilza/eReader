package com.example.raw.app.Viewers.Dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.Entities.Bookmark;
import com.example.raw.app.Utils.FileWorker;
import com.example.raw.app.R;

import java.io.File;
import java.util.Date;

public class BookmarksDialog extends DialogFragment implements View.OnClickListener {

    private int currentPage;
    private Book book;
    private Button actionOk;
    private Button actionActOk;
    private EditText input;
    private TextView actionAddText;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_adding_bookmarks, container, false);
        getDialog().setTitle(R.string.dialog_title_add_bookmarks);

        actionOk = view.findViewById(R.id.dialogBookmarksAddingActionOk);
        actionActOk = view.findViewById(R.id.dialogBookmarksAddingActionAltOk);
        actionActOk.setVisibility(View.GONE);
        actionAddText = view.findViewById(R.id.dialogBookmarksAddingActionAddText);
        input = view.findViewById(R.id.dialogBookmarksAddingEditText);
        input.setVisibility(View.GONE);
        currentPage = getArguments().getInt("currentPage");
        ((TextView) view.findViewById(R.id.dialogBookmarksAddingTvPage)).setText(getString(R.string.dialog_page) + (currentPage + 1));

        book = FileWorker.getInstance().getRecentBooks().get(getArguments().getInt("IndexInRecentBooks"));

        actionActOk.setOnClickListener(this);
        actionOk.setOnClickListener(this);
        actionAddText.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialogBookmarksAddingActionAddText:
                input.setVisibility(View.VISIBLE);
                actionOk.setVisibility(View.GONE);
                actionAddText.setVisibility(View.GONE);
                actionActOk.setVisibility(View.VISIBLE);
                input.requestFocus();
                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                break;

            case R.id.dialogBookmarksAddingActionOk:
                onActionClick();
                break;

            case R.id.dialogBookmarksAddingActionAltOk:
                onActionClick();
                break;
        }
    }

    private void onActionClick() {
        boolean isContainsBookmark = false;

        for (Bookmark bookmark : book.getBookmarks())
            if (bookmark.getPage() == currentPage &&
                    bookmark.getText().equals(input.getText().toString())) {
                isContainsBookmark = true;
                break;
            }

        if (!isContainsBookmark) {
            book.addBookmark(new Bookmark(currentPage, new Date().getTime(), input.getText().toString()));
            Toast.makeText(getActivity(), R.string.bookmark_added, Toast.LENGTH_SHORT).show();
            FileWorker.getInstance().refreshingJSON(FileWorker.getInstance().getRecentBooks());
        } else
            Toast.makeText(getActivity(), R.string.bookmarks_does_not_exist, Toast.LENGTH_SHORT).show();

        dismiss();
    }
}
