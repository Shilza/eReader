package com.example.raw.app.Viewers.Dialogs;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.Entities.Bookmark;
import com.example.raw.app.Utils.FileWorker;
import com.example.raw.app.R;
import com.example.raw.app.Utils.Repository;

import static android.app.Activity.RESULT_OK;


public class BookmarksDialog extends DialogFragment implements View.OnClickListener {

    private int currentPage;
    private Book book;
    private EditText input;
    private ImageView img;

    private ImageButton closeButton;
    private ImageButton imageButton;
    private ImageButton drawButton;
    private ImageButton recordButton;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_adding_bookmarks, container, false);
        getDialog().setTitle(R.string.dialog_title_add_bookmark);

        imageButton = view.findViewById(R.id.dialogBookmarksAddingImageSelect);
        drawButton = view.findViewById(R.id.dialogBookmarksAddingImageDraw);
        recordButton = view.findViewById(R.id.dialogBookmarksAddingRecord);

        input = view.findViewById(R.id.dialogBookmarksAddingEditText);
        img = view.findViewById(R.id.dialogBookmarksAddingImage);
        img.setVisibility(View.GONE);
        closeButton = view.findViewById(R.id.dialogBookmarksAddingCloseImage);
        closeButton.setVisibility(View.GONE);
        currentPage = getArguments().getInt("currentPage");
        ((TextView) view.findViewById(R.id.dialogBookmarksAddingTvPage)).setText(getString(R.string.dialog_page) + (currentPage + 1));

        book = Repository.getInstance().getRecentBooks().get(getArguments().getInt("IndexInRecentBooks"));

        (view.findViewById(R.id.dialogBookmarksAddingActionOk)).setOnClickListener(this);
        imageButton.setOnClickListener(this);
        recordButton.setOnClickListener(this);
        closeButton.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialogBookmarksAddingActionOk:
                if (!isContainsBookmark()) {
                    book.addBookmark(new Bookmark(currentPage, System.currentTimeMillis(), input.getText().toString()));
                    Toast.makeText(getActivity(), R.string.bookmark_added, Toast.LENGTH_SHORT).show();
                    FileWorker.getInstance().refreshingJSON();
                } else
                    Toast.makeText(getActivity(), R.string.bookmarks_does_not_exist, Toast.LENGTH_SHORT).show();

                removingOfDuplicatesBookmarks();

                dismiss();
                break;

            case R.id.dialogBookmarksAddingImageSelect:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
                closeButton.setVisibility(View.VISIBLE);
                break;

            case R.id.dialogBookmarksAddingCloseImage:

                img.setImageDrawable(null);
                img.destroyDrawingCache();
                img.setVisibility(View.GONE);
                closeButton.setVisibility(View.GONE);
                break;

            case R.id.dialogBookmarksAddingRecord:

                imageButton.setEnabled(false);
                drawButton.setEnabled(false);
                recordButton.setEnabled(false);
                break;
        }
    }

    private static final int SELECT_PICTURE = 1;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                img.setImageURI(selectedImageUri);
                img.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean isContainsBookmark() {
        String inputData = input.getText().toString();

        for (Bookmark bookmark : book.getBookmarks()) {
            if (bookmark.getPage() == currentPage &&
                    (bookmark.getText().equals(inputData) || inputData.length() == 0)) {
                return true;
            }
        }

        return false;
    }

    private void removingOfDuplicatesBookmarks() {
        for (Bookmark bookmark : book.getBookmarks())
            if (bookmark.getPage() == currentPage && bookmark.getText().length() == 0)
                for (Bookmark bm : book.getBookmarks())
                    if (bm.getPage() == currentPage && bm.getText().length() > 0) {
                        book.getBookmarks().remove(bookmark);
                        return;
                    }
    }
}
