package com.example.raw.app;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;


public class TabLocalBooks extends Fragment {

    private  List<Book> books;
    private RecyclerView rv;

    private void initializeData(){
        books = new ArrayList<>();

        ContentResolver cr = getActivity().getContentResolver();

        Uri uri = MediaStore.Files.getContentUri("external");

        String[] projection = null;

        String sortOrder = null; // unordered

        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        //+ MediaStore.Files.FileColumns.MEDIA_TYPE_NONE;
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        String[] selectionArgsPdf = new String[]{ mimeType };
        Cursor cursor = cr.query(uri, projection, selectionMimeType, selectionArgsPdf, sortOrder);// cursor -- allPDFFiles

        if (cursor != null){
            if (cursor.moveToFirst()) {
                do {
                    if(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)) != null){
                        String name = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                        float size = cursor.getFloat(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE)) / (1024*1024);

                        String extension = ".pdf";
                        if(name.contains(extension))
                            name = name.substring(0, name.indexOf(extension));

                        Book book = new Book(name, path, size, R.drawable.e);
                        books.add(book);
                    }
                } while (cursor.moveToNext());
            }

        }
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(books);
        rv.setAdapter(adapter);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.recycleview_activivty, null);

        rv = v.findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        initializeData();
        initializeAdapter();

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
