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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class TabLocalBooks extends Fragment {

    private List<Book> books;
    private RecyclerView recycleView;
    private RVAdapter adapter;

    private void initializeAdapter(){
        adapter = new RVAdapter(books, getActivity());
        recycleView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_local_books, null);

        recycleView = v.findViewById(R.id.recycleView);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(llm);

        books = FileWorker.initializeLocalBooksData(getActivity().getContentResolver());
        initializeAdapter();

        if(books.isEmpty())
            ((TextView)v.findViewById(R.id.tab_local_books_text)).setText("Локальные");

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

    @Override
    public boolean onContextItemSelected(MenuItem item){
        adapter.getItemSelected(item);
        return  super.onContextItemSelected(item);
    }

}
