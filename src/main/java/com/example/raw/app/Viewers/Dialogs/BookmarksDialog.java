package com.example.raw.app.Viewers.Dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.Entities.Bookmark;
import com.example.raw.app.FileWorker;
import com.example.raw.app.R;
import com.example.raw.app.TabKeeper;

import java.util.Date;

public class BookmarksDialog extends DialogFragment implements View.OnClickListener{

    private int currentPage;
    private Book book;
    private Button actionOk;
    private Button actionActOk;
    private EditText input;
    private TextView actionAddText;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookmarks_adding_dialog, container, false);
        getDialog().setTitle("Добавить закладку");

        actionOk = view.findViewById(R.id.bookmarks_adding_action_ok);
        actionActOk = view.findViewById(R.id.bookmarks_adding_action_alt_ok);
        actionActOk.setVisibility(View.GONE);
        actionAddText = view.findViewById(R.id.bookmarks_adding_action_add_text);
        input = view.findViewById(R.id.bookmarks_adding_et_text);
        input.setVisibility(View.GONE);
        currentPage = getArguments().getInt("currentPage") + 1;
        ((TextView) view.findViewById(R.id.bookmarks_adding_tv_pages)).setText("Страница " + currentPage);

        for(Book obj : FileWorker.getInstance().getRecentBooks())
            if(obj.equals((getArguments().getSerializable("book")))){
                book = obj;
                break;
            }

        actionActOk.setOnClickListener(this);
        actionOk.setOnClickListener(this);
        actionAddText.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bookmarks_adding_action_add_text:
                input.setVisibility(View.VISIBLE);
                actionOk.setVisibility(View.GONE);
                actionAddText.setVisibility(View.GONE);
                actionActOk.setVisibility(View.VISIBLE);
                input.requestFocus();
                break;

            case R.id.bookmarks_adding_action_ok:
                onActionClick();
                break;

            case R.id.bookmarks_adding_action_alt_ok:
                onActionClick();
                break;
        }
    }

    private void onActionClick(){
        boolean isContainsBookmark = false;

        for(Bookmark bookmark : book.getBookmarks())
            if(bookmark.getPage() == currentPage &&
                    bookmark.getText().equals(input.getText().toString())){
                isContainsBookmark = true;
                break;
            }

        if(!isContainsBookmark){
            book.addBookmark(new Bookmark(currentPage, new Date().getTime(), input.getText().toString()));
            Toast.makeText(getActivity() , "Закладка добавлена", Toast.LENGTH_SHORT).show();
            FileWorker.getInstance().refreshingJSON(FileWorker.getInstance().getRecentBooks());
        } else
            Toast.makeText(getActivity() , "Закладка уже существует", Toast.LENGTH_SHORT).show();

        dismiss();
    }
}
